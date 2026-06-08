package com.example.group_development_c.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SearchMapper {

    @Select("""
        SELECT COUNT(*)
        FROM employees
        WHERE employee_id = #{employeeId}
    """)
    int countById(Integer employeeId);
}