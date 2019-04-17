package com.marjina.csvreader.config;

import com.marjina.csvreader.batch.DataBaseProcessor;
import com.marjina.csvreader.batch.DataBaseWriter;
import com.marjina.csvreader.batch.DumpItemProcessor;
import com.marjina.csvreader.batch.StatisticsListener;
import com.marjina.csvreader.entity.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {


    private Resource outputResource = new FileSystemResource("output/bad-data-"+new SimpleDateFormat("dd-MM-yyyy-HH-mm").format(new Date())+".csv");

    @Bean
    public Job job_toDB(JobBuilderFactory jobBuilderFactory1,
                        StepBuilderFactory stepBuilderFactory1,
                        ItemReader<Customer> csvItemReader
    ) {

        Step step1 = stepBuilderFactory1.get("ETL-file-load")
                .<Customer, Customer>chunk(100)
                .reader(csvItemReader)
                .processor(dataBaseProcessor())
                .writer(dataBaseWriter())
                .listener(new StatisticsListener())
                .build();


        return jobBuilderFactory1.get("ETL-Load")
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }


    @Bean
    public Job job_BadData(JobBuilderFactory jobBuilderFactory2,
                   StepBuilderFactory stepBuilderFactory2,
                   ItemReader<Customer> itemReader
    ) {



        Step step2 = stepBuilderFactory2.get("ETL-file-load")
                .<Customer, Customer>chunk(100)
                .reader(itemReader)
                .processor(dumpProcessor())
                .writer(dumpFileWriter())
                .build();


        return jobBuilderFactory2.get("ETL-Load")
                .incrementer(new RunIdIncrementer())
                .start(step2)
                .build();
    }



    @Bean
    public ItemWriter<Customer> dataBaseWriter(){
        return new DataBaseWriter();
    }

    @Bean
    public ItemProcessor<Customer,Customer> dataBaseProcessor() {
        return new DataBaseProcessor();
    }


    @Bean
    public ItemProcessor<Customer,Customer> dumpProcessor(){
        return new DumpItemProcessor();
    }

    @Bean
    public FlatFileItemReader<Customer> itemReader(@Value("${input}") Resource resource) {

        FlatFileItemReader<Customer> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    @Bean
    public LineMapper<Customer> lineMapper() {

        DefaultLineMapper<Customer> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer =new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"});
        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);


        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }


    @Bean
    public FlatFileItemWriter<Customer> dumpFileWriter()
    {
        FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();
        writer.setResource(outputResource);
        writer.setLineAggregator(new DelimitedLineAggregator<Customer>() {
            { setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<Customer>() {
                    {
                        setNames(new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"});
                    }
                });
            }
        });
        return writer;
    }



}
