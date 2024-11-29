package com.inventry.inventry.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventry.inventry.Model.User;
import com.inventry.inventry.Service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsero(){
        return userService.getAllUser();
    }

    @PostMapping("/register/user")
    public User addUser(@RequestBody User user){
        return userService.saveUsers(user);
    }
}
