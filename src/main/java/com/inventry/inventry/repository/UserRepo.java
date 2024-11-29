package com.inventry.inventry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventry.inventry.Model.User;

public interface UserRepo extends JpaRepository<User,Integer> {
    
}
