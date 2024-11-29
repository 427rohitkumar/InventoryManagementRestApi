package com.inventry.inventry.controllers;

import java.util.List;
import com.inventry.inventry.Model.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventry.inventry.Model.Products;
import com.inventry.inventry.Service.CategoryService;
import com.inventry.inventry.Service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
     private ProductService prService;

     @Autowired
     private CategoryService catService;

    @GetMapping("/products")
    public List<Products> getProduct(){
        return prService.getAllProducts();
    }

    @GetMapping("/category")
    public List<Category> getCategory(){
        return catService.getAllCategory();
    }

    
}
