package org.example.dao;

import org.example.model.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    Optional<ProductEntity> getProductById(int id);
    Optional<List<ProductEntity>> getProductsContainNameIgnoreCase(String name);
    void addProduct(ProductEntity product);
    void updateProduct(ProductEntity product);
    void deleteProduct(int id);
    List<ProductEntity> getAllProducts();
    void clearDatabase();
    void executeCreateQuery(String query);
}
