package com.ll.springbatch20240111.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

@Slf4j
@Configuration
public class Hello4JobConfig {
    @Bean
    public Job hello4Job(JobRepository jobRepository, Step hello4Step1) {
        return new JobBuilder("hello4Job", jobRepository)
                .start(hello4Step1)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @JobScope
    @Bean
    public Step hello4Step1(JobRepository jobRepository,
                            ItemReader<String> hello4Step1Reader,
                            ItemProcessor<String, String> hello4Step1Processor,
                            ItemWriter<String> hello4Step1Writer,
                            PlatformTransactionManager platformTransactionManager
    ) {
        return new StepBuilder("hello4Step1", jobRepository)
                .<String, String>chunk(10, platformTransactionManager)
                .reader(hello4Step1Reader)
                .processor(hello4Step1Processor)
                .writer(hello4Step1Writer)
                .build();
    }

    @StepScope
    @Bean
    public ItemReader<String> hello4Step1Reader() {
        return new ListItemReader<>(Arrays.asList("Hello", "World", "4-1"));
    }

    @StepScope
    @Bean
    public ItemProcessor<String, String> hello4Step1Processor() {
        // 데이터를 가공하는 Processor 구현
        // 예시: 간단히 입력된 문자열에 "Processed: "를 접두사로 붙이는 Processor를 만듦
        return item -> "Processed: " + item;
    }

    @StepScope
    @Bean
    public ItemWriter<String> hello4Step1Writer() {
        // 데이터를 출력하는 Writer 구현
        // 예시: 간단히 콘솔에 출력하는 Writer를 만듦
        return items -> items.forEach(System.out::println);
    }
}