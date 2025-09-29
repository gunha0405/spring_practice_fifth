package com.example.question.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.question.model.QuestionA;
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

    Page<QuestionB> findByAuthorUsername(String username, Pageable pageable);
    
    Page<QuestionB> findAllByOrderByCreateDateDesc(Pageable pageable);


    @Query(value = "SELECT q.id, q.subject, q.content, q.create_date, q.modify_date, q.hashtag, " +
            "q.tenant_id, q.view_count, q.author_id, q.category_id " +
            "FROM questionB q " +
            "LEFT JOIN answer a ON q.id = a.question_id AND a.question_type = 'B' " +
            "GROUP BY q.id, q.subject, q.content, q.create_date, q.modify_date, " +
            "q.hashtag, q.tenant_id, q.view_count, q.author_id, q.category_id " +
            "ORDER BY MAX(a.create_date) DESC",
    countQuery = "SELECT COUNT(*) FROM questionB",
    nativeQuery = true)
    Page<QuestionB> findAllOrderByLatestAnswer(Pageable pageable);

    @Query(value = "SELECT q.id, q.subject, q.content, q.create_date, q.modify_date, q.hashtag, " +
            "q.tenant_id, q.view_count, q.author_id, q.category_id " +
            "FROM questionB q " +
            "LEFT JOIN comment c ON q.id = c.questionB_id " +
            "GROUP BY q.id, q.subject, q.content, q.create_date, q.modify_date, " +
            "q.hashtag, q.tenant_id, q.view_count, q.author_id, q.category_id " +
            "ORDER BY MAX(c.create_date) DESC",
    countQuery = "SELECT COUNT(*) FROM questionB",
    nativeQuery = true)
    Page<QuestionB> findAllOrderByLatestComment(Pageable pageable);


}
