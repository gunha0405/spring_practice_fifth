package com.example.question.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.question.model.QuestionA;

public interface QuestionARepository extends JpaRepository<QuestionA, Long>{
	QuestionA findBySubject(String subject);
	QuestionA findBySubjectAndContent(String subject, String content);
	List<QuestionA> findBySubjectLike(String subject);
}
