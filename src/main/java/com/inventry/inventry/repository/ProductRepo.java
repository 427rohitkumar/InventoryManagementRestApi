package com.inventry.inventry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventry.inventry.Model.Products;

public interface ProductRepo extends JpaRepository<Products,Long> {
    
}
