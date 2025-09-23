package com.example.question.model;

import java.time.LocalDateTime;

public interface QuestionBase {
    Long getId();
    String getSubject();
    String getContent();
    String getAuthor();
    LocalDateTime getCreateDate();
}
