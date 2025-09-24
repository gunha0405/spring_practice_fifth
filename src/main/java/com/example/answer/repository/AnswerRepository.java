package com.example.answer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.answer.model.Answer;
import com.example.question.model.QuestionType;

public interface AnswerRepository extends JpaRepository<Answer, Long>{
	List<Answer> findByQuestionIdAndQuestionType(Long questionId, QuestionType type);
	Integer countByQuestionIdAndQuestionType(Long questionId, QuestionType questionType);
}
