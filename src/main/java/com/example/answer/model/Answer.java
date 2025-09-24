package com.example.answer.model;

import java.time.LocalDateTime;
import java.util.Set;

import com.example.question.model.QuestionType;
import com.example.user.model.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column(columnDefinition = "TEXT") 
    private String content; 

    private LocalDateTime createDate; 
    
    private LocalDateTime modifyDate;

    private Long questionId;
    
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;
    
    @ManyToOne
    private SiteUser author;
    
    @ManyToMany
    Set<SiteUser> voter;
}
