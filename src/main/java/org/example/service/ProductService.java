package org.example.service;

import org.example.dto.Product;

import java.util.List;

public interface ProductService {
    void writeProduct(Product product);
    void readProduct(String id);
    void updateProduct();
    void deleteProduct();
    void searchProduct();
    void setRowTable(String numRows);
    void saveProduct();
    void unsavedProduct();
    void backUpDatabase();
    void restoreDatabase();
    List<Product> getAllProducts();
}
