package com.example.excel.strategy;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.category.model.Category;
import com.example.category.repository.CategoryRepository;
import com.example.excel.dto.QuestionBDto;
import com.example.excel.mapper.QuestionBRowMapper;
import com.example.excel.parser.ExcelParser;
import com.example.excel.validator.QuestionBExcelValidator;
import com.example.question.model.QuestionB;
import com.example.question.repository.QuestionBRepository;
import com.example.user.model.SiteUser;
import com.example.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service("questionBExcelImport")
@RequiredArgsConstructor
public class QuestionBExcelImportService implements ExcelImportStrategy {

    private final QuestionBRepository questionBRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionBExcelValidator validator;

    @Override
    public void importExcel(MultipartFile file) throws IOException {
        
        List<String> expectedHeaders = List.of(
            "subject", "content", "hashtag", "tenantid", "authoremail", "categoryname"
        );

        
        List<QuestionBDto> dtos = ExcelParser.parse(
            file,
            expectedHeaders,
            new QuestionBRowMapper()
        );

        for (QuestionBDto dto : dtos) {
            validator.validate(dto);

            SiteUser user = userRepository.findByEmail(dto.getAuthorEmail())
                .orElseThrow(() -> new IllegalArgumentException("작성자 없음: " + dto.getAuthorEmail()));

            Category category = categoryRepository.findByName(dto.getCategoryName())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 없음: " + dto.getCategoryName()));

            QuestionB entity = new QuestionB();
            entity.setSubject(dto.getSubject());
            entity.setContent(dto.getContent());
            entity.setHashtag(dto.getHashtag());
            entity.setTenantId(dto.getTenantId());
            entity.setCreateDate(LocalDateTime.now());
            entity.setAuthor(user);
            entity.setCategory(category);

            questionBRepository.save(entity);
        }
    }
}
