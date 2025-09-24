package com.example.answer.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.answer.model.Answer;
import com.example.question.model.QuestionType;

public interface AnswerRepository extends JpaRepository<Answer, Long>{
	List<Answer> findByQuestionIdAndQuestionType(Long questionId, QuestionType type);
	Integer countByQuestionIdAndQuestionType(Long questionId, QuestionType questionType);

    Page<Answer> findByQuestionIdAndQuestionTypeOrderByCreateDateDesc(
            Long questionId, QuestionType questionType, Pageable pageable);


    @Query("""
        SELECT a FROM Answer a
        LEFT JOIN a.voter v
        WHERE a.questionId = :questionId
          AND a.questionType = :questionType
        GROUP BY a
        ORDER BY COUNT(v) DESC
        """)
    Page<Answer> findRecommendedAnswers(@Param("questionId") Long questionId,
                                        @Param("questionType") QuestionType questionType,
                                        Pageable pageable);
    
    Page<Answer> findByAuthorUsername(String username, Pageable pageable);
}
