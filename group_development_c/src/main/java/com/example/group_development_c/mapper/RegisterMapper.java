package com.example.group_development_c.mapper;


import org.apache.ibatis.annotations.Mapper;

import com.example.group_development_c.entity.Employee;

import org.apache.ibatis.annotations.Insert;

@Mapper
public interface RegisterMapper {

    //登録
@Insert("INSERT INTO employees (name, age, start_date, end_date, password) " +
        "VALUES (#{name}, #{age}, #{startDate}, #{endDate}, #{password})")
void insert(Employee employee);
    
}
