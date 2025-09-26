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
import com.example.excel.mapper.ExcelRowMapper;

public class ExcelParser {

    public static <T> List<T> parse(MultipartFile file, 
                                    List<String> expectedHeaders,
                                    ExcelRowMapper<T> mapper) throws IOException {
        List<T> rows = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            Map<String, Integer> headerIndex = validateHeaders(sheet, expectedHeaders);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                rows.add(mapper.mapRow(row, headerIndex));
            }
        }
        return rows;
    }

    private static Map<String, Integer> validateHeaders(Sheet sheet, List<String> expectedHeaders) {
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new IllegalArgumentException("엑셀 파일에 헤더가 없습니다.");
        }

        Map<String, Integer> headerIndex = new HashMap<>();
        Set<String> actualHeaders = new HashSet<>();

        for (Cell cell : headerRow) {
            String header = cell.getStringCellValue().trim().toLowerCase();
            headerIndex.put(header, cell.getColumnIndex());
            actualHeaders.add(header);
        }

        if (!actualHeaders.containsAll(expectedHeaders.stream().map(String::toLowerCase).toList())) {
            throw new IllegalArgumentException("엑셀 헤더가 예상과 다릅니다.");
        }
        return headerIndex;
    }

    public static String getCellValue(Cell cell) {
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
