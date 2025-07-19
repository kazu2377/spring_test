package com.example.springtest.service;

import com.example.springtest.entity.User;
import com.example.springtest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User createUser(String name, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("このメールアドレスは既に使用されています: " + email);
        }
        
        User user = new User(name, email);
        return userRepository.save(user);
    }
    
    public User updateUser(Long id, String name, String email) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません: " + id));
        
        if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new RuntimeException("このメールアドレスは既に使用されています: " + email);
        }
        
        user.setName(name);
        user.setEmail(email);
        
        return userRepository.save(user);
    }
    
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    public User findOrCreateUser(String name, String email) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> createUser(name, email));
    }
}