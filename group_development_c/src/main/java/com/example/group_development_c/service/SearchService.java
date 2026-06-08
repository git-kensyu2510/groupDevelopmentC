package com.example.group_development_c.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.group_development_c.mapper.SearchMapper;

@Service
public class SearchService {

    @Autowired
    private SearchMapper mapper;

    public boolean existsById(Integer employeeId) {
        return mapper.countById(employeeId) > 0;
    }
}