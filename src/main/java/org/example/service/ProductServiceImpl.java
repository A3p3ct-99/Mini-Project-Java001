package org.example.service;



import org.example.constant.Color;
import org.example.constant.Error;
import org.example.constant.TableConfig;
import org.example.constant.Validation;
import org.example.dao.ProductDAO;
import org.example.dao.ProductDAOImpl;
import org.example.dto.Product;
import org.example.model.ProductEntity;
import org.example.utils.ProductUtils;
import org.example.validation.ValidationResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.example.constant.Color.*;
import static org.example.constant.Color.RESET;
import static org.example.constant.Config.*;
import static org.example.constant.Error.ERROR_OPTION_EMPTY;
import static org.example.constant.TableConfig.displayProductByIdAndName;
import static org.example.constant.TableConfig.displayProductTable;
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
        var productList = readProductsFromFile(UPDATE_PRODUCT_FILE_NAME);
        var updatedProduct = productList.stream()
                .map(p -> p.getId().equals(product.getId()))
                .toList();
        if (updatedProduct.isEmpty()) {
            writeProductToFile(product, UPDATE_PRODUCT_FILE_NAME);
            return;
        }
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
        if (savedOption.equals("b")) {
            return;
        }
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
                    System.out.println(GREEN + "✅ Product saved successfully." + RESET);
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
                    System.out.println(GREEN + "✅ Product updated successfully." + RESET);
                    clearProductFile(UPDATE_PRODUCT_FILE_NAME);
                });
            }
            default -> System.out.println(Color.RED + "\nInvalid Option... Please try again!!!" + Color.RESET);
        }
    }

    @Override
    public void unsavedProduct(List<Product> insertedProduct, List<Product> updatedProduct ,String unsavedInput) {
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
    public void backUpDatabase() {

    }

    @Override
    public void restoreDatabase() {

    }

    @Override
    public List<Product> getAllProducts() {
        var productsEntity = productDAO.getAllProducts();
        return productsEntity.stream().map(ProductUtils::getProductFromDatabase).toList();
    }
}
