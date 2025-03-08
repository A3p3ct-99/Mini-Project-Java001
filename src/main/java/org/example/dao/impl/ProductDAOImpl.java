package org.example.dao.impl;



import org.example.constant.Error;
import org.example.constant.Validation;
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
import static org.example.constant.Config.REGEX_TABLE_OPTION;
import static org.example.constant.Validation.getValidatedInput;
import static org.example.utils.ProductUtils.getProductFromDatabase;


public class ProductDAOImpl implements ProductDAO {

    Scanner scanner = new Scanner(System.in);
    List<Product> products = new ArrayList<>();

    // Constructor to initialize mock data
    public ProductDAOImpl() {
        // Adding 3 mock products to the list
        products.add(new Product("1", "Laptop", "1200.00", "10", "2023-10-01"));
        products.add(new Product("2", "Smartphone", "800.00", "25", "2023-09-15"));
        products.add(new Product("3", "Tablet", "500.00", "30", "2023-08-20"));
    }

    @Override
    public void updateProduct() {
        if (products.isEmpty()) {
            System.out.println(Error.ERROR_NO_PRODUCTS);
            return;
        }

        Product productToUpdate = null;
        while (productToUpdate == null) {
            // Get product ID to update
            String productId = Validation.getValidatedInput(
                    scanner::nextLine,
                    value -> {
                        if (value.isBlank()) {
                            return new ValidationResult(false, Error.ERROR_OPTION_EMPTY);
                        }
                        return new ValidationResult(true, "");
                    },
                    "Enter the product ID to update: "
            );

            // Find the product to update
            for (Product product : products) {
                if (product.getId().equals(productId)) { // Use .equals() for String comparison
                    productToUpdate = product;
                    break;
                }
            }

            if (productToUpdate == null) {
                System.out.println(String.format(Error.ERROR_PRODUCT_NOT_FOUND, productId));
            }
        }

        while (true) {
            System.out.println("Select the field to update:");
            System.out.println("1. Name 2. Quantity 3. Price 4. All fields 5.Exit");

            // Get menu choice
            String choiceInput = Validation.getValidatedInput(
                    scanner::nextLine,
                    value -> {
                        try {
                            int choice = Integer.parseInt(value);
                            if (choice < 1 || choice > 5) {
                                return new ValidationResult(false, Error.ERROR_INVALID_CHOICE);
                            }
                            return new ValidationResult(true, "");
                        } catch (NumberFormatException e) {
                            return new ValidationResult(false, Error.ERROR_INVALID_CHOICE);
                        }
                    },
                    "Enter your choice: "
            );
            int choice = Integer.parseInt(choiceInput);

            switch (choice) {
                case 1:
                    // Update name
                    String newName = Validation.getValidatedInput(
                            scanner::nextLine,
                            value -> {
                                if (!isValidName(value)) {
                                    return new ValidationResult(false, Error.ERROR_INVALID_NAME);
                                }
                                return new ValidationResult(true, "");
                            },
                            "Enter new name: "
                    );
                    productToUpdate.setName(newName);
                    System.out.println(Error.SUCCESS_NAME_UPDATED);
                    displayProductTable(productToUpdate);
                    break;
                case 2:
                    // Update quantity
                    String newQty = Validation.getValidatedInput(
                            scanner::nextLine,
                            value -> {
                                if (!isValidQuantity(value)) {
                                    return new ValidationResult(false, Error.ERROR_INVALID_QUANTITY);
                                }
                                return new ValidationResult(true, "");
                            },
                            "Enter new quantity: "
                    );
                    productToUpdate.setQuantity(newQty);
                    System.out.println(Error.SUCCESS_QUANTITY_UPDATED);
                    displayProductTable(productToUpdate);
                    break;
                case 3:
                    // Update price
                    String newPrice = Validation.getValidatedInput(
                            scanner::nextLine,
                            value -> {
                                if (!isValidPrice(value)) {
                                    return new ValidationResult(false, Error.ERROR_INVALID_PRICE);
                                }
                                return new ValidationResult(true, "");
                            },
                            "Enter new price: "
                    );
                    productToUpdate.setPrice(newPrice);
                    System.out.println(Error.SUCCESS_PRICE_UPDATED);
                    displayProductTable(productToUpdate);
                    break;
                case 4:
                    // Update all fields
                    String newNameAll = Validation.getValidatedInput(
                            scanner::nextLine,
                            value -> {
                                if (!isValidName(value)) {
                                    return new ValidationResult(false, Error.ERROR_INVALID_NAME);
                                }
                                return new ValidationResult(true, "");
                            },
                            "Enter new name: "
                    );
                    productToUpdate.setName(newNameAll);

                    String newQtyAll = Validation.getValidatedInput(
                            scanner::nextLine,
                            value -> {
                                if (!isValidQuantity(value)) {
                                    return new ValidationResult(false, Error.ERROR_INVALID_QUANTITY);
                                }
                                return new ValidationResult(true, "");
                            },
                            "Enter new quantity: "
                    );
                    productToUpdate.setQuantity(newQtyAll);

                    String newPriceAll = Validation.getValidatedInput(
                            scanner::nextLine,
                            value -> {
                                if (!isValidPrice(value)) {
                                    return new ValidationResult(false, Error.ERROR_INVALID_PRICE);
                                }
                                return new ValidationResult(true, "");
                            },
                            "Enter new price: "
                    );
                    productToUpdate.setPrice(newPriceAll);

                    System.out.println(Error.SUCCESS_ALL_FIELDS_UPDATED);
                    displayProductTable(productToUpdate);
                    break;
                case 5:
                    System.out.println(Error.SUCCESS_EXIT);
                    return;
                default:
                    System.out.println(Error.ERROR_INVALID_CHOICE);
            }
        }
    }

    // Helper method to validate name (must start with a letter and can contain letters, numbers, and spaces)
    private boolean isValidName(String name) {
        return name.matches("^[a-zA-Z][a-zA-Z0-9 ]*$"); // Starts with a letter, followed by letters, numbers, or spaces
    }

    // Helper method to validate quantity (must be a positive integer)
    private boolean isValidQuantity(String quantity) {
        try {
            int qty = Integer.parseInt(quantity);
            return qty > 0; // Quantity must be greater than 0
        } catch (NumberFormatException e) {
            return false; // Not a valid integer
        }
    }

    // Helper method to validate price (must be a positive float or integer)
    private boolean isValidPrice(String price) {
        try {
            float pr = Float.parseFloat(price);
            return pr > 0; // Price must be greater than 0
        } catch (NumberFormatException e) {
            return false; // Not a valid float or integer
        }
    }

    // Helper method to display product details in a table format
    private void displayProductTable(Product product) {
        System.out.println("+------------+------------------+------------+------------+------------+");
        System.out.println("| Product ID | Name             | Price      | Quantity   | Date       |");
        System.out.println("+------------+------------------+------------+------------+------------+");
        System.out.printf("| %-10s | %-16s | %-10s | %-10s | %-10s |\n",
                product.getId(), product.getName(), product.getPrice(), product.getQuantity(), product.getDate());
        System.out.println("+------------+------------------+------------+------------+------------+");
    }


    @Override
    public void writeProduct() {

    }

    @Override
    public void readProduct() {

    }




    @Override
    public void deleteProduct() {

    }

    @Override
    public void searchProduct() {

    }

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
    public void saveProduct() {

    }

    @Override
    public void unsavedProduct() {

    }


    //Bonus point
    @Override
    public void backUpDatabase() {

    }

    @Override
    public void restoreDatabase() {

    }
}
