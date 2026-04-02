package com.example.group_development_c.entity;

import java.sql.Date;

import lombok.Data;

@Data
public class Employee {
    int employeeId;
    String name;
    int age;
    Date startDate;
    Date endDate;
    String password;



    
}
