package com.inventry.inventry.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.inventry.inventry.Model.User;
import com.inventry.inventry.repository.UserRepo;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

     public List<User> getAllUser(){
        return userRepo.findAll();
    }

    public User getUserById(int userId) {
        Optional<User> categoryOptional = userRepo.findById(userId);
        return categoryOptional.orElse(null); // Returns the product if present, otherwise returns null
    }

    public User saveUsers(User user){
        return userRepo.save(user);
    }


    public User updateUsers(User user){
        return userRepo.save(user);
    }
}
