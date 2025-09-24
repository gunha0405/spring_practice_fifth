package com.example.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.example.question.model.BaseQuestion;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QuestionRouter {

    private final QuestionARepository questionARepository;
    private final QuestionBRepository questionBRepository;

    @SuppressWarnings("unchecked")
    public <T extends BaseQuestion> JpaRepository<T, Long> resolve(String tenantId) {
        return (JpaRepository<T, Long>) switch (tenantId) {
            case "A" -> questionARepository;
            case "B" -> questionBRepository;
            default -> throw new IllegalArgumentException("지원하지 않는 tenantId: " + tenantId);
        };
    }
}
