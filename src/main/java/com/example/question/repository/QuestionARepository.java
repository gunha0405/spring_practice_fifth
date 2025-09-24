package com.example.question.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
    
    Page<QuestionA> findByAuthorUsername(String username, Pageable pageable);
    
    Page<QuestionA> findAllByOrderByCreateDateDesc(Pageable pageable);


 
    @Query(value = "SELECT q.* " +
                   "FROM questionA q " +
                   "LEFT JOIN answer a ON q.id = a.question_id AND a.question_type = 'A' " +
                   "GROUP BY q.id " +
                   "ORDER BY MAX(a.create_date) DESC",
           countQuery = "SELECT COUNT(*) FROM question_a",
           nativeQuery = true)
    Page<QuestionA> findAllOrderByLatestAnswer(Pageable pageable);


    @Query(value = "SELECT q.* " +
                   "FROM questionA q " +
                   "LEFT JOIN comment c ON q.id = c.questionA_id " +
                   "GROUP BY q.id " +
                   "ORDER BY MAX(c.create_date) DESC",
           countQuery = "SELECT COUNT(*) FROM questionA",
           nativeQuery = true)
    Page<QuestionA> findAllOrderByLatestComment(Pageable pageable);
    
}

