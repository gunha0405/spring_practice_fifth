package com.example.scheduler;

import java.time.LocalDateTime;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Component;

import com.example.question.model.QuestionA;
import com.example.question.model.QuestionB;
import com.example.question.repository.QuestionARepository;
import com.example.question.repository.QuestionBRepository;

import lombok.RequiredArgsConstructor;

// 상태 유지 어노테이션
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
@RequiredArgsConstructor
public class QuestionInsertJob implements Job {

    private final QuestionARepository questionARepository;
    private final QuestionBRepository questionBRepository;

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String lastTarget = dataMap.getString("lastTarget");

        if ("B".equals(lastTarget)) {
            QuestionA qa = new QuestionA();
            qa.setSubject("자동 생성 질문 A " + LocalDateTime.now());
            qa.setContent("Quartz Job으로 데이터 넣기");
            qa.setKeyword("auto-keyword " + LocalDateTime.now());
            qa.setTenantId("A");
            qa.setCreateDate(LocalDateTime.now());

            questionARepository.save(qa);
            System.out.println("QuestionA insert 완료 (id=" + qa.getId() + ")");
            dataMap.put("lastTarget", "A");

        } else {
            QuestionB qb = new QuestionB();
            qb.setSubject("자동 생성된 질문B - " + LocalDateTime.now());
            qb.setContent("Quartz Job이 QuestionB에 넣은 데이터");
            qb.setHashtag("auto-hashtag " + LocalDateTime.now());
            qb.setTenantId("B");
            qb.setCreateDate(LocalDateTime.now());

            questionBRepository.save(qb);
            System.out.println("QuestionB insert 완료 (id=" + qb.getId() + ")");
            dataMap.put("lastTarget", "B");
        }
    }
}

