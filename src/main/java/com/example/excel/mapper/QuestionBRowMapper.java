package com.example.excel.mapper;

import java.util.Map;

import org.apache.poi.ss.usermodel.Row;

import com.example.excel.dto.QuestionBDto;
import com.example.excel.parser.ExcelParser;

public class QuestionBRowMapper implements ExcelRowMapper<QuestionBDto>{
	@Override
	public QuestionBDto mapRow(Row row, Map<String, Integer> headerIndex) {
		QuestionBDto dto = new QuestionBDto();
		dto.setSubject(ExcelParser.getCellValue(row.getCell(headerIndex.get("subject"))));
        dto.setContent(ExcelParser.getCellValue(row.getCell(headerIndex.get("content"))));
        dto.setHashtag(ExcelParser.getCellValue(row.getCell(headerIndex.get("hashtag"))));
        dto.setTenantId(ExcelParser.getCellValue(row.getCell(headerIndex.get("tenantid"))));
        dto.setAuthorEmail(ExcelParser.getCellValue(row.getCell(headerIndex.get("authoremail"))));
        dto.setCategoryName(ExcelParser.getCellValue(row.getCell(headerIndex.get("categoryname"))));
        return dto;
	}
}
