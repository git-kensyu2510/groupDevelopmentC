package com.example.group_development_c.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class SessionController {
    @ModelAttribute
    public void setSession(HttpSession session,Model m){
        String employeeName = (String) session.getAttribute("employeeName");
        m.addAttribute("employeeName",employeeName);
    }
}
