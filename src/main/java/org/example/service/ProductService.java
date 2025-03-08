package org.example.service;

import org.example.dto.Product;

import java.util.List;

public interface ProductService {
    void writeProduct(Product product);
    void readProduct(String id);
    void updateProduct();
    void deleteProduct(String id);
    void searchProduct(String name);
    void setRowTable(String numRows);
    void saveProduct();
    void unsavedProduct(List<Product> insertedProduct, List<Product> updatedProduct, String unsavedInput);
    void backUpDatabase();
    void restoreDatabase();
    List<Product> getAllProducts();
}
