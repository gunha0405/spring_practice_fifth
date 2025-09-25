package com.example.question.model;

import java.time.LocalDateTime;
import java.util.Set;

import com.example.user.model.SiteUser;

public interface BaseQuestion {
    Long getId();
    String getSubject();
    String getContent();
    String getTenantId();
    int getViewCount();
    LocalDateTime getCreateDate();
    LocalDateTime getModifyDate();
    SiteUser getAuthor();
    Set<SiteUser> getVoter();
    
    public abstract QuestionType getQuestionType();
    
    void setSubject(String subject);
    void setContent(String content);
    void setAuthor(SiteUser author);
    void setTenantId(String tenantId);
    void setCreateDate(LocalDateTime createDate);
    void setModifyDate(LocalDateTime modifyDate);
	void setViewCount(int viewCount);
}
