package com.inventry.inventry.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.inventry.inventry.Model.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventry.inventry.repository.CategoryRepo;

@Service
public class CategoryService {
    @Autowired
     private CategoryRepo categoryRepo;

     public final String IMAGE_BASE_URL="http://localhost:8080/categoryImages/";

    // ========== getting all Category list ===============
    public List<Category> getAllCategory(){
        return categoryRepo.findAll()
                            .stream()
                            .map(cate->{
                                if(cate.getCatImage() !=null && !cate.getCatImage().isEmpty()){
                                    cate.setCatImage(IMAGE_BASE_URL+cate.getCatImage());
                                } 
                                 return cate;                               
                            }).collect(Collectors.toList());

    }

    public Optional<Category> getCateById(int catId) {
        return  categoryRepo.findById(catId);
    }

    public Category saveCategory(Category cat){
        return categoryRepo.save(cat);
    }


    public Category updateCategory(Category cat){
        Category category=categoryRepo.save(cat);
        return category;
    }



    //    =============== deleting user Data =================
    public boolean deleteCategory(int catId) {
        return categoryRepo.findById(catId)
                       .map(user->{
                        categoryRepo.deleteById(catId);
                          return true;
                       }).orElse(false);


        // Optional<User> userOptional = userRepo.findById(userId);
    
        // if (userOptional.isPresent()) {
        //     userRepo.deleteById(userId);
        //     return true;
        // }
        // return false; 
    }


    public List<Category> getSearchedCategory(String query){
       return categoryRepo.findAll().stream()
                                    .filter(cat->cat.getCatName().contains(query)||
                                                  String.valueOf(cat.getCatId()).contains(query)||
                                                  cat.getCatDesc().contains(query)||
                                                  String.valueOf(cat.getUpdated_at()).contains(query)
                                    )
                                    .map(cat->{
                                        if(cat.getCatImage() !=null && !cat.getCatImage().isEmpty()){
                                            cat.setCatImage(IMAGE_BASE_URL + cat.getCatImage());
                                        }
                                        return cat;
                                    })
                                    .collect(Collectors.toList());
    }
}
