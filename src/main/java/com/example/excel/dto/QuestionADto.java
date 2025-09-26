package com.example.excel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionADto {
	private String subject;
	private String content;
	private String keyword;
	private String tenantId;
	private String authorEmail; // 작성자 매핑
	private String categoryName; // 카테고리 매핑
}
