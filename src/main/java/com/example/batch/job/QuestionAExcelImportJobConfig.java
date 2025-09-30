package com.example.batch.job;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.FilenameUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.batch.processor.QuestionAExcelItemProcessor;
import com.example.batch.reader.CsvItemReader;
import com.example.batch.reader.ExcelItemReader;
import com.example.batch.writer.QuestionAExcelItemWriter;
import com.example.excel.dto.QuestionADto;
import com.example.excel.mapper.QuestionACsvRowMapper;
import com.example.excel.mapper.QuestionARowMapper;
import com.example.question.model.QuestionA;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class QuestionAExcelImportJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final QuestionAExcelItemProcessor processor;
    private final JdbcBatchItemWriter<QuestionA> questionAJdbcWriter; // WriterConfig에서 주입됨

    @Bean
    public Job questionAExcelImportJob(Step questionAExcelImportStep) {
        return new JobBuilder("questionAExcelImportJob", jobRepository)
                .start(questionAExcelImportStep)
                .build();
    }

    @Bean
    public Step questionAExcelImportStep(ItemReader<QuestionADto> questionAReader) {
        return new StepBuilder("questionAExcelImportStep", jobRepository)
                .<QuestionADto, QuestionA>chunk(1000, transactionManager)
                .reader(questionAReader)
                .processor(processor)
                .writer(questionAJdbcWriter)  // WriterConfig에서 정의된 Bean을 주입
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<QuestionADto> questionAReader(
            @Value("#{jobParameters['filePath']}") String filePath
    ) throws IOException {
        File file = new File(filePath);
        String ext = FilenameUtils.getExtension(file.getName());

        if ("csv".equalsIgnoreCase(ext)) {
            return new CsvItemReader<>(file, new QuestionACsvRowMapper());
        } else {
            return new ExcelItemReader<>(file,
                    List.of("subject","content","keyword","tenantid","authoremail","categoryname"),
                    new QuestionARowMapper());
        }
    }
}
