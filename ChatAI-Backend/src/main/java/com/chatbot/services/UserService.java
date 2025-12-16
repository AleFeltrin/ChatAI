package com.chatbot.services;

import com.chatbot.entities.User;
import com.chatbot.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User insertUser(User user) {
        return userRepository.save(user);
    }

    public List<User> insertUsers(List<User> users) {
        return userRepository.saveAll(users);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));
    }
}