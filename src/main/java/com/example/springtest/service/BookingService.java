package com.example.springtest.service;

import com.example.springtest.entity.Booking;
import com.example.springtest.entity.Event;
import com.example.springtest.entity.User;
import com.example.springtest.repository.BookingRepository;
import com.example.springtest.repository.EventRepository;
import com.example.springtest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<Booking> getUserBookings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません: " + userId));
        return bookingRepository.findByUserOrderByBookedAtDesc(user);
    }
    
    public List<Booking> getEventBookings(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("イベントが見つかりません: " + eventId));
        return bookingRepository.findByEventOrderByBookedAtDesc(event);
    }
    
    public Booking createBooking(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません: " + userId));
        
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("イベントが見つかりません: " + eventId));
        
        if (bookingRepository.existsByUserAndEventAndStatus(user, event, Booking.BookingStatus.CONFIRMED)) {
            throw new RuntimeException("既にこのイベントに予約済みです");
        }
        
        synchronized (this) {
            event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("イベントが見つかりません: " + eventId));
            
            if (!event.hasAvailableSpots()) {
                throw new RuntimeException("このイベントは満席です");
            }
            
            event.decrementAvailableSpots();
            eventRepository.save(event);
            
            Booking booking = new Booking(user, event);
            return bookingRepository.save(booking);
        }
    }
    
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("予約が見つかりません: " + bookingId));
        
        if (booking.isCancelled()) {
            throw new RuntimeException("この予約は既にキャンセル済みです");
        }
        
        booking.cancel();
        bookingRepository.save(booking);
        
        Event event = booking.getEvent();
        event.incrementAvailableSpots();
        eventRepository.save(event);
    }
    
    @Transactional(readOnly = true)
    public boolean hasUserBookedEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません: " + userId));
        
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("イベントが見つかりません: " + eventId));
        
        return bookingRepository.existsByUserAndEventAndStatus(user, event, Booking.BookingStatus.CONFIRMED);
    }
    
    @Transactional(readOnly = true)
    public Optional<Booking> getUserEventBooking(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません: " + userId));
        
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("イベントが見つかりません: " + eventId));
        
        return bookingRepository.findByUserAndEventAndStatus(user, event, Booking.BookingStatus.CONFIRMED);
    }
}