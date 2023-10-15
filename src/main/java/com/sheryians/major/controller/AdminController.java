package com.sheryians.major.controller;


import com.sheryians.major.domain.Category;
import com.sheryians.major.domain.Product;
import com.sheryians.major.domain.User;
import com.sheryians.major.dto.ProductDTO;
import com.sheryians.major.service.CategoryService;
import com.sheryians.major.service.ProductService;
import com.sheryians.major.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller

public class AdminController {
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;


    @GetMapping("/admin")
    public String adminHome(){

        return "adminHome";
    }

    @GetMapping("/admin/categories")
    public String getCategories(Model model){
        model.addAttribute("categories",categoryService.getAllCategory());
        return "categories";
    }

    @GetMapping("/admin/categories/add")
    public String getCatAdd(Model model){
        model.addAttribute("category",new Category());
        return "categoriesAdd";
    }

    @PostMapping("/admin/categories/add")
    public String postCatAdd(@ModelAttribute("category") @Valid Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "categoriesAdd";
        }

        try {
            categoryService.saveCategory(category);
            model.addAttribute("successMessage", "Category added successfully.");
            return "redirect:/admin/categories";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("categoryError", "Category with this name already exists.");
            return "categoriesAdd";
        }
    }



    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCat(@PathVariable int id, Model model){
        try{
            categoryService.removeCategoryById(id);

        }catch (DataIntegrityViolationException e){
            model.addAttribute("categories",categoryService.getAllCategory());
            model.addAttribute("errorCat","There are products under this category!!");
        }
        model.addAttribute("successCat","There were no products under this category!!");
        model.addAttribute("categories",categoryService.getAllCategory());
        return "categories";
    }

    @RequestMapping("/admin/categories/update/{id}")
    public String updateCat(@PathVariable int id, Model model){
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isPresent()){
            model.addAttribute("category",category.get());
            return "categoriesAdd";
        }else{
            return "404";
        }
    }

    //Product section
    @GetMapping("/admin/products")
    public String product(Model model){
        model.addAttribute("products", productService.getAllProduct());
        return "products";
    }

    @GetMapping("/admin/products/add")
    public String productAddGet(Model model){
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "productsAdd";
    }


    @PostMapping("/admin/products/add")
    public String productAddPost(@ModelAttribute("productDTO") ProductDTO productDTO,
                                 @RequestParam("productImage")MultipartFile file,
                                 @RequestParam("imgName")String imgName,
                                 Model model) throws IOException{




        String productName = productDTO.getName();
        int productCategory = productDTO.getCategoryId();
        List<Product> existingProducts = productService.findProductByName(productName);

        if (!existingProducts.isEmpty()){

            for(Product existingProduct : existingProducts){
                if (existingProduct.getCategory().getId() == productCategory){
                        model.addAttribute("productExistsMessage", "A product with the same name already exists.");
                        model.addAttribute("categories", categoryService.getAllCategory());

                        return "productsAdd";
                }
            }


        }

        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).get());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());



        String imageUUID;
        if(!file.isEmpty()){
            imageUUID = file.getOriginalFilename();
            Path fileNamePath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNamePath, file.getBytes());
        }else{
            imageUUID = imgName;
        }
        product.setImageName(imageUUID);
        productService.addProduct(product);
        model.addAttribute("successMessage", "Product added successfully.");
        return "redirect:/admin/products";


//        boolean hasErrors = result.hasErrors();
//        if (hasErrors){
//            model.addAttribute("productDTO", productDTO);
//            return "productsAdd";
//        }


    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id,Model model){

        model.addAttribute("successPro","Product Removed!!");
        productService.removeProductById(id);
        return "redirect:/admin/products";
    }



    @GetMapping("/admin/product/update/{id}")
    public String updateProductGet(@PathVariable long id,Model model){
        Product product = productService.getProductById(id).get();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setPrice(product.getPrice());
        productDTO.setWeight(product.getWeight());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageName(product.getImageName());

        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("productDTO",productDTO);

        return "productsAdd";
    }

    @GetMapping("/admin/adminManageUsers")
    public String getall(Model model){
        model.addAttribute("allUsers",userService.getAllUsers());
        return "adminUsers";
    }

    @GetMapping("admin/user/block/{id}")
    public String block(@PathVariable int id){
        User user = userService.findById(id).get();
        user.setEnable(false);
        userService.save(user);
        return "redirect:/admin/adminManageUsers";

    }

    @GetMapping("admin/user/unblock/{id}")
    public String unblock(@PathVariable int id){
        User user = userService.findById(id).get();
        user.setEnable(true);
        userService.save(user);
        return "redirect:/admin/adminManageUsers";

    }

    @GetMapping("admin/user/remove/{id}")
    public String removeUser(@PathVariable int id){
        User user = userService.findById(id).get();
        userService.delete(user);
        return "redirect:/admin/adminManageUsers";
    }
}
