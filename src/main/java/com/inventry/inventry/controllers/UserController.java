package com.inventry.inventry.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inventry.inventry.Model.User;
import com.inventry.inventry.Service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
    
    @Autowired
    private UserService userService;

    // =========== getting all users data =================
    @GetMapping("/users")
    public List<User> getAllUser(){
        return userService.getAllUser();
    }

//    ================ getting single users data by id =====================
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") int userId){
        Optional<User> optionalUser=userService.getUserById(userId);
        if(optionalUser.isPresent()){
            return new ResponseEntity<>(optionalUser, HttpStatus.OK);  
        }
        return new ResponseEntity<>("User_NOT_FOUND", HttpStatus.NOT_FOUND);

    }

    // ============= Saving user Data ==============
    @PostMapping("/users/register")
    public ResponseEntity<Object> addUser(@RequestParam(value = "file", required = false)MultipartFile file,
                        @RequestParam("name")String name,
                        @RequestParam("email")String email,
                        @RequestParam("password")String password,
                        @RequestParam("role")String role){
         
        // Check for missing fields and respond immediately
        if (name == null || name.isEmpty()) {
            return new ResponseEntity<>("Name is required.", HttpStatus.BAD_REQUEST);
        }
        if (email == null || email.isEmpty()) {
            return new ResponseEntity<>("Email is required.", HttpStatus.BAD_REQUEST);
        }
        if (password == null || password.isEmpty()) {
            return new ResponseEntity<>("Password is required.", HttpStatus.BAD_REQUEST);
        }
        if (role == null || role.isEmpty()) {
            return new ResponseEntity<>("Role is required.", HttpStatus.BAD_REQUEST);
        }

        try {
            User user=new User();
            user.setUserName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(role);
            if(file==null|| file.isEmpty()){
                user.setProfileImage("defualtUserProfile.png");
                System.out.println("================FIle is Null or Empity===================");
            }else{
                String fileName=saveFile(file);
                user.setProfileImage(fileName);
            }
            
            
            User saveUser= userService.saveUsers(user);
            return new ResponseEntity<>(saveUser, HttpStatus.CREATED); // Success, 201 Created
    
        } catch (Exception e) {
            return new ResponseEntity<>("Error registering user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    // ===========  method for file handling or Creating dynamic custome file name ==============
    private String saveFile(MultipartFile file) {
        try {
            // Define the directory where the file will be stored
            String uploadDir = "static/images";
            
            // Create the directory if it does not exist
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate a unique file name
            String originalFileName = file.getOriginalFilename();
            String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;

            // Create the file path
            Path filePath = Paths.get(uploadDir, uniqueFileName);

            // Save the file to the directory
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return the file name for storing in the database
            return uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save file: " + e.getMessage());
        }
}

    // ============== Update user by Id ================
    @PutMapping("/users/update")
    public ResponseEntity<Object> updateUser(@RequestParam(value = "file", required = false)MultipartFile file,
                                            @RequestParam("userId")int userId,
                                            @RequestParam("name")String name,
                                            @RequestParam("email")String email,
                                            @RequestParam("role")String role){


         // Check for missing fields and respond immediately
         if (name == null || name.isEmpty()) {
            return new ResponseEntity<>("Name is required.", HttpStatus.BAD_REQUEST);
        }
        if (email == null || email.isEmpty()) {
            return new ResponseEntity<>("Email is required.", HttpStatus.BAD_REQUEST);
        }
        if (role == null || role.isEmpty()) {
            return new ResponseEntity<>("Password is required.", HttpStatus.BAD_REQUEST);
        }
      //Find user By id 
       Optional<User> optionalUser=userService.getUserById(userId);
       if(!optionalUser.isPresent()){
          return new ResponseEntity<>("User_NOT_FOUND",HttpStatus.NOT_FOUND);
       }

      try {
            User user=optionalUser.get();
            user.setUserName(name);
            user.setEmail(email);
            user.setRole(role);
    
            // handling File upload for profile image
    
            if(file ==null || file.isEmpty()){
                user.setProfileImage(user.getProfileImage());
    
            }else{
            String fileName=saveFile(file);
            user.setProfileImage(fileName);
            }
    
            // udate the user
            boolean updated=userService.updateUsers(user, userId);
            if(updated){
                return new ResponseEntity<>(user,HttpStatus.OK);
    
            }else{
                return new ResponseEntity<>("Update_Failed",HttpStatus.INTERNAL_SERVER_ERROR);
            }
      } catch (Exception e) {
        return new ResponseEntity<>("Error registering user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    
    //   =============== deleting the use data by user Id ==================
    @DeleteMapping("/users/delete/{userId}")
    public ResponseEntity<?> deleteUsers(@PathVariable("userId") int userId) {
            // Get the user by ID to retrieve the profile image path
            Optional<User> optionalUser = userService.getUserById(userId);
            
            if (!optionalUser.isPresent()) {
                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
            }

            User user=optionalUser.get();
            // Get the profile image path
            String profileImageName = user.getProfileImage(); // Profile image file name or path stored in the DB

            // Delete the user record from the database
            boolean isDeleted = userService.deleteUsers(userId);
            if (isDeleted) {
                // Check if the profile image is not the default one before attempting to delete
                if (profileImageName != null && !profileImageName.isEmpty() && !profileImageName.equals("defualtUserProfile.png")) {
                    // Define the directory where the profile images are stored
                    String uploadDir = "static/images";  // Make sure this path is consistent with where your images are stored
                    
                    // Create the file object to represent the image file
                    File imageFile = new File(uploadDir + File.separator + profileImageName);
                    
                    // Check if the file exists and delete it
                    if (imageFile.exists() && imageFile.isFile()) {
                        boolean imageDeleted = imageFile.delete();
                        if (!imageDeleted) {
                            System.err.println("Failed to delete profile image: " + imageFile.getAbsolutePath());
                        }
                    }
                }

                return new ResponseEntity<>("User deleted successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to delete user.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


    @GetMapping("/users/search") 
    public ResponseEntity<List<User>> searchUsers(@RequestParam(value = "query") String query) { 
        List<User> searchResults = userService.getSearchResults(query); 
        if (searchResults.isEmpty()) { 
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
        } else { 
            return new ResponseEntity<>(searchResults, HttpStatus.OK); 
        } 
    }


}