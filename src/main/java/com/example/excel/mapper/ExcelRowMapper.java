package com.example.excel.mapper;

import java.util.Map;

import org.apache.poi.ss.usermodel.Row;

public interface ExcelRowMapper<T> {
	T mapRow(Row row, Map<String, Integer> headerIndex);
}
