package com.example.excel.strategy;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.category.model.Category;
import com.example.category.repository.CategoryRepository;
import com.example.excel.dto.QuestionADto;
import com.example.excel.mapper.QuestionARowMapper;
import com.example.excel.parser.ExcelParser;
import com.example.excel.validator.QuestionAExcelValidator;
import com.example.question.model.QuestionA;
import com.example.question.repository.QuestionARepository;
import com.example.user.model.SiteUser;
import com.example.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service("questionAExcelImport")
@RequiredArgsConstructor
public class QuestionAExcelImportService implements ExcelImportStrategy {

    private final QuestionARepository questionARepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionAExcelValidator validator;

    @Override
    public void importExcel(MultipartFile file) throws IOException {
        
        List<String> expectedHeaders = List.of(
            "subject", "content", "keyword", "tenantid", "authoremail", "categoryname"
        );

        
        List<QuestionADto> dtos = ExcelParser.parse(
            file,
            expectedHeaders,
            new QuestionARowMapper()
        );

        for (QuestionADto dto : dtos) {
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

            questionARepository.save(entity);
        }
    }
}
