package com.example.group_development_c.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.group_development_c.entity.Employee;
import com.example.group_development_c.mapper.LoginMapper;

@Service
public class LoginService {
@Autowired
LoginMapper mapper;
public Employee selectByLogin(int id,String pass){
    return mapper.selectByLogin(id, pass);
}

}
