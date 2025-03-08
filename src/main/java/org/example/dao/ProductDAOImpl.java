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

    public Optional<List<ProductEntity>> getProductsByNameIgnoreCase(String name) {
        String query = "SELECT * FROM products WHERE LOWER(name) = LOWER('" + name + "')";
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
                return Optional.of(List.of(product));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setDate(4, Date.valueOf(product.getDate()));
            statement.setInt(5, product.getId());
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
