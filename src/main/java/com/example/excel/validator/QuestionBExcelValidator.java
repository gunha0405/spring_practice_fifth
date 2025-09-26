package com.example.excel.validator;

import org.springframework.stereotype.Component;
import com.example.excel.dto.QuestionBDto;

@Component
public class QuestionBExcelValidator {
	
	public void validate(QuestionBDto dto) {
		if(dto.getSubject() == null || dto.getSubject().isBlank()) {
			throw new IllegalArgumentException("제목은 필수값입니다.");
		} 
		if(dto.getCategoryName() == null || dto.getCategoryName().isBlank()) {
			throw new IllegalArgumentException("카테고리는 필수값입니다.");
		}
		if(dto.getHashtag() == null || dto.getHashtag().isBlank()) {
			throw new IllegalArgumentException("키워드는 필수값입니다.");
		}
		if(dto.getContent() == null || dto.getContent().isBlank()) {
			throw new IllegalArgumentException("내용은 필수값입니다.");
		}
		if(dto.getTenantId() == null || dto.getTenantId().isBlank()) {
			throw new IllegalArgumentException("tenantId값은 필수값입니다.");
		}
	}
	
}