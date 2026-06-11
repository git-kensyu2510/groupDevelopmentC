package com.example.group_development_c.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.group_development_c.entity.Employee;
import com.example.group_development_c.service.DeleteService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DeleteController {
    @Autowired
    DeleteService service;

    /*ID検索画面 */
    @GetMapping("/searchId")
    public String SearchAll(
        HttpSession session,
        Model m){
        String employeeName = (String) session.getAttribute("employeeName");
        m.addAttribute("employeeName",employeeName);
        return "searchId";
    }

    @GetMapping("/searchId2")
    public String SearchAll2(){
        return "searchId2";
    }

     /* 複数検索（仮） */
    @GetMapping("/searchId2/result")
    public String SearchAll2(
        @RequestParam(name = "selectIds", required = false) List<String> selectIds,
        Model m){
        List<Employee> employee;

        /*空欄の場合全件表示 */
        if(selectIds == null || selectIds.isEmpty()){
            employee = service.selectAll();
        }else{ //単一検索 
            int empId = Integer.parseInt(selectIds.get(0));
            employee = service.selectById(empId);
        }
        m.addAttribute("employee",employee);
        return "searchId2";
    }

    /*削除確認画面 IDが存在しない場合はエラー */
    @PostMapping("/delete")
    public String receiveId(
        @RequestParam("from") String from,
        @RequestParam(name = "selectIds", required = false) List<String> selectIds,
        Model m){
        /* IDの入力がなければエラー*/
        if(selectIds == null || selectIds.isEmpty()){
            m.addAttribute("searchErrorNull","IDを入力してください。");
            return "searchId";
        }

        List<Integer> empIds = new ArrayList<>();
        
        /* IDの入力が数字でなければエラー*/
        try{
            for(String id : selectIds){
                empIds.add(Integer.parseInt(id));
            }
        }catch (NumberFormatException e){
            m.addAttribute("searchError", "IDは数字で入力してください");
            return "searchId";
        }
        
        /* 検索成功 */
        List<Employee> empList = new ArrayList<>();
        for(int id : empIds){
            List<Employee> employees = service.selectById(id);
            if(employees.isEmpty()){ // 該当のIDが存在しない
                m.addAttribute("searchError", "該当のIDが存在しません");
                return "searchId";
            }
            empList.addAll((employees));
        }
        m.addAttribute("employee",empList);
        m.addAttribute("from", from);
        return "delete";
    }
    

    /*削除処理(物理削除) 自身のデータを削除する場合はエラー */
    @PostMapping("/deleted")
    public String deleted(
        @RequestParam(name = "selectIds", required = false) List<String> selectIds,
        HttpSession session,
        Model m){
        /*ログイン中のID */
        Employee loginEmployee = (Employee) session.getAttribute("employee");
        Integer loginId = null;
        if(loginEmployee != null){
            loginId = loginEmployee.getEmployeeId();
        }

        List<Employee> employee = service.selectAll();
        
        /*ID未選択 */
        if(selectIds == null || selectIds.isEmpty()){
            m.addAttribute("employee", employee);
            m.addAttribute("search2ErrorNull","IDを選択してください。");
            return "searchId2";
        }

        List<Integer> empIds = new ArrayList<>();
        try{
            for(String id : selectIds){
                empIds.add(Integer.parseInt(id));
            }
        }catch (NumberFormatException e){
            m.addAttribute("deleteError", "IDは数字で入力してください");
            return "delete";
        }

        List<Employee> empList = new ArrayList<>();
        for(int id : empIds){
            empList.addAll(service.selectById(id));
        }

        /*自身のデータを含む場合のエラー */
        if(loginId != null && empIds.contains(loginId)){
            m.addAttribute("employee", empList);
            m.addAttribute("deleteError", "削除できない情報があります");
            return "delete";
        }

        service.deleteIds(empIds);

        return "deleted";
    }

    @PostMapping("/back")
    public String back(@RequestParam("from") String from){
        if(from.equals("multi")){
            return "redirect:/searchId2";
        }
        if(from.equals("single")){
            return "redirect:/searchId";
        }
        return "redirect:/searchId";
}
}
