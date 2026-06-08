package com.example.group_development_c.controller;

// 確認用URL
// http://localhost:8080/update?employeeId=10006
// http://localhost:8080/search

import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.group_development_c.entity.Employee;
import com.example.group_development_c.service.UpdateService;

@Controller
public class UpdateController {

    @Autowired
    private UpdateService service;

    @GetMapping("/update")
    public String updateInput(
            @RequestParam(value = "employeeId", required = false) Integer employeeId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "age", required = false) String age,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "passwordConfirm", required = false) String passwordConfirm,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            Model model) {

        if (employeeId == null) {
            model.addAttribute("error", "社員IDが指定されていません。");
            return "update";
        }

        if (name != null || age != null || password != null || passwordConfirm != null || startDate != null || endDate != null) {
            model.addAttribute("employeeId", employeeId);
            model.addAttribute("name", name);
            model.addAttribute("age", age);
            model.addAttribute("password", password);
            model.addAttribute("passwordConfirm", passwordConfirm);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            return "update";
        }

        Employee employee = service.findById(employeeId);

        if (employee == null) {
            model.addAttribute("error", "指定された社員情報が見つかりません。");
            return "update";
        }

        model.addAttribute("employeeId", employee.getEmployeeId());
        model.addAttribute("name", employee.getName());
        model.addAttribute("age", employee.getAge());
        model.addAttribute("password", employee.getPassword());
        model.addAttribute("passwordConfirm", employee.getPassword());
        model.addAttribute("startDate", employee.getStartDate() != null ? employee.getStartDate().toString() : "");
        model.addAttribute("endDate", employee.getEndDate() != null ? employee.getEndDate().toString() : "");

        return "update";
    }

    @PostMapping("/update")
    public String backToInput(
            @RequestParam("employeeId") Integer employeeId,
            @RequestParam("name") String name,
            @RequestParam("age") String age,
            @RequestParam("password") String password,
            @RequestParam("passwordConfirm") String passwordConfirm,
            @RequestParam("startDate") String startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "") String endDate,
            Model model) {

        model.addAttribute("employeeId", employeeId);
        model.addAttribute("name", name);
        model.addAttribute("age", age);
        model.addAttribute("password", password);
        model.addAttribute("passwordConfirm", passwordConfirm);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "update";
    }

    @PostMapping("/update/confirm")
    public String confirm(
            @RequestParam("employeeId") Integer employeeId,
            @RequestParam("name") String name,
            @RequestParam("age") String age,
            @RequestParam("password") String password,
            @RequestParam("passwordConfirm") String passwordConfirm,
            @RequestParam("startDate") String startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "") String endDate,
            Model model,
            RedirectAttributes redirectAttributes) {

        String error = "";

        if (name == null || name.trim().isEmpty()) {
            error += "・社員名は必須です\n";
        }

        if (age == null || !age.matches("^[0-9]+$")) {
            error += "・年齢は半角数字で入力してください\n";
        }

        if (password == null || !password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])[A-Za-z0-9]{8,}$")) {
            error += "・パスワードは半角英数字、大文字を含む8文字以上で入力してください\n";
        }

        if (passwordConfirm == null || !password.equals(passwordConfirm)) {
            error += "・パスワードと確認が一致しません\n";
        }

        if (startDate == null || !startDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            error += "・開始日は yyyy-MM-dd 形式で入力してください\n";
        }

        if (!endDate.isEmpty() && !endDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            error += "・終了日は yyyy-MM-dd 形式で入力してください\n";
        }

        if (!error.isEmpty()) {
            model.addAttribute("error", error);
            model.addAttribute("employeeId", employeeId);
            model.addAttribute("name", name);
            model.addAttribute("age", age);
            model.addAttribute("password", password);
            model.addAttribute("passwordConfirm", passwordConfirm);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            return "update";
        }

        redirectAttributes.addFlashAttribute("employeeId", employeeId);
        redirectAttributes.addFlashAttribute("name", name);
        redirectAttributes.addFlashAttribute("age", age);
        redirectAttributes.addFlashAttribute("password", password);
        redirectAttributes.addFlashAttribute("passwordConfirm", passwordConfirm);
        redirectAttributes.addFlashAttribute("startDate", startDate);
        redirectAttributes.addFlashAttribute("endDate", endDate);

        return "redirect:/update/confirm";
    }

    @GetMapping("/update/confirm")
    public String showConfirm(Model model) {
        if (!model.containsAttribute("employeeId")) {
            return "redirect:/search";
        }
        return "update_confirm";
    }

    @PostMapping("/update/complete")
    public String complete(
            @RequestParam("employeeId") Integer employeeId,
            @RequestParam("name") String name,
            @RequestParam("age") String age,
            @RequestParam("password") String password,
            @RequestParam("passwordConfirm") String passwordConfirm,
            @RequestParam("startDate") String startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "") String endDate,
            RedirectAttributes redirectAttributes) {

        if (!password.equals(passwordConfirm)) {
            redirectAttributes.addAttribute("employeeId", employeeId);
            return "redirect:/update";
        }

        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setName(name);
        employee.setAge(Integer.parseInt(age));
        employee.setPassword(password);
        employee.setStartDate(Date.valueOf(startDate));
        employee.setEndDate(endDate.isEmpty() ? null : Date.valueOf(endDate));

        service.update(employee);

        redirectAttributes.addFlashAttribute("msg", "社員情報の更新が完了しました。");

        return "redirect:/update/complete";
    }

    @GetMapping("/update/complete")
    public String showComplete(Model model) {
        if (!model.containsAttribute("msg")) {
            model.addAttribute("msg", "社員情報の更新が完了しました。");
        }
        return "update_complete";
    }
}

