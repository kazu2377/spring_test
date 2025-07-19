package com.example.springtest.controller;

import com.example.springtest.entity.Booking;
import com.example.springtest.entity.Event;
import com.example.springtest.entity.User;
import com.example.springtest.service.BookingService;
import com.example.springtest.service.EventService;
import com.example.springtest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/user/{userId}")
    public String listUserBookings(@PathVariable Long userId, Model model) {
        List<Booking> bookings = bookingService.getUserBookings(userId);
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        
        model.addAttribute("bookings", bookings);
        model.addAttribute("user", user);
        return "bookings/user-list";
    }
    
    @GetMapping("/event/{eventId}")
    public String listEventBookings(@PathVariable Long eventId, Model model) {
        List<Booking> bookings = bookingService.getEventBookings(eventId);
        Event event = eventService.getEventById(eventId)
                .orElseThrow(() -> new RuntimeException("イベントが見つかりません"));
        
        model.addAttribute("bookings", bookings);
        model.addAttribute("event", event);
        return "bookings/event-list";
    }
    
    @GetMapping("/new")
    public String showBookingForm(
            @RequestParam Long eventId,
            @RequestParam(required = false) Long userId,
            Model model) {
        
        Event event = eventService.getEventById(eventId)
                .orElseThrow(() -> new RuntimeException("イベントが見つかりません"));
        
        model.addAttribute("event", event);
        if (userId != null) {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
            model.addAttribute("user", user);
        }
        
        return "bookings/form";
    }
    
    @PostMapping
    public String createBooking(
            @RequestParam Long eventId,
            @RequestParam Long userId,
            RedirectAttributes redirectAttributes) {
        
        try {
            Booking booking = bookingService.createBooking(userId, eventId);
            redirectAttributes.addFlashAttribute("successMessage", "予約が正常に作成されました");
            return "redirect:/bookings/user/" + userId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "予約の作成に失敗しました: " + e.getMessage());
            return "redirect:/bookings/new?eventId=" + eventId + "&userId=" + userId;
        }
    }
    
    @PostMapping("/{bookingId}/cancel")
    public String cancelBooking(
            @PathVariable Long bookingId,
            @RequestParam Long userId,
            RedirectAttributes redirectAttributes) {
        
        try {
            bookingService.cancelBooking(bookingId);
            redirectAttributes.addFlashAttribute("successMessage", "予約が正常にキャンセルされました");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "予約のキャンセルに失敗しました: " + e.getMessage());
        }
        
        return "redirect:/bookings/user/" + userId;
    }
    
    @GetMapping("/quick-book")
    public String showQuickBookingForm(
            @RequestParam Long eventId,
            Model model) {
        
        Event event = eventService.getEventById(eventId)
                .orElseThrow(() -> new RuntimeException("イベントが見つかりません"));
        
        model.addAttribute("event", event);
        return "bookings/quick-form";
    }
    
    @PostMapping("/quick-book")
    public String createQuickBooking(
            @RequestParam Long eventId,
            @RequestParam String userName,
            @RequestParam String userEmail,
            RedirectAttributes redirectAttributes) {
        
        try {
            User user = userService.findOrCreateUser(userName, userEmail);
            Booking booking = bookingService.createBooking(user.getId(), eventId);
            redirectAttributes.addFlashAttribute("successMessage", "予約が正常に作成されました");
            return "redirect:/events/" + eventId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "予約の作成に失敗しました: " + e.getMessage());
            return "redirect:/bookings/quick-book?eventId=" + eventId;
        }
    }
}