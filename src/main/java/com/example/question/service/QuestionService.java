package com.example.question.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.Exception.DataNotFoundException;
import com.example.question.model.Question;
import com.example.question.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> getList() {
        return this.questionRepository.findAll();
    }
    
    public Question getQuestion(Long id) {  
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }
    
    public void create(String subject, String content, String keyword, String hashtag) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setHashtag(hashtag);
        q.setKeyword(keyword);
        q.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q);
    }
}
