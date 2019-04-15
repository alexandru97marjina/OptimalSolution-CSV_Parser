package com.marjina.csvreader.batch;

import com.marjina.csvreader.config.DBWriter;
import com.marjina.csvreader.config.Dumper;
import com.marjina.csvreader.config.Processor;
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
    public Job job(JobBuilderFactory jobBuilderFactory1,
                   StepBuilderFactory stepBuilderFactory1,
                   ItemReader<Customer> itemReader
    ) {

        Step step1 = stepBuilderFactory1.get("ETL-file-load")
                .<Customer, Customer>chunk(100)
                .reader(itemReader)
                .processor(processor2())
                .writer(dbWriter())
                .build();


        return jobBuilderFactory1.get("ETL-Load")
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }
    @Bean
    public Job job2(JobBuilderFactory jobBuilderFactory2,
                   StepBuilderFactory stepBuilderFactory2,
                   ItemReader<Customer> itemReader
    ) {



        Step step2 = stepBuilderFactory2.get("ETL-file-load")
                .<Customer, Customer>chunk(100)
                .reader(itemReader)
                .processor(dumper())
                .writer(itemWriter2())
                .build();


        return jobBuilderFactory2.get("ETL-Load")
                .incrementer(new RunIdIncrementer())
                .start(step2)
                .build();
    }


    @Bean
    public ItemWriter<Customer> dbWriter(){
        return new DBWriter();
    }

    @Bean
    public ItemProcessor<Customer,Customer> processor2() {
        return new Processor();
    }

    @Bean
    public ItemProcessor<Customer,Customer> dumper(){
        return new Dumper();
    }


    @Bean
    public FlatFileItemWriter<Customer> itemWriter2()
    {
        FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();

        writer.setResource(outputResource);

        writer.setLineAggregator(new DelimitedLineAggregator<Customer>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<Customer>() {
                    {
                        setNames(new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"});

                    }
                });
            }
        });
        return writer;
    }


    @Bean
    public FlatFileItemReader<Customer> itemReader1(@Value("${input}") Resource resource) {

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

}
