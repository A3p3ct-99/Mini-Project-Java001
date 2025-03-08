package org.example.service;


import org.example.constant.Color;
import org.example.dao.ProductDAO;
import org.example.dao.ProductDAOImpl;
import org.example.dto.Product;
import org.example.model.ProductEntity;
import org.example.utils.ProductUtils;
import org.example.validation.ValidationResult;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

import static java.sql.DriverManager.getConnection;
import static org.example.constant.Color.*;
import static org.example.constant.Color.RESET;
import static org.example.constant.Config.*;
import static org.example.constant.Error.ERROR_OPTION_EMPTY;
import static org.example.constant.TableConfig.*;
import static org.example.utils.ProductUtils.*;


public class ProductServiceImpl implements ProductService {

    ProductDAO productDAO = new ProductDAOImpl();

    @Override
    public void writeProduct(Product product) {
        writeProductToFile(product, INSERT_PRODUCT_FILE_NAME);
    }

    @Override
    public void readProduct(String id) {
        int productId = Integer.parseInt(id);
        var productEntity = productDAO.getProductById(productId);
        var product = productEntity
                .map(ProductUtils::getProductFromDatabase)
                .orElse(null);
        if (product == null) {
            printError("Product with id " + productId + " not found.");
            return;
        }
        displayProductByIdAndName(product);
    }

    @Override
    public void updateProduct(Product product) {
        updateProductToFile(product, UPDATE_PRODUCT_FILE_NAME);
    }

    @Override
    public void deleteProduct(String id) {
        int productId = Integer.parseInt(id);
        productDAO.deleteProduct(productId);
    }

    @Override
    public void searchProduct(String name) {
        var productsEntity = productDAO.getProductsContainNameIgnoreCase(name);
        if (productsEntity.isEmpty()) {
            System.out.println(RED + "❌ Product is  not found" + RESET);
        } else {
            var products = productsEntity.filter(productEntities -> !productEntities.isEmpty())
                    .map(productEntities -> productEntities.stream().map(ProductUtils::getProductFromDatabase).toList())
                    .orElseGet(ArrayList::new);
            displayProductTable(products, 0, products.size(), 1, 1);
        }
    }

    @Override
    public void saveProduct(String savedOption) {
        var insertedProducts = readProductsFromFile(INSERT_PRODUCT_FILE_NAME);
        var updatedProducts = readProductsFromFile(UPDATE_PRODUCT_FILE_NAME);
        switch (savedOption) {
            case "si" -> {
                if (insertedProducts.isEmpty()) {
                    System.out.println(RED + "❌ No unsaved product write to save." + RESET);
                    return;
                }
                insertedProducts.forEach(product -> {
                    ProductEntity productEntity = createProductEntity(
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            product.getQuantity(),
                            product.getDate()
                    );
                    productDAO.addProduct(productEntity);
                    System.out.println(GREEN + "✅\uD83D\uDED2 Product " + product.getId() + " saved successfully." + RESET);
                    clearProductFile(INSERT_PRODUCT_FILE_NAME);
                });
            }
            case "su" -> {
                if (updatedProducts.isEmpty()) {
                    System.out.println(RED + "❌ No unsaved product update to save." + RESET);
                    return;
                }
                updatedProducts.forEach(product -> {
                    ProductEntity productEntity = createProductEntity(
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            product.getQuantity(),
                            product.getDate()
                    );
                    productDAO.updateProduct(productEntity);
                    System.out.println(GREEN + "✅\uD83D\uDED2 Product " + product.getId() + " updated successfully." + RESET);
                    clearProductFile(UPDATE_PRODUCT_FILE_NAME);
                });
            }
            default -> System.out.println(Color.RED + "\nInvalid Option... Please try again!!!" + Color.RESET);
        }
    }

    @Override
    public void unsavedProduct(List<Product> insertedProduct, List<Product> updatedProduct, String unsavedInput) {
        while (true) {
            switch (unsavedInput.toLowerCase()) {
                case "ui" -> displayProductTable(insertedProduct, 0, insertedProduct.size(), 1, 1);
                case "uu" -> displayProductTable(updatedProduct, 0, updatedProduct.size(), 1, 1);
                case "b" -> {
                    return;
                }
                default -> {
                    System.out.println(Color.RED + "❌ Invalid option! Please enter a valid option ('ui', 'uu', 'b')! Try again." + Color.RESET);
                    continue;
                }
            }
            break;
        }
    }

    @Override
    public void setRowTable(String numRows) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rowTable.txt"))) {
            writer.write(numRows);
        } catch (IOException e) {
            System.out.println("An error occurred " + e.getMessage());
        }
    }


    //Bonus point
    @Override
    public void backUpDatabase(String yN) {
        String backupDir = "backup";
        File directory = new File(backupDir);
        if (!directory.exists()) {
            directory.mkdir();
        }
        var products = productDAO.getAllProducts();
        if (products.isEmpty()) {
            System.out.println(RED + "❌ No product to backup." + RESET);
            return;
        }

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        int versionIndex = directory.listFiles() != null ? directory.listFiles().length + 1 : 1;
        String backupFileName = String.format("Version%d-product-backup-%s.sql", versionIndex, formattedDate);

        backupDatabase(backupDir + "/" + backupFileName, products.stream().map(ProductUtils::getProductFromDatabase).collect(Collectors.toList()));
    }

    @Override
    public void restoreDatabase(String backupId) {
        String backupDir = "backup";
        File directory = new File(backupDir);
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            System.out.println(RED + "❌ No backup files found." + RESET);
            return;
        }
        System.out.println("Enter backup_id to restore: ");

        File selectedFile = null;
        for (File file : files) {
            if (file.getName().contains("Version" + backupId + "-product-backup-")) {
                selectedFile = file;
                break;
            }
        }

        if (selectedFile == null) {
            System.out.println(RED + "❌ Backup file not found with ID: " + backupId + RESET);
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader("backup/" + selectedFile.getName()))) {
            productDAO.clearDatabase();
            String line;
            while ((line = reader.readLine()) != null) {
                productDAO.executeCreateQuery(line);
            }
            System.out.println("Database restore completed successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred during restore: " + e.getMessage());
        }
    }

    @Override
    public List<Product> getAllProducts() {
        var productsEntity = productDAO.getAllProducts();
        return productsEntity.stream().map(ProductUtils::getProductFromDatabase).toList();
    }
}
