package com.sheryians.major.controller;

import com.sheryians.major.domain.Product;
import com.sheryians.major.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public class ProductController {

    @Autowired
    ProductService productService;




}
