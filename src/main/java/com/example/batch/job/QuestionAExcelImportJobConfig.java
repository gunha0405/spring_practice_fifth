package com.example.batch.job;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.batch.processor.QuestionAExcelItemProcessor;
import com.example.batch.reader.ExcelItemReader;
import com.example.batch.writer.QuestionAExcelItemWriter;
import com.example.excel.dto.QuestionADto;
import com.example.excel.mapper.QuestionARowMapper;
import com.example.question.model.QuestionA;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class QuestionAExcelImportJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final QuestionAExcelItemProcessor processor;
    private final QuestionAExcelItemWriter writer;

    @Bean
    public Job questionAExcelImportJob(Step questionAExcelImportStep) {
        return new JobBuilder("questionAExcelImportJob", jobRepository)
                .start(questionAExcelImportStep)
                .build();
    }

    @Bean
    public Step questionAExcelImportStep(ExcelItemReader<QuestionADto> reader) {
        return new StepBuilder("questionAExcelImportStep", jobRepository)
                .<QuestionADto, QuestionA>chunk(1000, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    
    @Bean
    @StepScope
    public ExcelItemReader<QuestionADto> questionAReader(
            @Value("#{jobParameters['filePath']}") String filePath
    ) throws IOException {
        List<String> expectedHeaders = List.of("subject", "content", "keyword", "tenantid", "authoremail", "categoryname");
        return new ExcelItemReader<>(
                new File(filePath),
                expectedHeaders,
                new QuestionARowMapper()
        );
    }
}


