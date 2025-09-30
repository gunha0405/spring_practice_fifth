package com.example.batch.writer;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.question.model.QuestionA;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class QuestionAExcelItemWriterConfig {

	@Bean
	public JdbcBatchItemWriter<QuestionA> questionAJdbcWriter(DataSource dataSource) {
	    return new JdbcBatchItemWriterBuilder<QuestionA>()
	            .dataSource(dataSource)
	            .sql("INSERT INTO questiona (subject, content, keyword, tenant_id, author_id, category_id, create_date) " +
	                 "VALUES (:subject, :content, :keyword, :tenantId, :author.id, :category.id, :createDate)")
	            .beanMapped()
	            .build();
	}
}

