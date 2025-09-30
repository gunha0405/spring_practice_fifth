package com.example.batch.writer;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.example.question.model.QuestionA;
import com.example.question.repository.QuestionARepository;

import lombok.RequiredArgsConstructor;

@Component("questionAWriter")
@RequiredArgsConstructor
public class QuestionAExcelItemWriter implements ItemWriter<QuestionA>{
	
	private final QuestionARepository questionARepository;
	
	@Override
	public void write(Chunk<? extends QuestionA> chunk) {
		if(chunk.isEmpty()) {
			return;
		}
		questionARepository.saveAll(chunk.getItems());
	}
	
}
