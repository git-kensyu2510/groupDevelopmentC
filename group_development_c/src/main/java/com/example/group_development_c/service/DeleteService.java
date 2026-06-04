package com.example.group_development_c.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.group_development_c.entity.Employee;
import com.example.group_development_c.mapper.DeleteMapper;

@Service
public class DeleteService {
    @Autowired
    DeleteMapper deleteMapper;

    public List<Employee> selectById(int id){
        return deleteMapper.selectById(id);
    }

    public List<Employee> selectAll(){
        return deleteMapper.selectAll();
    }
    
    public void deleteIds(List<Integer> selectIds){
        deleteMapper.deleteByIds(selectIds);
}
}
