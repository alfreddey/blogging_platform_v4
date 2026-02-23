package com.example.demo.controller;

import com.example.demo.dto.SimpleForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FormController {

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("form", new SimpleForm());
        return "form-page";
    }

    @PostMapping("/form")
    public String submitForm(@ModelAttribute SimpleForm form, RedirectAttributes redirect) {
        redirect.addFlashAttribute("message", "Form received: " + form.getName());
        return "redirect:/form";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        return "profile-page";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

}