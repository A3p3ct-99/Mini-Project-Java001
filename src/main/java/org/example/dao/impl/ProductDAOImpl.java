package org.example.dao.impl;
import org.example.dao.ProductDAO;
import org.example.dto.Product;
import org.example.validation.ValidationResult;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.example.constant.Config.ENTER_ROWS;
import static org.example.constant.Validation.getValidatedInput;

public class ProductDAOImpl implements ProductDAO {

    Scanner scanner = new Scanner(System.in);
    List<Product> products = new ArrayList<>();

    @Override
    public void writeProduct(Product product) {
        products.add(product);

        // Save product to a file you can modify this to use in a database
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt", true))) {
            writer.write(product.getId() + "," + product.getName() + "," + product.getPrice() + "," + product.getQuantity() + "," + product.getDate());
            writer.newLine();
            System.out.println("Product successfully saved!");
        } catch (IOException e) {
            System.out.println("Error saving product: " + e.getMessage());
        }
    }

    @Override
    public void readProduct() {}

    @Override
    public void updateProduct() {}

    @Override
    public void deleteProduct() {}

    @Override
    public void searchProduct() {}

    @Override
    public void setRowTable() {
        String numRows = getValidatedInput(
                scanner::nextLine,
                value -> {
                    if (!value.matches("\\d+")) {
                        return new ValidationResult(false, "Invalid input, please enter a number");
                    }
                    return new ValidationResult(true, "");
                },
                "\n" + ENTER_ROWS
        );

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rowTable.txt"))) {
            writer.write(numRows);
        } catch (IOException e) {
            System.out.println("An error occurred " + e.getMessage());
        }
    }

    @Override
    public void saveProduct() {}

    @Override
    public void unsavedProduct() {}

    @Override
    public void backUpDatabase() {}

    @Override
    public void restoreDatabase() {}

}