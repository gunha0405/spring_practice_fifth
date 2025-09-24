package com.example.question.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.example.answer.model.Answer;
import com.example.category.model.Category;
import com.example.comment.model.Comment;
import com.example.user.model.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class QuestionB implements BaseQuestion{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    private String hashtag;   // B 전용 필드

    private String tenantId;
    
    @Column(columnDefinition = "INT NOT NULL DEFAULT 0")
    private int viewCount;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    private Set<SiteUser> voter;
    
    @OneToMany(mappedBy = "questionB")
    private List<Comment> commentList;
    
    @ManyToOne
    private Category category;
    
    @Override
    public QuestionType getQuestionType() {
        return QuestionType.B;
    }
}
