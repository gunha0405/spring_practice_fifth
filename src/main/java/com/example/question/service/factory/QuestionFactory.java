package com.example.question.service.factory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.category.model.Category;
import com.example.question.model.BaseQuestion;
import com.example.user.model.SiteUser;

public interface QuestionFactory {
    BaseQuestion create(String subject, String content,
                        String keyword, String hashtag, Category category,
                        String tenantId, SiteUser user);

    void modify(BaseQuestion question, String subject,
                String content, String keyword, String hashtag);

    Page<? extends BaseQuestion> search(String subject, String value, Pageable pageable);
    
    Page<? extends BaseQuestion> getUserQuestions(String username, Pageable pageable);
    
    Page<? extends BaseQuestion> getListOrderByLatestQuestion(Pageable pageable);
    Page<? extends BaseQuestion> getListOrderByLatestAnswer(Pageable pageable);
    Page<? extends BaseQuestion> getListOrderByLatestComment(Pageable pageable);

}
