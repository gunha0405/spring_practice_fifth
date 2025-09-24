package com.example.answer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.Exception.DataNotFoundException;
import com.example.answer.model.Answer;
import com.example.answer.repository.AnswerRepository;
import com.example.question.model.QuestionType;
import com.example.user.model.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer create(Long questionId, String questionType, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestionId(questionId);
        answer.setQuestionType(QuestionType.valueOf(questionType)); // A or B
        answer.setAuthor(author);
        return this.answerRepository.save(answer);
    }

    public Answer getAnswer(Long id) {
        return this.answerRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("answer not found"));
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }


    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }


    public void vote(Answer answer, SiteUser siteUser) {
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }

    public List<Answer> getAnswersByQuestion(Long questionId, String customerId) {
        QuestionType type = QuestionType.valueOf(customerId); // "A" → QuestionType.A
        return answerRepository.findByQuestionIdAndQuestionType(questionId, type); // ✅ Enum 넘김
    }
    public Integer countByQuestion(Long questionId, QuestionType questionType) {
        return answerRepository.countByQuestionIdAndQuestionType(questionId, questionType);
    }

}
