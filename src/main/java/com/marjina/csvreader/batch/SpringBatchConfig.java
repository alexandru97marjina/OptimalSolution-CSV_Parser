package com.marjina.csvreader.batch;

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
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    private Resource outputResource = new FileSystemResource("output/outputData.csv");

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<Customer> itemReader,
                   ItemProcessor<Customer, Customer>itemProcessor,
                   ItemWriter<Customer> itemWriter
    ) {

        Step step = stepBuilderFactory.get("ETL-file-load")
                .<Customer, Customer>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();


        return jobBuilderFactory.get("ETL-Load")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }
//    @Bean
//    public FlatFileItemWriter<Customer> itemWriter()
//    {
//        //Create writer instance
//        FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();
//
//        //Set output file location
//        writer.setResource(outputResource);
//
//        //All job repetitions should "append" to same output file
//        writer.setAppendAllowed(true);
//
//        //Name field values sequence based on object properties
//        writer.setLineAggregator(new DelimitedLineAggregator<Customer>() {
//            {
//                setDelimiter(",");
//                setFieldExtractor(new BeanWrapperFieldExtractor<Customer>() {
//                    {
//                        setNames(new String[] {"id", "name", "dept", "salary" });
//                    }
//                });
//            }
//        });
//        return writer;
//    }


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
//        lineTokenizer.setQuoteCharacter('"');
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"});
        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);


        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

}
