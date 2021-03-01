package com.akashneil.batch.configuration;

import com.akashneil.batch.dao.entity.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class PersonBatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    public JsonItemReader<Person> reader() {
        return new JsonItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("Persons.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(Person.class))
                .build();
    }

    @Bean
    public PersonProcessor processor() {
        return new PersonProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Person> writer(final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO person (first_Name, last_Name) VALUES (:firstName, :lastName) ")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importPersonJob(NotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importPersonJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Person> writer) {
        return stepBuilderFactory.get("step1")
                .<Person, Person> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }

}
