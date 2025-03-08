package org.example.dao;


import org.example.manager.DatabaseConnectionManager;
import org.example.model.ProductEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.constant.Config.*;


public class ProductDAOImpl implements ProductDAO {

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
        try (PreparedStatement statement = databaseConnectionManager.getConnection().prepareStatement(QUERY_SEARCH_CONTAIN_NAME)) {
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
        try (PreparedStatement statement = databaseConnectionManager.getConnection().prepareStatement(QUERY_INSERT)) {
            statement.setInt(1, product.getId());
            statement.setString(2, product.getName());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getQuantity());
            statement.setDate(5, Date.valueOf(product.getDate()));
            statement.execute();
        } catch (SQLException e) {
            printError("Connection error occurred" + e.getMessage());
        }
    }

    public void updateProduct(ProductEntity product) {
        try (PreparedStatement statement = databaseConnectionManager.getConnection().prepareStatement(QUERY_UPDATE)) {
            statement.setInt(1, product.getId());
            statement.setString(2, product.getName());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getQuantity());
            statement.setDate(5, Date.valueOf(product.getDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            printError("Connection error occurred" + e.getMessage());
        }
    }

    public void deleteProduct(int id) {
        try (Connection connection = databaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            printError("Connection error occurred: " + e.getMessage());
        }
    }

    public List<ProductEntity> getAllProducts() {
        List<ProductEntity> products = new ArrayList<>();
        try (Statement statement = databaseConnectionManager.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(QUERY_SELECT_ALL)) {
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
