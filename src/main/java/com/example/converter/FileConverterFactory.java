package com.example.converter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileConverterFactory {
	
	private final List<FileConverter> converters;
	
	public File convert(MultipartFile file) throws IOException {
		String contentType = file.getContentType();
		String extension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
		System.out.println("DEBUG contentType=" + contentType + ", extension=" + extension);
		
		return converters.stream()
				.filter(c -> c.supports(contentType, extension))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("지원하지 않는 파일 형식 : " + extension))
				.convert(file);
	}
	
}
