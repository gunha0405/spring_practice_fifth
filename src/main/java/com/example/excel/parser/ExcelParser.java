package com.example.excel.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

import com.example.excel.dto.QuestionADto;

public class ExcelParser {

	private static final List<String> EXPECTED_HEADERS = List.of(
			"subject", "content", "keyword", "tenantId", "authorEmail", "categoryName"
	);
			
	
	public static List<QuestionADto> parseQuestionA(MultipartFile file) throws IOException {
		List<QuestionADto> rows = new ArrayList<>();
		
		try(Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			
			Map<String, Integer> headerIndex = validateHeaders(sheet);
			
			for(int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null) continue;
				
				QuestionADto dto = new QuestionADto();
				dto.setSubject(getCellValue(row.getCell(headerIndex.get("subject"))));
                dto.setContent(getCellValue(row.getCell(headerIndex.get("content"))));
                dto.setKeyword(getCellValue(row.getCell(headerIndex.get("keyword"))));
                dto.setTenantId(getCellValue(row.getCell(headerIndex.get("tenantId"))));
                dto.setAuthorEmail(getCellValue(row.getCell(headerIndex.get("authorEmail"))));
                dto.setCategoryName(getCellValue(row.getCell(headerIndex.get("categoryName"))));

                rows.add(dto);
			}
			
		}
		
		return rows;
		
	}
	
	private static Map<String, Integer> validateHeaders(Sheet sheet) {
		Row headerRow = sheet.getRow(0);
		if(headerRow == null) {
			throw new IllegalArgumentException("엑셀 파일 헤더가 존재하지 않습니다.");
		}
		
		Map<String, Integer> headerIndex = new HashMap<>();
		Set<String> actualHeaders = new HashSet<>();
		
		for (Cell cell : headerRow) {
			String header = cell.getStringCellValue().trim();
			headerIndex.put(header, cell.getColumnIndex());
			actualHeaders.add(header);
		}
		
		if(!actualHeaders.containsAll(EXPECTED_HEADERS)) {
			throw new IllegalArgumentException("엑셀 헤더가 형식에 알맞지 않습니다.");
		}
		
		return headerIndex;
		
	}
	
	private static String getCellValue(Cell cell) {
		if (cell == null) return null;
		
		return switch (cell.getCellType()) {
		case STRING -> cell.getStringCellValue().trim();
		case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
		case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
		case BLANK -> null;
		default -> cell.toString().trim();
		};
	}
	
}
