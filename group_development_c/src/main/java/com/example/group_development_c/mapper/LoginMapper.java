package com.example.group_development_c.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.example.group_development_c.entity.Employee;

@Mapper
public interface LoginMapper {
    @Select("SELECT * FROM employees WHERE employee_id = #{employeeId} AND password = #{password}")
        @Results({
        @Result(property = "employeeId",column = "employee_id") 
    })
    Employee selectByLogin(
        @Param("employeeId") int employeeId,
        @Param("password") String password);
}