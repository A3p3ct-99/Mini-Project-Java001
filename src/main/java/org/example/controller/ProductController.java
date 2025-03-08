package org.example.controller;

import org.example.service.ProductService;
import org.example.service.ProductServiceImpl;
import org.example.view.ProductView;

public class ProductController {

    ProductService service;
    ProductView productController;

    public ProductController() {
        service = new ProductServiceImpl();
        productController = new ProductView(service);
    }
    public void start() {
        productController.start();
    }
}
