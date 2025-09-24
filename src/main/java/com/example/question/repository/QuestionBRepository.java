package com.example.question.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.question.model.QuestionB;

public interface QuestionBRepository extends JpaRepository<QuestionB, Long> {

    QuestionB findBySubject(String subject);

    QuestionB findBySubjectAndContent(String subject, String content);

    List<QuestionB> findBySubjectLike(String subject);

    Page<QuestionB> findAll(Pageable pageable);

    Optional<QuestionB> findByIdAndTenantId(Long id, String tenantId);

    Page<QuestionB> findAllByTenantId(String tenantId, Pageable pageable);

    // B 고객사 전용
    Page<QuestionB> findBySubjectOrHashtag(String subject, String hashtag, Pageable pageable);
}
