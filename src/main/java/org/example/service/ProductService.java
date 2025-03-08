package org.example.service;

public interface ProductService {
    void writeProduct();
    void readProduct();
    void updateProduct();
    void deleteProduct();
    void searchProduct(String name);
    void setRowTable();
    void saveProduct();
    void unsavedProduct();
    void backUpDatabase();
    void restoreDatabase();
}
