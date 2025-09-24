package com.example.question.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.Exception.DataNotFoundException;
import com.example.question.model.BaseQuestion;
import com.example.question.repository.QuestionRouter;
import com.example.question.service.factory.QuestionFactory;
import com.example.user.model.SiteUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final Map<String, QuestionFactory> factories; // tenantId → Factory 매핑
    private final QuestionRouter questionRouter;          // Repository 라우터

    public Page<? extends BaseQuestion> getList(int page, String tenantId) {
        List<Sort.Order> sorts = List.of(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return questionRouter.resolve(tenantId).findAll(pageable);
    }

    public BaseQuestion getQuestion(Long id, String tenantId) {
        return questionRouter.resolve(tenantId).findById(id)
            .orElseThrow(() -> new DataNotFoundException("question not found"));
    }

    public BaseQuestion create(String subject, String content,
                               String keyword, String hashtag,
                               String tenantId, SiteUser user) {
        return factories.get(tenantId).create(subject, content, keyword, hashtag, tenantId, user);
    }

    public void modify(BaseQuestion question, String subject,
                       String content, String keyword, String hashtag) {
        factories.get(question.getTenantId()).modify(question, subject, content, keyword, hashtag);
    }

    public void delete(BaseQuestion question) {
        questionRouter.resolve(question.getTenantId()).delete(question);
    }

    public void vote(BaseQuestion question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        questionRouter.resolve(question.getTenantId()).save(question);
    }

    public Page<? extends BaseQuestion> search(String subject, String value, String tenantId, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createDate")));
        return factories.get(tenantId).search(subject, value, pageable);
    }
}
