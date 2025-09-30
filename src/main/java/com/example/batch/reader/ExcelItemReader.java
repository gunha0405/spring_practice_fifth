package com.example.batch.reader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;

import com.example.excel.mapper.ExcelRowMapper;
import com.example.excel.parser.ExcelParser;

public class ExcelItemReader<T> implements ItemReader<T> {

    private final Iterator<T> iterator;

    public ExcelItemReader(File file,
                           List<String> expectedHeaders,
                           ExcelRowMapper<T> mapper) throws IOException {
        List<T> dataList = ExcelParser.parse(file, expectedHeaders, mapper);
        this.iterator = dataList.iterator();
    }

    @Override
    public T read() {
        return iterator.hasNext() ? iterator.next() : null;
    }
}
