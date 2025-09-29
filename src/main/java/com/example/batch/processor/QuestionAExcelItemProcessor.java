package com.example.batch.processor;

import java.time.LocalDateTime;

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

    @Override
    public QuestionA process(QuestionADto dto) {
        validator.validate(dto);

        SiteUser user = userRepository.findByEmail(dto.getAuthorEmail())
            .orElseThrow(() -> new IllegalArgumentException("작성자 없음: " + dto.getAuthorEmail()));

        Category category = categoryRepository.findByName(dto.getCategoryName())
            .orElseThrow(() -> new IllegalArgumentException("카테고리 없음: " + dto.getCategoryName()));

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
