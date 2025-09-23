package com.example.question.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
}
