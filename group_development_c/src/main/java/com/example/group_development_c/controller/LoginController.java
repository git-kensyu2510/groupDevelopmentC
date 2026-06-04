package com.example.group_development_c.controller;

import com.example.group_development_c.service.LoginService;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.group_development_c.entity.Employee;

@Controller
public class LoginController {
    @Autowired
    LoginService service;

    /*ログイン画面 */
    @GetMapping("/login")
    public String login(){
        return "login";
    } 

    /*ログイン情報処理*/
    @PostMapping("/menu")
    public String menu(
        @RequestParam("id")String id,
        @RequestParam("pass")String pass,
        HttpSession session,
        Model m
    ){
        /* 空白チェック */
        if(id.isBlank() || pass.isBlank()){
            m.addAttribute("loginErrorNull","ID、パスワードを入力してください。");
            return "login";
        }
        /* String → int 
        * IDの入力が数字でなければエラー
        */
        int empId;
        try {
            empId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            m.addAttribute("loginError", "IDは数字で入力してください。");
            return "login";
        }

        Employee employee = service.selectByLogin(empId, pass);
        /* ログイン成功 */
        if(employee != null){
            session.setAttribute("employee",employee);
            session.setAttribute("loginTime",LocalDateTime.now());
            return "menu";
        }
        /* ID,Passが不一致 */
        m.addAttribute("loginError","IDもしくはパスワードが間違っています。");
        return "login";
    }

/*  各コントローラーに記載するとlayoutが適応される。"page"部分を変更する。
    @GetMapping("/page")
    public String setSession(HttpSession session,Model m){
        String employeeName = (String) session.getAttribute("employeeName");
        m.addAttribute("employeeName",employeeName);
        return "page";
    }

*/

}
