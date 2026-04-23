package com.example.group_development_c.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.group_development_c.entity.Employee;

@Mapper
public interface UpdateMapper {

    @Select("""
        SELECT
            employee_id AS employeeId,
            name,
            age,
            start_date AS startDate,
            end_date AS endDate,
            password
        FROM employees
        WHERE employee_id = #{employeeId}
    """)
    Employee findById(Integer employeeId);

    @Update("""
        UPDATE employees
        SET
            name = #{name},
            age = #{age},
            start_date = #{startDate},
            end_date = #{endDate},
            password = #{password}
        WHERE employee_id = #{employeeId}
    """)
    void update(Employee employee);
}