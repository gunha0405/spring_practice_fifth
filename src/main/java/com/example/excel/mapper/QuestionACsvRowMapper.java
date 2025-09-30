package com.example.excel.mapper;

import com.example.excel.dto.QuestionADto;

public class QuestionACsvRowMapper implements CsvRowMapper<QuestionADto> {

    @Override
    public QuestionADto mapRow(String[] headers, String[] values) {
        QuestionADto dto = new QuestionADto();

        for (int i = 0; i < headers.length; i++) {
            String header = headers[i].trim().toLowerCase();
            String value = i < values.length ? values[i].trim() : null;

            switch (header) {
                case "subject" -> dto.setSubject(value);
                case "content" -> dto.setContent(value);
                case "keyword" -> dto.setKeyword(value);
                case "tenantid" -> dto.setTenantId(value);
                case "authoremail" -> dto.setAuthorEmail(value);
                case "categoryname" -> dto.setCategoryName(value);
                default -> {
                    
                }
            }
        }

        return dto;
    }
}
