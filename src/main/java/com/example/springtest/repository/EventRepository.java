package com.example.springtest.repository;

import com.example.springtest.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    List<Event> findByEventDateAfterOrderByEventDateAsc(LocalDateTime dateTime);
    
    List<Event> findByEventDateBetweenOrderByEventDateAsc(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT e FROM Event e WHERE e.availableSpots > 0 AND e.eventDate > :now ORDER BY e.eventDate ASC")
    List<Event> findAvailableEvents(LocalDateTime now);
    
    @Query("SELECT e FROM Event e WHERE e.title LIKE %:keyword% OR e.description LIKE %:keyword%")
    List<Event> findByKeyword(String keyword);
}