package org.example.dao;

public interface ProductDAO {
    void writeProduct();
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
