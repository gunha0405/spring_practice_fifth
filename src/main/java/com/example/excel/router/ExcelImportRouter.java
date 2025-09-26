package com.example.excel.router;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.excel.strategy.ExcelImportStrategy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExcelImportRouter {

    private final Map<String, ExcelImportStrategy> strategies;

    public ExcelImportStrategy resolve(String customerId) {
        String beanName = switch (customerId) {
            case "A" -> "questionAExcelImport";
            case "B" -> "questionBExcelImport";
            default -> throw new IllegalArgumentException("지원하지 않는 customerId: " + customerId);
        };

        ExcelImportStrategy strategy = strategies.get(beanName);
        if (strategy == null) {
            throw new IllegalArgumentException("등록되지 않은 전략: " + beanName);
        }
        return strategy;
    }
}
