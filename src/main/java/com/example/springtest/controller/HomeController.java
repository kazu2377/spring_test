package com.example.springtest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Hello Spring Boot!");
        model.addAttribute("title", "Spring Test Application");
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("message", "これは簡単なSpring Boot Webアプリケーションです。");
        model.addAttribute("title", "About Page");
        return "about";
    }
}