package com.example.converter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class XlsxToCsvConverter implements FileConverter{
	
	@Override
    public File convert(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("excel_", ".csv");

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream());
             FileWriter writer = new FileWriter(tempFile)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                List<String> cells = new ArrayList<>();
                for (Cell cell : row) {
                    cells.add(cell.toString()
                                   .replace(",", " ")
                                   .replace("\n", " ")
                                   .replace("\r", " ")
                                   .replace("\"", "'"));
                }
                writer.write(String.join(",", cells) + "\n");
            }
        }
        return tempFile;
    }
	
	@Override
	public boolean supports(String contentType, String extension) {
	    return "xlsx".equalsIgnoreCase(extension);
	}
	
}
