package com.example.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.scheduler.QuestionInsertJob;

@Configuration
public class QuartzConfig {
    
    @Bean
    public JobDetail questionInsertJobDetail() {
        JobDetail jobDetail = JobBuilder.newJob(QuestionInsertJob.class)
                .withIdentity("questionInsertJob", "systemTasks")
                .storeDurably()
                .build();

        jobDetail.getJobDataMap().put("lastTarget", "B");

        return jobDetail;
    }

    @Bean
    public Trigger questionInsertTrigger(JobDetail questionInsertJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(questionInsertJobDetail)
                .withIdentity("questionInsertTrigger", "systemTasks")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?")) // 1분마다
                .build();
    }
}
