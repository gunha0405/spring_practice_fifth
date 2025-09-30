package com.example.batch.processor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.category.model.Category;
import com.example.category.repository.CategoryRepository;
import com.example.excel.dto.QuestionADto;
import com.example.excel.validator.QuestionAExcelValidator;
import com.example.question.model.QuestionA;
import com.example.user.model.SiteUser;
import com.example.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component("questionAProcessor")
@RequiredArgsConstructor
public class QuestionAExcelItemProcessor implements ItemProcessor<QuestionADto, QuestionA> {

    private final QuestionAExcelValidator validator;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    // 캐싱 맵 추가
    private final Map<String, SiteUser> userCache = new HashMap<>();
    private final Map<String, Category> categoryCache = new HashMap<>();

    @Override
    public QuestionA process(QuestionADto dto) {
        validator.validate(dto);

        // User 캐싱
        SiteUser user = userCache.computeIfAbsent(dto.getAuthorEmail(), email ->
            userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("작성자 없음: " + email))
        );

        // Category 캐싱
        Category category = categoryCache.computeIfAbsent(dto.getCategoryName(), name ->
            categoryRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("카테고리 없음: " + name))
        );

        QuestionA entity = new QuestionA();
        entity.setSubject(dto.getSubject());
        entity.setContent(dto.getContent());
        entity.setKeyword(dto.getKeyword());
        entity.setTenantId(dto.getTenantId());
        entity.setCreateDate(LocalDateTime.now());
        entity.setAuthor(user);
        entity.setCategory(category);

        return entity;
    }
}

