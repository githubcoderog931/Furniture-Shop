package com.sheryians.major.controller;


import com.sheryians.major.domain.*;
import com.sheryians.major.dto.ProductDTO;
import com.sheryians.major.repository.OrderRepository;
import com.sheryians.major.repository.ProductImageRepository;
import com.sheryians.major.service.CategoryService;
import com.sheryians.major.service.OrderService;
import com.sheryians.major.service.ProductService;
import com.sheryians.major.service.UserService;
import org.hibernate.criterion.Order;
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
import java.security.Principal;
import java.util.ArrayList;
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

    @Autowired
    ProductImageRepository productImageRepository;

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    // show admin page
    @GetMapping("/admin/home")
    public String adminHome(){
        return "adminHome";
    }


    // get all the categories
    @GetMapping("/admin/categories")
    public String getCategories(Model model){
        model.addAttribute("categories",categoryService.getAllCategory());
        return "categories";
    }


    // get/show add categories page
    @GetMapping("/admin/categories/add")
    public String getCatAdd(Model model){
        model.addAttribute("category",new Category());
        return "categoriesAdd";
    }


    // store new category to database
    @PostMapping("/category/add")
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



    // show the category delete page
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


    // show category update page

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

    // show all the products
    @GetMapping("admin/products")
    public String product(Model model){
        model.addAttribute("products", productService.getAllProduct());
        return "products";
    }

    // show add product page

    @GetMapping("admin/products/add")
    public String productAddGet(Model model){
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "productsAdd";
    }



    // insert/add a new product to database
    @PostMapping("/product/add")
    public String productAddPost(@ModelAttribute("productDTO") ProductDTO productDTO,
                                 @RequestParam("productImage")List<MultipartFile> files,
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
        imageUUID = files.get(0).getOriginalFilename();
        Path fileNamePath = Paths.get(uploadDir, imageUUID);
        Files.write(fileNamePath, files.get(0).getBytes());

        product.setImageName(imageUUID);

        List<ProductImage> mList = new ArrayList<>();

        for (int i=1; i< files.size();i++) {
            imageUUID = files.get(i).getOriginalFilename();
            fileNamePath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNamePath, files.get(i).getBytes());
            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            productImage.setImageUrl(imageUUID);
            mList.add(productImage);
        }
        product.setUnitsInStock(productDTO.getUnitsInStock());
        product.setImages(mList);
        productService.addProduct(product);
        model.addAttribute("successMessage", "Product added successfully.");
        return "redirect:/admin/products";


    }


    // show delete product page
    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id,Model model){

        model.addAttribute("successPro","Product Removed!!");
        productService.removeProductById(id);
        return "redirect:/admin/products";
    }



    @GetMapping("/admin/product/update/{id}")
    public String updateProductGet(@PathVariable long id,Model model){
        Product product = productService.getProductById(id);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageName(product.getImageName());

        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("productDTO",productDTO);

        return "productsAdd";
    }

    @GetMapping("/admin/ManageUsers")
    public String getall(Model model){
        model.addAttribute("allUsers",userService.getAllUsers());
        return "adminUsers";
    }

    @GetMapping("/user/block/{id}")
    public String block(@PathVariable int id){
        User user = userService.findById(id).get();
        user.setEnable(false);
        userService.save(user);
        return "redirect:/admin/ManageUsers";

    }

    @GetMapping("/user/unblock/{id}")
    public String unblock(@PathVariable int id){
        User user = userService.findById(id).get();
        user.setEnable(true);
        userService.save(user);
        return "redirect:/admin/ManageUsers";

    }

    @GetMapping("/user/remove/{id}")
    public String removeUser(@PathVariable int id){
        User user = userService.findById(id).get();
        userService.delete(user);
        return "redirect:/admin/ManageUsers";
    }


    // admin get order details

    @GetMapping("/admin/ManageOrders")

    public String manageOrders(Model model, Principal principal)
    {
        User user = userService.getUserByEmail(principal.getName());
        List<Orders> orders = orderRepository.findAll();
        model.addAttribute("orders",orders);
        return "adminOrder";
    }



    // order details view page

    @GetMapping("/orderDetails/{id}")
    public String adminViewOrderPage(@PathVariable("id") Long id,Model model){
        Orders orders = orderRepository.findById(id).get();
        model.addAttribute("orders",orders);
        return "adminViewOrderDetails";
    }


}
