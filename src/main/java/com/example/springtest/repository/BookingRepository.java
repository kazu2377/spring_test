package com.example.springtest.repository;

import com.example.springtest.entity.Booking;
import com.example.springtest.entity.Event;
import com.example.springtest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByUserOrderByBookedAtDesc(User user);
    
    List<Booking> findByEventOrderByBookedAtDesc(Event event);
    
    Optional<Booking> findByUserAndEventAndStatus(User user, Event event, Booking.BookingStatus status);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.event = :event AND b.status = 'CONFIRMED'")
    long countConfirmedBookingsByEvent(Event event);
    
    boolean existsByUserAndEventAndStatus(User user, Event event, Booking.BookingStatus status);
}