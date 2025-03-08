package org.example.service;



import org.example.dao.ProductDAO;
import org.example.dao.ProductDAOImpl;
import org.example.dto.Product;
import org.example.utils.ProductUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ProductServiceImpl implements ProductService {


    List<Product> products = new ArrayList<>();
    ProductDAO productDAO = new ProductDAOImpl();

    @Override
    public void writeProduct() {

    }

    @Override
    public void readProduct() {

    }

    @Override
    public void updateProduct() {

    }

    @Override
    public void deleteProduct() {

    }

    @Override
    public void searchProduct() {

    }

    @Override
    public void saveProduct() {

    }

    @Override
    public void unsavedProduct() {

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
