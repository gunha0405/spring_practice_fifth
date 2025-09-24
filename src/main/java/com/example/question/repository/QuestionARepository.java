package com.example.question.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.question.model.QuestionA;

public interface QuestionARepository extends JpaRepository<QuestionA, Long> {

    QuestionA findBySubject(String subject);

    QuestionA findBySubjectAndContent(String subject, String content);

    List<QuestionA> findBySubjectLike(String subject);

    Page<QuestionA> findAll(Pageable pageable);

    Optional<QuestionA> findByIdAndTenantId(Long id, String tenantId);

    Page<QuestionA> findAllByTenantId(String tenantId, Pageable pageable);

    // A 고객사 전용
    Page<QuestionA> findBySubjectAndKeyword(String subject, String keyword, Pageable pageable);
}

