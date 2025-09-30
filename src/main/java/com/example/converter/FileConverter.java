package com.example.converter;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileConverter {
	File convert(MultipartFile file) throws IOException;
	boolean supports(String contentType, String extension);
}
