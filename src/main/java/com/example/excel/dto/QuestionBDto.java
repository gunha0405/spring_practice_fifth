package com.example.excel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionBDto {
	private String subject;
	private String content;
	private String hashtag;
	private String tenantId;
	private String authorEmail;
	private String categoryName;
}
