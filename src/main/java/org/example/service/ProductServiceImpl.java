package org.example.service;



import org.example.constant.Color;
import org.example.dao.ProductDAO;
import org.example.dao.ProductDAOImpl;
import org.example.dto.Product;
import org.example.utils.ProductUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.example.constant.Color.*;
import static org.example.constant.Color.RESET;
import static org.example.constant.Config.INSERT_PRODUCT_FILE_NAME;
import static org.example.constant.Config.printError;
import static org.example.constant.TableConfig.displayProductByIdAndName;
import static org.example.constant.TableConfig.displayProductTable;
import static org.example.utils.ProductUtils.writeProductToFile;


public class ProductServiceImpl implements ProductService {


    List<Product> products = new ArrayList<>();
    List<Product> insertedProduct = new ArrayList<>();
    List<Product> updatedProduct = new ArrayList<>();
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
    public void updateProduct() {

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
    public void saveProduct() {

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
