package com.example.comment.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.Exception.DataNotFoundException;
import com.example.answer.model.Answer;
import com.example.answer.repository.AnswerRepository;
import com.example.comment.model.Comment;
import com.example.comment.repository.CommentRepository;
import com.example.question.model.QuestionA;
import com.example.question.model.QuestionB;
import com.example.question.repository.QuestionARepository;
import com.example.question.repository.QuestionBRepository;
import com.example.user.model.SiteUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final QuestionARepository questionARepository;
    private final QuestionBRepository questionBRepository;
    private final AnswerRepository answerRepository;
    private final CommentRepository commentRepository;

    public void createForQuestion(Long questionId, String customerId, SiteUser author, String content) {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());

        if ("A".equals(customerId)) {
            QuestionA questionA = questionARepository.findById(questionId)
                    .orElseThrow(() -> new DataNotFoundException("QuestionA not found"));
            comment.setQuestionA(questionA);
        } else if ("B".equals(customerId)) {
            QuestionB questionB = questionBRepository.findById(questionId)
                    .orElseThrow(() -> new DataNotFoundException("QuestionB not found"));
            comment.setQuestionB(questionB);
        }

        commentRepository.save(comment);
    }

    public void createForAnswer(Long answerId, SiteUser author, String content) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new DataNotFoundException("Answer not found"));

        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setAnswer(answer);

        commentRepository.save(comment);
    }

    public Optional<Comment> getComment(Long id) {
        return commentRepository.findById(id);
    }

    public Comment modify(Comment comment, String content) {
        comment.setContent(content);
        comment.setModifyDate(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public Long getAnswerQuestionId(Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new DataNotFoundException("Answer not found"));
        return answer.getQuestionId(); 
    }
}
