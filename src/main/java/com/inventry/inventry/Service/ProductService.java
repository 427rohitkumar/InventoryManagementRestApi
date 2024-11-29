package com.inventry.inventry.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventry.inventry.Model.Products;
import com.inventry.inventry.repository.ProductRepo;

@Service
public class ProductService {
    @Autowired
     private ProductRepo productRepo;

    public List<Products> getAllProducts(){
        return productRepo.findAll();
    }

    public Products getProductById(long pCode) {
        Optional<Products> productOptional = productRepo.findById(pCode);
        return productOptional.orElse(null); // Returns the product if present, otherwise returns null
    }

    public Products saveProducts(Products products){
        return productRepo.save(products);
    }


    public Products updateProducts(Products products){
        return productRepo.save(products);
    }

   

}
