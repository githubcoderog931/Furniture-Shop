package com.sheryians.major.controller;


import com.sheryians.major.domain.*;
import com.sheryians.major.dto.ProductDTO;
import com.sheryians.major.repository.*;
import com.sheryians.major.service.*;
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

import static java.util.Arrays.stream;

@Controller
public class AdminController {
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";
    @Autowired
    CategoryService categoryService;

    @Autowired
    ReferralRepository referralRepository;

    @Autowired
    ReferralService referralService;

    @Autowired
    ProductService productService;
    
    @Autowired
    UserService userService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductImageRepository productImageRepository;




    @Autowired
    CategoryRepository categoryRepository;

    // show admin page
    @GetMapping("/admin/home")
    public String adminHome() {
        return "adminHome";
    }


    // get all the categories
    @GetMapping("/admin/categories")
    public String getCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        return "categories";
    }


    // get/show add categories page
    @GetMapping("/admin/categories/add")
    public String getCatAdd(Model model) {
        return "categoriesAdd";
    }


    // store new category to database
    @PostMapping("/category/add")
    public String postCatAdd(@RequestParam("name") String name,@RequestParam("description") String description, Model model) {
        Category category = new Category();
        try {
            category.setName(name);
            category.setDescription(description);
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
    public String deleteCat(@PathVariable int id, Model model) {
        try {
            categoryService.removeCategoryById(id);

        } catch (DataIntegrityViolationException e) {
            model.addAttribute("categories", categoryService.getAllCategory());
            model.addAttribute("errorCat", "There are products under this category!!");
        }
        model.addAttribute("successCat", "There were no products under this category!!");
        model.addAttribute("categories", categoryService.getAllCategory());
        return "categories";
    }


    // show category update page

    @RequestMapping("/admin/categories/update/{id}")
    public String updateCat(@PathVariable int id, Model model) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "categoriesAdd";
        } else {
            return "404";
        }
    }

    // show all the products
    @GetMapping("admin/products")
    public String product(Model model) {
        model.addAttribute("products", productService.getAllProduct());
        return "products";
    }

    // show add product page

    @GetMapping("admin/products/add")
    public String productAddGet(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "productsAdd";
    }


    // insert/add a new product to database
    @PostMapping("/product/add")
    public String productAddPost(@ModelAttribute("productDTO") ProductDTO productDTO,
                                 @RequestParam("productImage") List<MultipartFile> files,
                                 @RequestParam("imgName") String imgName,
                                 Model model) throws IOException {

        String productName = productDTO.getName();
        int productCategory = productDTO.getCategoryId();
        List<Product> existingProducts = productService.findProductByName(productName);

        if (!existingProducts.isEmpty()) {

            for (Product existingProduct : existingProducts) {
                if (existingProduct.getCategory().getId() == productCategory) {
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

        for (int i = 1; i < files.size(); i++) {
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
    public String deleteProduct(@PathVariable long id, Model model) {

        model.addAttribute("successPro", "Product Removed!!");
        productService.removeProductById(id);
        return "redirect:/admin/products";
    }







    @GetMapping("/admin/product/update/{id}")
    public String updateProductGet(@PathVariable long id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        model.addAttribute("product",product);
        model.addAttribute("categories",categoryRepository.findAll());
        return "productUpdate";
    }


    @PostMapping("/product/update")
    public String productUpdate(@RequestParam("name") String name,
                                @RequestParam("category") Category category,
                                @RequestParam("price") double price,
                                @RequestParam("stock") long stock,
                                @RequestParam("description")String description,
                                @RequestParam("id")long id){
        Product product = productRepository.findById(id).orElse(null);
        assert product != null;
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setUnitsInStock(stock);
        product.setDescription(description);
        productRepository.save(product);
        return "redirect:/admin/products";
    }




    @GetMapping("/admin/ManageUsers")
    public String getall(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        return "adminUsers";
    }

    @GetMapping("/user/block/{id}")
    public String block(@PathVariable int id) {
        User user = userService.findById(id).get();
        user.setEnable(false);
        userService.save(user);
        return "redirect:/admin/ManageUsers";

    }

    @GetMapping("/user/unblock/{id}")
    public String unblock(@PathVariable int id) {
        User user = userService.findById(id).get();
        user.setEnable(true);
        userService.save(user);
        return "redirect:/admin/ManageUsers";

    }

    @GetMapping("/user/remove/{id}")
    public String removeUser(@PathVariable int id) {
        User user = userService.findById(id).get();
        userService.delete(user);
        return "redirect:/admin/ManageUsers";
    }


    // admin get order details

//    @GetMapping("/admin/ManageOrders")
//
//    public String manageOrders(Model model, Principal principal) {
//        User user = userService.getUserByEmail(principal.getName());
//        List<Orders> orders = orderRepository.findAll();
//        model.addAttribute("orders", orders);
//        return "adminOrder";
//    }


    // order details view page

    @GetMapping("/orderDetails/{id}")
    public String adminViewOrderPage(@PathVariable("id") Long id, Model model) {
        Orders orders = orderRepository.findById(id).get();
        model.addAttribute("orders", orders);
        return "adminViewOrderDetails";
    }


    // view stocks page

    @GetMapping("/admin/manageStocks")
    public String viewStocksPage( Model model){
        model.addAttribute("products", productService.getAllProduct());
        return "adminStock";
    }


    // admin add stock

    @GetMapping("/addStock/{id}")
    public String adminAddStock(@PathVariable("id") Long id){
        Product product = productService.getProductById(id);
        System.out.println(product);
        long stock = product.getUnitsInStock();
        product.setUnitsInStock(stock+1);
        productRepository.save(product);
        return "redirect:/admin/manageStocks";
    }


    @GetMapping("/minusStock/{id}")
    public String adminMinusStock(@PathVariable("id") Long id){
        Product product = productService.getProductById(id);
        System.out.println(product);
        long stock = product.getUnitsInStock();
        product.setUnitsInStock(stock-1);
        productRepository.save(product);

        return "redirect:/admin/manageStocks";
    }


    // update stocks page

    @GetMapping("/updateStock/{id}")
    public String updateStocks(@PathVariable("id") Long id, Model model){
        Product product = productRepository.findById(id).orElse(null);

        model.addAttribute("product",product);
        model.addAttribute("productId",id);
        return "updateStock";
    }

    // post or receive update

    @PostMapping("/updateStock")
    public String postUpdateStocks(@RequestParam("newStock") long newStock,@RequestParam("productId") long id,Model model){
        Product product = productRepository.findById(id).orElse(null);
        if(newStock>=0){
            assert product != null;
            product.setUnitsInStock(newStock);
            productRepository.save(product);
            return "redirect:/admin/manageStocks";
        }
        model.addAttribute("stockError", "Stock cannot go below zero");
        model.addAttribute("product",product);
        return "redirect:/updateStock/"+product.getId();


    }

//    @GetMapping("/referrals")
//    public String viewCompleteReferals(Principal principal, Model model){
//        User user = userService.findByUsername(principal.getName());
//        List<Referral> referral = referralRepository.findAll();
//        Long referralCount = (long) referral.size();
//        List<Referral> referralComplete = referralRepository.findByComplete(user);
//        model.addAttribute("successfullReferral",referralComplete);
//        model.addAttribute("totalReferral",referralCount);
//
//    }

}
