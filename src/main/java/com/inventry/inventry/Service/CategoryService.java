package com.inventry.inventry.Service;

import java.util.List;
import java.util.Optional;
import com.inventry.inventry.Model.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventry.inventry.repository.CategoryRepo;

@Service
public class CategoryService {
    @Autowired
     private CategoryRepo categoryRepo;

    public List<Category> getAllCategory(){
        return categoryRepo.findAll();
    }

    public Category getProductById(int catId) {
        Optional<Category> categoryOptional = categoryRepo.findById(catId);
        return categoryOptional.orElse(null); // Returns the product if present, otherwise returns null
    }

    public Category saveProducts(Category cat){
        return categoryRepo.save(cat);
    }


    public Category updateProducts(Category cat){
        return categoryRepo.save(cat);
    }

}
