package org.example.dao;

import org.example.dto.Product;

public interface ProductDAO {
    void writeProduct(Product product); // Updated to accept a Product parameter
    void readProduct();
    void updateProduct();
    void deleteProduct();
    void searchProduct();
    void setRowTable();
    void saveProduct();
    void unsavedProduct();
    void backUpDatabase();
    void restoreDatabase();
}