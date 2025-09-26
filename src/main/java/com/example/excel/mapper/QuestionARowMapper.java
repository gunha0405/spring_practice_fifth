package com.example.excel.mapper;

import java.util.Map;

import org.apache.poi.ss.usermodel.Row;

import com.example.excel.dto.QuestionADto;
import com.example.excel.parser.ExcelParser;

public class QuestionARowMapper implements ExcelRowMapper<QuestionADto> {
    @Override
    public QuestionADto mapRow(Row row, Map<String, Integer> headerIndex) {
        QuestionADto dto = new QuestionADto();
        dto.setSubject(ExcelParser.getCellValue(row.getCell(headerIndex.get("subject"))));
        dto.setContent(ExcelParser.getCellValue(row.getCell(headerIndex.get("content"))));
        dto.setKeyword(ExcelParser.getCellValue(row.getCell(headerIndex.get("keyword"))));
        dto.setTenantId(ExcelParser.getCellValue(row.getCell(headerIndex.get("tenantid"))));
        dto.setAuthorEmail(ExcelParser.getCellValue(row.getCell(headerIndex.get("authoremail"))));
        dto.setCategoryName(ExcelParser.getCellValue(row.getCell(headerIndex.get("categoryname"))));
        return dto;
    }
}
