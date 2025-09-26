package com.example.scheduler;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class LogCleanupJob implements Job{
	
	private static final String ARCHIVE_BASE_DIR = "logs/archive";
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			Path archiveDir = Paths.get(ARCHIVE_BASE_DIR);
			if(!Files.exists(archiveDir)) {
				System.out.println("아카이브 폴더 존재 X : " + archiveDir.toAbsolutePath());
				return;
			}
			
			Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);
			
			Files.walkFileTree(archiveDir, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (attrs.lastModifiedTime().toInstant().isBefore(threshold)) {
                        Files.delete(file);
                        System.out.println("오래된 로그 삭제: " + file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
			
		} catch (IOException e) {
			throw new JobExecutionException("로그 정리 중 오류", e);
		}
	}
	
}
