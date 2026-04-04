package com.example.group_development_c.controller;


import java.time.LocalDate;
import com.example.group_development_c.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.group_development_c.service.RegisterService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;



@Controller
public class RegisterController {
    
@Autowired
    private RegisterService service;

    @Autowired
    private HttpSession session; 


    //登録localhost:8080/insert     
    @GetMapping("/insert")
    public String insert(Model model) {
        model.addAttribute ("name", session.getAttribute("name")) ;
        model.addAttribute ("age", session.getAttribute("age")) ;
        model.addAttribute ("password", session.getAttribute("password"));
        model.addAttribute("passwordConfirm", session.getAttribute("passwordConfirm")) ;
    return "insert"; 
    


        }

        @GetMapping("/insert/back")
public String backToInsert(HttpServletRequest request) {
    String referer = request.getHeader("Referer");


    if (referer != null && !referer.isEmpty()) {
        
        return "redirect:" + referer;
    } 
    // 現状前の画面は作成されていない
    return null; 
}




    
    @PostMapping("/insert/confirm")
public String confirm(
        @RequestParam ("name") String name,
        @RequestParam ("age") String age,
        @RequestParam ("password") String password,
        @RequestParam("passwordConfirm") String passwordConfirm,
        Model model) {

        
        session.setAttribute("name", name);
        session.setAttribute("age", age);
        session.setAttribute("password", password);
        session.setAttribute("passwordConfirm", passwordConfirm);

        String error = "";

    if (name == null || name.trim().isEmpty()) {
        error += "・社員名は必須です\n";
    }

    if (age == null || !age.matches("^[0-9]+$")) {
        error += "・年齢は半角数字で入力してください\n";
    }

    if (!password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])[A-Za-z0-9]{8,}$")) {
        error += "・パスワードは半角英数字、大文字を含む8文字以上で入力してください\n";
    }

    if (!password.equals(passwordConfirm)) {
        error += "・パスワードと確認が一致しません\n";
    }

    if (!error.isEmpty()) {
        model.addAttribute("error", error);
        model.addAttribute("name", name);
        model.addAttribute("age", age);
        model.addAttribute("password", password);
        model.addAttribute("passwordConfirm", passwordConfirm);
        return "insert";
    }
        model.addAttribute("name", name);
        model.addAttribute("age", age);
        model.addAttribute("password", password);
        model.addAttribute("passwordConfirm", passwordConfirm);

    return "confirm";
}
    





    //登録完了通知画面


@PostMapping("/insert/complete")
public String newInsert(
        @RequestParam("name") String name,
        @RequestParam("age") String age,
        @RequestParam("password") String password,
        @RequestParam("passwordConfirm") String passwordConfirm,
        Model model
) {
        if (!password.equals(passwordConfirm)) {
        return "redirect:/insert"; 
    }
    
    

    int newAge = Integer.parseInt(age);

    java.sql.Date startDate = java.sql.Date.valueOf(LocalDate.now());
    Employee employee = new Employee();
    employee.setEmployeeId(0);
    employee.setName(name);
    employee.setAge(newAge);
    employee.setStartDate(startDate);
    employee.setEndDate(null);
    employee.setPassword(password);

    service.insert(employee);
    model.addAttribute("msg", "社員情報の登録が完了しました。");

    session.removeAttribute("name");
    session.removeAttribute("age");
    session.removeAttribute("password");
    session.removeAttribute("passwordConfirm");
    return "complete";
}

    

    //メニューへ,現状URL不明のため仮
        @GetMapping("/menu")
        public String menu() {
        return "menu";
    }

    //検索画面が作られていないので仮
        @GetMapping("/search")
        public String search() {
        return "search";
    }

    


}
