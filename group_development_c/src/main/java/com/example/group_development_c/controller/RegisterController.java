package com.example.group_development_c.controller;

import java.time.LocalDate;

import com.example.group_development_c.entity.Employee;
import com.example.group_development_c.service.RegisterService;
import com.example.group_development_c.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    @Autowired
    private RegisterService service;
    @Autowired
    private SearchService searchService;

    // 入力画面表示
    //localhost:8080/insert
    @GetMapping("/insert")
public String insert(
        @RequestParam(value = "name", required = false, defaultValue = "") String name,
        @RequestParam(value = "age", required = false, defaultValue = "") String age,
        @RequestParam(value = "password", required = false, defaultValue = "") String password,
        @RequestParam(value = "passwordConfirm", required = false, defaultValue = "") String passwordConfirm,
        //メニュー画面や登録画面など、元いた画面に戻る(まだ作られてないので仮)
        @RequestParam(value = "from", required = false, defaultValue = "menu") String from,
        Model model
) {
    model.addAttribute("name", name);
    model.addAttribute("age", age);
    model.addAttribute("password", password);
    model.addAttribute("passwordConfirm", passwordConfirm);
    //メニュー画面や登録画面など、元いた画面に戻る(まだ作られてないので仮)
    model.addAttribute("from", from); 
    return "insert";
}

@PostMapping("/insert")
public String backPost(
    @RequestParam String name,
    @RequestParam String age,
    @RequestParam String password,
    @RequestParam String passwordConfirm,
    Model model
) {
    model.addAttribute("name", name);
    model.addAttribute("age", age);
    model.addAttribute("password", password);
    model.addAttribute("passwordConfirm", passwordConfirm);
    return "insert";
}

    
    // 入力確認
    @PostMapping("/insert/confirm")
    public String confirm(
            @RequestParam("name") String name,
            @RequestParam("age") String age,
            @RequestParam("password") String password,
            @RequestParam("passwordConfirm") String passwordConfirm,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
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

        
        redirectAttributes.addFlashAttribute("name", name);
        redirectAttributes.addFlashAttribute("age", age);
        redirectAttributes.addFlashAttribute("password", password);
        redirectAttributes.addFlashAttribute("passwordConfirm", passwordConfirm);

        return "redirect:/insert/confirm";
    }

    @GetMapping("/insert/confirm")
    public String showConfirm() {
        return "confirm";
    }

    //メニュー画面や登録画面など、元いた画面に戻る(まだ作られてないので仮)
    @GetMapping("/insert/back")
public String backToPrevious(@RequestParam(value = "from", defaultValue = "menu") String from) {
    switch (from) {
        case "search":
            return "redirect:/search";  // 検索画面に戻る(仮)
        case "menu":
        default:
            return "redirect:/menu";    // メニュー画面に戻る(仮)
    }
}

    
    // 登録処理
    @PostMapping("/insert/complete")
    public String newInsert(
            @RequestParam("name") String name,
            @RequestParam("age") String age,
            @RequestParam("password") String password,
            @RequestParam("passwordConfirm") String passwordConfirm,
            RedirectAttributes redirectAttributes
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

        
        redirectAttributes.addFlashAttribute("msg", "社員情報の登録が完了しました。");

        return "redirect:/insert/complete";
    }

    @GetMapping("/insert/complete")
    public String complete(Model model) {
        if (!model.containsAttribute("msg")) {
        model.addAttribute("msg", "社員情報の登録が完了しました。");
    }
        return "complete";
    }


    // メニュー、検索画面がまだないので（仮）
    @GetMapping("/menu")
    public String menu() {
        return "menu";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(value = "employeeId", required = false, defaultValue = "")
            String employeeId,
            Model model
    ) 

    {model.addAttribute("employeeId", employeeId);
        return "search";
    }

    @PostMapping("/search")
    public String searchById(
            @RequestParam("employeeId") String employeeId,
            Model model
    ) {

        String error = "";
        if (employeeId == null || employeeId.trim().isEmpty()) {
            error = "・社員IDは必須です";
        } else if (!employeeId.matches("^[0-9]+$")) {
            error = "・社員IDは半角数字で入力してください";
        } else {
            Integer id = Integer.parseInt(employeeId);
            if (searchService.existsById(id)) {
                return "redirect:/update?employeeId=" + id;
            }
            error = "・指定された社員IDは存在しません";
        }
        model.addAttribute("error", error);
        model.addAttribute("employeeId", employeeId);
        return "search";
    }
}