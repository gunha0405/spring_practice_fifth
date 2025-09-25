package com.example.config;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.scheduler.MyJob;

@Configuration
public class QuartzConfig {
	@Bean
    public JobDetail myJobDetail() {
        return JobBuilder.newJob(MyJob.class)
                .withIdentity("myJob", "group1")
                .storeDurably() 
                .build();
    }

    @Bean
    public Trigger myJobTrigger(JobDetail myJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(myJobDetail)
                .withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10) // 10초마다 실행
                        .repeatForever())
                .build();
    }
}
