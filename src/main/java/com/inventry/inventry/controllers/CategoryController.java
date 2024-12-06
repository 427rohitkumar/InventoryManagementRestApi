package com.inventry.inventry.controllers;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
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

import com.inventry.inventry.Model.Category;
import com.inventry.inventry.Model.User;
import com.inventry.inventry.Service.CategoryService;
import com.inventry.inventry.Service.UserService;


@RestController
@RequestMapping("/api")
public class CategoryController {
    
      @Autowired
      private CategoryService cateService;

      @Autowired
      private UserService userService;


    // ===========  get category Controller handler =================
    @GetMapping("/category/all")
    public ResponseEntity<?> getAllCategory()
    {
        List<Category> cate=cateService.getAllCategory();
        if( cate !=null){
            return new ResponseEntity<>(cate,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Category_Not _Found",HttpStatus.NOT_FOUND);
        }
        
    }

    // ================ getting category by Id ================
    @GetMapping("/category/{catId}")
    public ResponseEntity<?> getCategoryByid(@PathVariable("catId")int catId){
        Optional<Category> optionalCate=cateService.getCateById(catId);
        if(optionalCate.isPresent()){
            Category cateData=optionalCate.get();
            return new ResponseEntity<>(cateData,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Category Not Fond",HttpStatus.NOT_FOUND);
        }
    }
    
    // =========== saving category detail ============
    @PostMapping("/category/save")
    public ResponseEntity<?> saveCategory(@RequestParam("catName")String catName,
                                 @RequestParam("userId")int userId,
                                 @RequestParam("catDesc")String catDesc,
                                 @RequestParam(value="file",required=false)MultipartFile file)
    {
          System.out.println("=============calling category/save handler===========");
       
         // Check for missing fields and respond immediately
         if (catName == null || catName.isEmpty()) {
            return new ResponseEntity<>("Category Name is Required.", HttpStatus.BAD_REQUEST);
        }
        if (userId <=0) {
            return new ResponseEntity<>("UserId is Required.", HttpStatus.BAD_REQUEST);
        }
        if (catDesc == null || catDesc.isEmpty()) {
            return new ResponseEntity<>("Category Description is Required.", HttpStatus.BAD_REQUEST);
        }
        if (file == null || file.isEmpty()) {
            return new ResponseEntity<>("Category imageFile is Required.", HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser= userService.getUserById(userId);
        if(optionalUser.isPresent()){
            try {
                User user=optionalUser.get();
                Category cate=new Category();
                cate.setCatName(catName);
                cate.setUserId(user);
                cate.setCatDesc(catDesc);
                cate.setUpdated_at(new Date());
                if(file !=null && !file.isEmpty()){
                String fileName=saveCategoryFile(file);
                cate.setCatImage(fileName);
                }
                Category saveCat=cateService.saveCategory(cate);

                return new ResponseEntity<>(saveCat,HttpStatus.CREATED);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>("Failed to Created Category",HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>("User Not Found",HttpStatus.NOT_FOUND);
        }   

    }

//    ============= updating category data ================
   @PutMapping("/category/update")
   public ResponseEntity<?> updateCategory(@RequestParam("catName")String catName,
                                           @RequestParam("catId")int catId,
                                           @RequestParam("catDesc")String catDesc,
                                           @RequestParam(value="file",required=false)MultipartFile file){
      Optional<Category> optionalCat=cateService.getCateById(catId);
      if(optionalCat.isPresent()){
          try {
            Category existingCategoryData=optionalCat.get();
            // Category cat=new Category();
            existingCategoryData.setCatName(catName);
            existingCategoryData.setCatDesc(catDesc);
            existingCategoryData.setUpdated_at(new Date());
            existingCategoryData.setCategoryCreationDate(existingCategoryData.getCategoryCreationDate());
            

            if(file !=null && !file.isEmpty()){
                String fileName=saveCategoryFile(file);
                existingCategoryData.setCatImage(fileName);
            }
            Category updateCat=cateService.updateCategory(existingCategoryData);
            return new ResponseEntity<>(updateCat,HttpStatus.OK);
          }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Category Not updated",HttpStatus.INTERNAL_SERVER_ERROR);
           }
        }else{
            return new ResponseEntity<>("Category Not Found",HttpStatus.NOT_FOUND);
        }
   }


//    ======================= delete category by Id ======================
   @DeleteMapping("/category/delete/{catId}")
   public ResponseEntity<?> deleteCategoryById(@PathVariable("catId")int catId){
      // Get the user by ID to retrieve the profile image path
            Optional<Category> optionalCate =cateService.getCateById(catId);
            
            if (!optionalCate.isPresent()) {
                return new ResponseEntity<>("Category not found.", HttpStatus.NOT_FOUND);
            }

            Category cate=optionalCate.get();
            // Get the profile image path
            String catImage = cate.getCatImage(); // Profile image file name or path stored in the DB

            // Delete the user record from the database
            boolean isDeleted = cateService.deleteCategory(catId);
            if (isDeleted) {
                // Check if the profile image is not the default one before attempting to delete
                if (catImage != null && !catImage.isEmpty()) {
                    // Define the directory where the profile images are stored
                    String uploadDir = "static/categoryImages";  // Make sure this path is consistent with where your images are stored
                    
                    // Create the file object to represent the image file
                    File imageFile = new File(uploadDir + File.separator + catImage);
                    
                    // Check if the file exists and delete it
                    if (imageFile.exists() && imageFile.isFile()) {
                        boolean imageDeleted = imageFile.delete();
                        if (!imageDeleted) {
                            System.err.println("Failed to delete Category image: " + imageFile.getAbsolutePath());
                        }
                    }
                }

                return new ResponseEntity<>("Category deleted successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to delete Category.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
   }

//    ================ search Category Data ======================
   @GetMapping("/category/search")
   public ResponseEntity<List<Category>> categorySearch(@RequestParam(value = "query") String query){

     List<Category> searchResult=cateService.getSearchedCategory(query);

     if(searchResult.isEmpty()){
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
     }else{
        return new ResponseEntity<>(searchResult,HttpStatus.OK);
     }
   }

    // ========= file dandlin method of Category images upload ===============
    private String saveCategoryFile(MultipartFile file){
        try {
            //defined the directory where the file will stored
            String uploadCategoryImgDir="static/categoryImages";

            //Creating the directory if it does not exist
            Path cateogryImgDirPath=Paths.get(uploadCategoryImgDir);
              if(!Files.exists(cateogryImgDirPath)){
                 Files.createDirectories(cateogryImgDirPath);
              }

            // Generate Uniqe File Name
            String originalFileName=file.getOriginalFilename();
            String uniqeFileName=System.currentTimeMillis()+"_"+originalFileName;

            //Create File Path
            Path categoryFullFilePath=cateogryImgDirPath.resolve(uniqeFileName);

            //Saveing the FIle to the Directory
            Files.copy(file.getInputStream(),categoryFullFilePath,StandardCopyOption.REPLACE_EXISTING);

            //returing  the file name for storing in the database

            return uniqeFileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("File is not Saved Something went wrong.."+e.getMessage());
        }
    }
   

}
