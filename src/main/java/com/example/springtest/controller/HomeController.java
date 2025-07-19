package com.example.springtest.controller;

import com.example.springtest.entity.Event;
import com.example.springtest.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public String home(Model model) {
        List<Event> upcomingEvents = eventService.getAvailableEvents();
        model.addAttribute("message", "イベント予約システムへようこそ!");
        model.addAttribute("title", "イベント予約システム");
        model.addAttribute("events", upcomingEvents);
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("message", "これはSpring Bootで作成されたイベント予約システムです。");
        model.addAttribute("title", "システムについて");
        return "about";
    }
}