package com.example;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.question.model.QuestionA;
import com.example.question.model.QuestionB;
import com.example.question.repository.QuestionARepository;
import com.example.question.repository.QuestionBRepository;

@SpringBootTest
class SpringPracticeFifthApplicationTests {

	@Autowired
    private QuestionARepository questionARepository;
	
	@Autowired
	private QuestionBRepository questionBRepository;

    @Test
    void testJpa() {        
        QuestionB q1 = new QuestionB();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionBRepository.save(q1);  // 첫번째 질문 저장

        QuestionB q2 = new QuestionB();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionBRepository.save(q2);  // 두번째 질문 저장
    }

}
