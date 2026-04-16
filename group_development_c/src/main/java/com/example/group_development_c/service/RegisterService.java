package com.example.group_development_c.service;



import org.springframework.stereotype.Service;

import com.example.group_development_c.entity.Employee;
import com.example.group_development_c.mapper.RegisterMapper;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class RegisterService {
    
@Autowired
    private RegisterMapper mapper;

     //登録
    public void insert (Employee m) {
        mapper.insert(m);
    }

    
}
