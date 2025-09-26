package com.example.scheduler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.sql.DataSource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;


import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DatabaseBackupJob implements Job {

    private final DataSource dataSource;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String fileName = "mydb_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) + ".bak";
        String backupPath = "/var/opt/mssql/backups/" + fileName;

        String sql = "BACKUP DATABASE myappdb_kor TO DISK = ? WITH INIT, STATS = 10";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, backupPath);
            ps.execute();
            System.out.println("MSSQL 백업 완료: " + backupPath);

        } catch (Exception e) {
            throw new JobExecutionException("DB 백업 실패", e);
        }
    }
}