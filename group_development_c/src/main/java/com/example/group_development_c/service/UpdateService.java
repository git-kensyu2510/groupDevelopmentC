package com.example.group_development_c.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.group_development_c.entity.Employee;
import com.example.group_development_c.mapper.UpdateMapper;

@Service
public class UpdateService {

    @Autowired
    private UpdateMapper mapper;

    public Employee findById(Integer employeeId) {
        return mapper.findById(employeeId);
    }

    public void update(Employee employee) {
        mapper.update(employee);
    }
}