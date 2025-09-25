package com.example.question.service.factory;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.category.model.Category;
import com.example.question.model.BaseQuestion;
import com.example.question.model.QuestionA;
import com.example.question.repository.QuestionARepository;
import com.example.user.model.SiteUser;

import lombok.RequiredArgsConstructor;

@Component("A")
@RequiredArgsConstructor
public class QuestionAFactory implements QuestionFactory {

    private final QuestionARepository questionARepository;

    @Override
    public BaseQuestion create(String subject, String content,
                               String keyword, String hashtag, Category category,
                               String tenantId, SiteUser user) {
        QuestionA q = new QuestionA();
        q.setSubject(subject);
        q.setContent(content);
        q.setKeyword(keyword);   // A 전용
        q.setAuthor(user);
        q.setCategory(category);
        q.setTenantId(tenantId);
        q.setCreateDate(LocalDateTime.now());
        return questionARepository.save(q);
    }

    @Override
    public void modify(BaseQuestion question, String subject,
                       String content, String keyword, String hashtag) {
        if (question instanceof QuestionA q) {
            q.setSubject(subject);
            q.setContent(content);
            q.setKeyword(keyword);
            q.setModifyDate(LocalDateTime.now());
            questionARepository.save(q);
        }
    }

    @Override
    public Page<QuestionA> search(String subject, String value, Pageable pageable) {
        return questionARepository.findBySubjectAndKeyword(subject, value, pageable);
    }
    
    @Override
    public Page<QuestionA> getUserQuestions(String username, Pageable pageable) {
        return questionARepository.findByAuthorUsername(username, pageable);
    }
    
    @Override
    public Page<QuestionA> getListOrderByLatestQuestion(Pageable pageable) {
        return questionARepository.findAllByOrderByCreateDateDesc(pageable);
    }

    @Override
    public Page<QuestionA> getListOrderByLatestAnswer(Pageable pageable) {
        return questionARepository.findAllOrderByLatestAnswer(pageable);
    }

    @Override
    public Page<QuestionA> getListOrderByLatestComment(Pageable pageable) {
        return questionARepository.findAllOrderByLatestComment(pageable);
    }
}

