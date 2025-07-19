package com.example.springtest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    
    @Column(nullable = false)
    private LocalDateTime bookedAt;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;
    
    public enum BookingStatus {
        CONFIRMED, CANCELLED
    }
    
    public Booking() {
        this.bookedAt = LocalDateTime.now();
        this.status = BookingStatus.CONFIRMED;
    }
    
    public Booking(User user, Event event) {
        this();
        this.user = user;
        this.event = event;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Event getEvent() {
        return event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }
    
    public LocalDateTime getBookedAt() {
        return bookedAt;
    }
    
    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }
    
    public BookingStatus getStatus() {
        return status;
    }
    
    public void setStatus(BookingStatus status) {
        this.status = status;
    }
    
    public void cancel() {
        this.status = BookingStatus.CANCELLED;
    }
    
    public boolean isConfirmed() {
        return status == BookingStatus.CONFIRMED;
    }
    
    public boolean isCancelled() {
        return status == BookingStatus.CANCELLED;
    }
}