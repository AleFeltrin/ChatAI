package com.chatbot.controllers;

import com.chatbot.entities.User;
import com.chatbot.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    
    @GetMapping()
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping()
    public User addNewUser(@RequestBody User user) {
        return userService.insertUser(user);
    }

    @PostMapping("/listofusers")
    public List<User> addList(@RequestBody List<User> users) {
        return userService.insertUsers(users);
    }
}