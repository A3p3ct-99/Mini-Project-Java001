package org.example.dao;


import org.example.domain.DatabaseConnectionManager;
import org.example.model.ProductEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.constant.Config.printError;


public class ProductDAOImpl {

    private final DatabaseConnectionManager databaseConnectionManager;

    public ProductDAOImpl() {
        this.databaseConnectionManager = new DatabaseConnectionManager();
    }

    public Optional<ProductEntity> getProductById(int id) {
        String query = "SELECT * FROM products WHERE id = " + id;
        try (Statement statement = databaseConnectionManager.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                ProductEntity product = new ProductEntity(
                        resultSet.getInt("id"),
                        resultSet.getString("product_name"),
                        resultSet.getDouble("unit_price"),
                        resultSet.getInt("quantity"),
                        resultSet.getDate("created_at").toLocalDate()
                );
                return Optional.of(product);
            }
        } catch (SQLException e) {
            printError("Connection error occurred" + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<List<ProductEntity>> getProductsContainNameIgnoreCase(String name) {
        String query = "SELECT * FROM products WHERE product_name ILIKE ?";
        try (PreparedStatement statement = databaseConnectionManager.getConnection().prepareStatement(query)) {
            statement.setString(1, "%" + name + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                List<ProductEntity> products = new ArrayList<>();
                while (resultSet.next()) {
                    ProductEntity product = new ProductEntity(
                            resultSet.getInt("id"),
                            resultSet.getString("product_name"),
                            resultSet.getDouble("unit_price"),
                            resultSet.getInt("quantity"),
                            resultSet.getDate("created_at").toLocalDate()
                    );
                    products.add(product);
                }
                return Optional.of(products);
            }
        } catch (SQLException e) {
            printError("Connection error occurred" + e.getMessage());
        }
        return Optional.empty();
    }

    public void addProduct(ProductEntity product) {
        String query = "INSERT INTO products (name, price, quantity, date) VALUES ('" + product.getName() + "', " + product.getPrice() + ", " + product.getQuantity() + ", '" + product.getDate() + "')";
        try (Statement statement = databaseConnectionManager.getConnection().createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            printError("Connection error occurred" + e.getMessage());
        }
    }

    public void updateProduct(ProductEntity product) {
       
        
    }

    public void deleteProduct(int id) {
        String query = "DELETE FROM products WHERE id = " + id;
        try (Statement statement = databaseConnectionManager.getConnection().createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            printError("Connection error occurred" + e.getMessage());
        }
    }

    public List<ProductEntity> getAllProducts() {
        String query = "SELECT * FROM products";
        List<ProductEntity> products = new ArrayList<>();
        try (Statement statement = databaseConnectionManager.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                ProductEntity product = new ProductEntity(
                        resultSet.getInt("id"),
                    resultSet.getString("product_name"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getInt("quantity"),
                    resultSet.getDate("created_at").toLocalDate()
                );
                products.add(product);
            }
        } catch (SQLException e) {
            printError("Connection error occurred" + e.getMessage());
        }
        return products;
    }


}
