package com.inventry.inventry.repository;

import com.inventry.inventry.Model.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Integer>{
    
}
