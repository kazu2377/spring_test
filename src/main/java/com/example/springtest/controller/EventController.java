package com.example.springtest.controller;

import com.example.springtest.entity.Event;
import com.example.springtest.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {
    
    @Autowired
    private EventService eventService;
    
    @GetMapping
    public String listEvents(Model model) {
        List<Event> events = eventService.getUpcomingEvents();
        model.addAttribute("events", events);
        return "events/list";
    }
    
    @GetMapping("/available")
    public String listAvailableEvents(Model model) {
        List<Event> events = eventService.getAvailableEvents();
        model.addAttribute("events", events);
        return "events/available";
    }
    
    @GetMapping("/{id}")
    public String viewEvent(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id)
                .orElseThrow(() -> new RuntimeException("イベントが見つかりません"));
        model.addAttribute("event", event);
        return "events/detail";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new Event());
        return "events/form";
    }
    
    @PostMapping
    public String createEvent(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime eventDate,
            @RequestParam Integer capacity,
            RedirectAttributes redirectAttributes) {
        
        try {
            Event event = eventService.createEvent(title, description, eventDate, capacity);
            redirectAttributes.addFlashAttribute("successMessage", "イベントが正常に作成されました");
            return "redirect:/events/" + event.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "イベントの作成に失敗しました: " + e.getMessage());
            return "redirect:/events/new";
        }
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id)
                .orElseThrow(() -> new RuntimeException("イベントが見つかりません"));
        model.addAttribute("event", event);
        return "events/edit";
    }
    
    @PostMapping("/{id}")
    public String updateEvent(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime eventDate,
            @RequestParam Integer capacity,
            RedirectAttributes redirectAttributes) {
        
        try {
            eventService.updateEvent(id, title, description, eventDate, capacity);
            redirectAttributes.addFlashAttribute("successMessage", "イベントが正常に更新されました");
            return "redirect:/events/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "イベントの更新に失敗しました: " + e.getMessage());
            return "redirect:/events/" + id + "/edit";
        }
    }
    
    @PostMapping("/{id}/delete")
    public String deleteEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            eventService.deleteEvent(id);
            redirectAttributes.addFlashAttribute("successMessage", "イベントが正常に削除されました");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "イベントの削除に失敗しました: " + e.getMessage());
        }
        return "redirect:/events";
    }
    
    @GetMapping("/search")
    public String searchEvents(@RequestParam(required = false) String keyword, Model model) {
        List<Event> events;
        if (keyword != null && !keyword.trim().isEmpty()) {
            events = eventService.searchEvents(keyword);
        } else {
            events = eventService.getUpcomingEvents();
        }
        model.addAttribute("events", events);
        model.addAttribute("keyword", keyword);
        return "events/search";
    }
}