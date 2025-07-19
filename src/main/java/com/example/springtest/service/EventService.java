package com.example.springtest.service;

import com.example.springtest.entity.Event;
import com.example.springtest.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;
    
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    
    public List<Event> getUpcomingEvents() {
        return eventRepository.findByEventDateAfterOrderByEventDateAsc(LocalDateTime.now());
    }
    
    public List<Event> getAvailableEvents() {
        return eventRepository.findAvailableEvents(LocalDateTime.now());
    }
    
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }
    
    public List<Event> searchEvents(String keyword) {
        return eventRepository.findByKeyword(keyword);
    }
    
    public Event createEvent(String title, String description, LocalDateTime eventDate, Integer capacity) {
        Event event = new Event(title, description, eventDate, capacity);
        return eventRepository.save(event);
    }
    
    public Event updateEvent(Long id, String title, String description, LocalDateTime eventDate, Integer capacity) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("イベントが見つかりません: " + id));
        
        event.setTitle(title);
        event.setDescription(description);
        event.setEventDate(eventDate);
        event.setCapacity(capacity);
        
        return eventRepository.save(event);
    }
    
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public boolean hasAvailableSpots(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("イベントが見つかりません: " + eventId));
        return event.hasAvailableSpots();
    }
}