package com.example.answer.model;

import java.time.LocalDateTime;

import com.example.question.model.QuestionA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Entity 
public class AnswerA { 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer id;

    @Column(columnDefinition = "TEXT") 
    private String content; 

    private LocalDateTime createDate; 

    @ManyToOne 
    private QuestionA question; 
}