package com.example.batch.reader;

import java.io.BufferedReader;
import java.io.Closeable;
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
public class CsvItemReader<T> implements ItemReader<T>, Closeable {
    private final BufferedReader br;
    private final CsvRowMapper<T> mapper;

    private String[] headers;
    private boolean headerRead = false;

    public CsvItemReader(File file, CsvRowMapper<T> mapper) throws IOException {
        this.br = new BufferedReader(new FileReader(file));
        this.mapper = mapper;
    }

    @Override
    public T read() throws IOException {
        if (!headerRead) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                return null;
            }
            headers = headerLine.split(",");
            headerRead = true;
        }

        String line = br.readLine();
        if (line == null) {
            return null;
        }

        String[] values = line.split(",");
        return mapper.mapRow(headers, values);
    }

    @Override
    public void close() throws IOException {
        br.close();
    }
}

