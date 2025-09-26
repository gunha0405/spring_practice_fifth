package com.example.scheduler;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class LogArchiveJob implements Job{
	
	private static final String LOG_DIR = "logs";
	private static final String ARCHIVE_BASE_DIR = "logs/archive";
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			Path logDir = Paths.get(LOG_DIR);
			if(!Files.exists(logDir)) {
				System.out.println("로그 폴더 존재 X : " + logDir.toAbsolutePath());
				return;
			}
			
			String monthDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
			Path archiveDir = Paths.get(ARCHIVE_BASE_DIR, monthDir);
			Files.createDirectories(archiveDir);
			
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(logDir, "*.gz")) {
				for (Path file : stream) {
					Path target = archiveDir.resolve(file.getFileName());
					Files.move(file, target, StandardCopyOption.REPLACE_EXISTING);
					System.out.print("로그 아카이브 완료 : " + target);
				}
			}
		} catch (IOException e) {
			throw new JobExecutionException("로그 아카이브 오류", e);
		}
	}
	
}
