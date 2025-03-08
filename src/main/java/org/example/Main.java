package org.example;


import org.example.controller.ProductController;

public class Main {
    public static void main(String[] args) {

        ProductController productController = new ProductController();
        productController.start();
    }
}
