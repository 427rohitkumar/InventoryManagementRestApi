package com.inventry.inventry.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.inventry.inventry.Model.User;
import com.inventry.inventry.repository.UserRepo;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    // Base URL for serving images

    private final String IMAGE_BASE_URL = "http://localhost:8080/images/";

    // ========== getting List of  all User Data ==============
      // Fetch all users Data and adding full path of the user profile image and update profileImage field
    public List<User> getAllUser() {
        return 
            userRepo.findAll()
            .stream()
            .map(user -> {
            if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
                user.setProfileImage(IMAGE_BASE_URL + user.getProfileImage());
            }
            return user;
        }).collect(Collectors.toList());
    }


//    ========== getting user data by ID ============
    public Optional<User> getUserById(int userId) {
         return userRepo.findById(userId);
    }


//    =========== Saving user Data =============
    public User saveUsers(User user){
        return userRepo.save(user);
    }


//    ============= Updating user Data ==============
    public boolean updateUsers(User userObj,int id){
       return userRepo.findById(id).map(existingUser->{
        // Update only Field that should be updated 

            existingUser.setUserName(userObj.getUserName());
            existingUser.setEmail(userObj.getEmail());
            existingUser.setPassword(userObj.getPassword());
            existingUser.setRole(userObj.getRole());
            existingUser.setProfileImage(userObj.getProfileImage());
            existingUser.setAccountActive(userObj.isAccountActive());
            userRepo.save(existingUser);
            return true;
        }).orElse(false);
        
    }

//    =============== deleting user Data =================
    public boolean deleteUsers(int userId) {
        return userRepo.findById(userId)
                       .map(user->{
                          userRepo.deleteById(userId);
                          return true;
                       }).orElse(false);


        // Optional<User> userOptional = userRepo.findById(userId);
    
        // if (userOptional.isPresent()) {
        //     userRepo.deleteById(userId);
        //     return true;
        // }
        // return false; 
    }



    public List<User> getSearchResults(String query) {
         return userRepo.findAll().stream() 
         .filter(user -> user.getUserName().contains(query) || 
                         user.getEmail().contains(query) || 
                         user.getRole().contains(query)) 
          .map(user -> { 
             if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()){ 
                user.setProfileImage(IMAGE_BASE_URL + user.getProfileImage()); 
             } 
             return user; 
            
            })
                         
           .collect(Collectors.toList());
         }

    
}
