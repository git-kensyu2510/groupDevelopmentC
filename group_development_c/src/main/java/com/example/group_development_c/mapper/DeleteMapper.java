package com.example.group_development_c.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.example.group_development_c.entity.Employee;

@Mapper
public interface DeleteMapper {
    /*単一検索 */
    @Select("SELECT * FROM employees WHERE employee_id = #{employeeId}")
    @Results({
        @Result(property = "employeeId",column = "employee_id") 
    })
    List<Employee> selectById(@Param("employeeId") int id);

    /* 複数検索 */
    @Select("SELECT * FROM employees")
    @Results({
        @Result(property = "employeeId",column = "employee_id") 
    })
    List<Employee> selectAll();

    /*削除 */
    @Delete("""
            <script>
                Delete FROM employees WHERE employee_id IN
                <foreach item = "id" collection = "ids" open="(" separator="," close=")">
                    #{id}
                </foreach>
            </script>
            """)
    void deleteByIds(@Param("ids") List<Integer> selectIds);
}
