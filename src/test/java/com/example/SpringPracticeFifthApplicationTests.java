package com.example;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.question.service.QuestionService;



@SpringBootTest
class SpringPracticeFifthApplicationTests {

	@Autowired
    private QuestionService questionService;

    @Test
    void testJpa() {        
    	for (int i = 1; i <= 300; i++) {
            String subject = String.format("테스트 데이터입니다:[%03d]", i);
            String content = "내용무";
            String hashtag = String.format("테스트 데이터입니다:[%03d]", i);
            String keyword = "해시태그";
            this.questionService.create(subject, content, keyword, hashtag, null);
        }
    }

}
