package com.example.question.service.factory;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.category.model.Category;
import com.example.question.model.BaseQuestion;
import com.example.question.model.QuestionB;
import com.example.question.repository.QuestionBRepository;
import com.example.user.model.SiteUser;

import lombok.RequiredArgsConstructor;

@Component("B")
@RequiredArgsConstructor
public class QuestionBFactory implements QuestionFactory {

    private final QuestionBRepository questionBRepository;

    @Override
    public BaseQuestion create(String subject, String content,
                               String keyword, String hashtag, Category category,
                               String tenantId, SiteUser user) {
        QuestionB q = new QuestionB();
        q.setSubject(subject);
        q.setContent(content);
        q.setHashtag(hashtag);   // B 전용
        q.setAuthor(user);
        q.setCategory(category);
        q.setTenantId(tenantId);
        q.setCreateDate(LocalDateTime.now());
        return questionBRepository.save(q);
    }

    @Override
    public void modify(BaseQuestion question, String subject,
                       String content, String keyword, String hashtag) {
        if (question instanceof QuestionB q) {
            q.setSubject(subject);
            q.setContent(content);
            q.setHashtag(hashtag);
            q.setModifyDate(LocalDateTime.now());
            questionBRepository.save(q);
        }
    }

    
    @Override
    public Page<QuestionB> search(String subject, String value, Pageable pageable) {
        return questionBRepository.findBySubjectOrHashtag(subject, value, pageable);
    }
}
