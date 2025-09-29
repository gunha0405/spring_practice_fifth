package com.example.comment.model;

import java.time.LocalDateTime;

import com.example.answer.model.Answer;
import com.example.question.model.QuestionA;
import com.example.question.model.QuestionB;
import com.example.question.model.QuestionType;
import com.example.user.model.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private SiteUser author;
	
	@Column(columnDefinition = "NVARCHAR(MAX)")
	private String content;
	
	private LocalDateTime createDate;
	
	private LocalDateTime modifyDate;
	
	@ManyToOne
	private QuestionA questionA;
	
	@ManyToOne
	private QuestionB questionB;
	
	@ManyToOne
	private Answer answer;
	
	public Long getQuestionId() {
	    if (this.questionA != null) {
	        return this.questionA.getId();
	    } else if (this.questionB != null) {
	        return this.questionB.getId();
	    } else if (this.answer != null) {
	        return this.answer.getQuestionId();
	    }
	    return null;
	}

	
}
