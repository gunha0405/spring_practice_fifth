package com.example.batch.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;

import com.example.excel.mapper.CsvRowMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CsvItemReader<T> implements ItemReader<T> {
	private final Iterator<T> iterator;
	
	public CsvItemReader(File file, CsvRowMapper<T> mapper) throws IOException {
		List<T> dataList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String headerLine = br.readLine();
			String[] headers = headerLine.split(",");
			
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				dataList.add(mapper.mapRow(headers, values));
			}
			
		}
		this.iterator = dataList.iterator();
	}
	
	@Override
	public T read() {
		return iterator.hasNext() ? iterator.next() : null;
	}
}
