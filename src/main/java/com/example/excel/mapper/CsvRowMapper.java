package com.example.excel.mapper;

public interface CsvRowMapper<T> {
	T mapRow(String[] headers, String[] values);
}
