package com.example.question.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.question.model.QuestionB;

public interface QuestionBRepository extends JpaRepository<QuestionB, Long>{
	QuestionB findBySubject(String subject);
	QuestionB findBySubjectAndContent(String subject, String content);
	List<QuestionB> findBySubjectLike(String subject);
}
