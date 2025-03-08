package org.example.dao;

import java.io.IOException;

public interface ProductDAO {
    void writeProduct();
    void readProduct();
    void updateProduct();
    void deleteProduct();
    void searchProduct();
    void setRowTable();
    void saveProduct();
    void unsavedProduct();
    void backUpDatabase() throws IOException, InterruptedException;
    void restoreDatabase();
}
