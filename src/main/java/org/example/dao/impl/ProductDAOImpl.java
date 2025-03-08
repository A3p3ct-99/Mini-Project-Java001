package org.example.dao.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.example.constant.Error;
import org.example.constant.TableConfig;
import org.example.constant.Validation;
import org.example.dao.ProductDAO;
import org.example.dto.Product;
import org.example.validation.ValidationResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            System.out.println("1. Name 2. Quantity 3. Price 4. All fields 5. Exit");

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
                    TableConfig.displayProductTable(List.of(productToUpdate), 0, 1, 1, 1); // Display updated product in a table
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
                    TableConfig.displayProductTable(List.of(productToUpdate), 0, 1, 1, 1); // Display updated product in a table
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
                    TableConfig.displayProductTable(List.of(productToUpdate), 0, 1, 1, 1); // Display updated product in a table
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
                    TableConfig.displayProductTable(List.of(productToUpdate), 0, 1, 1, 1); // Display updated product in a table
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
        if (products.isEmpty()) {
            System.out.println(Error.ERROR_NO_PRODUCTS);
            return;
        }

        // Auto-increment ID based on the previous product's ID
        int newId = 1; // Default ID if no products exist
        if (!products.isEmpty()) {
            // Find the maximum ID in the existing products
            int maxId = products.stream()
                    .mapToInt(product -> Integer.parseInt(product.getId()))
                    .max()
                    .orElse(0);
            newId = maxId + 1; // Increment the maximum ID by 1
        }

        // Collect and validate product details
        String name = Validation.getValidatedInput(
                scanner::nextLine,
                value -> {
                    if (!isValidName(value)) {
                        return new ValidationResult(false, Error.ERROR_INVALID_NAME);
                    }
                    return new ValidationResult(true, "");
                },
                "Enter product name: "
        );

        String quantity = Validation.getValidatedInput(
                scanner::nextLine,
                value -> {
                    if (!isValidQuantity(value)) {
                        return new ValidationResult(false, Error.ERROR_INVALID_QUANTITY);
                    }
                    return new ValidationResult(true, "");
                },
                "Enter product quantity: "
        );

        String price = Validation.getValidatedInput(
                scanner::nextLine,
                value -> {
                    if (!isValidPrice(value)) {
                        return new ValidationResult(false, Error.ERROR_INVALID_PRICE);
                    }
                    return new ValidationResult(true, "");
                },
                "Enter product price: "
        );

        // Use current local date
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = currentDate.format(formatter);

        // Create and add the new product
        Product newProduct = new Product(String.valueOf(newId), name, price, quantity, date);
        products.add(newProduct);

        System.out.println("Product added successfully!");
        TableConfig.displayProductTable(List.of(newProduct), 0, 1, 1, 1); // Display the new product in a table
    }

    @Override
    public void readProduct() {
        if (products.isEmpty()) {
            System.out.println(Error.ERROR_NO_PRODUCTS);
            return;
        }

        while (true) {
            // Get product ID from the user
            String productId = Validation.getValidatedInput(
                    scanner::nextLine,
                    value -> {
                        if (value.isBlank()) {
                            return new ValidationResult(false, Error.ERROR_OPTION_EMPTY);
                        }
                        return new ValidationResult(true, "");
                    },
                    "Enter the product ID to read (or type 'exit' to cancel): "
            );

            // Allow the user to exit
            if (productId.equalsIgnoreCase("exit")) {
                System.out.println("Operation canceled.");
                return;
            }

            // Find the product by ID
            Product product = products.stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (product == null) {
                System.out.println(String.format(Error.ERROR_PRODUCT_NOT_FOUND, productId));
                System.out.println("Please try again or type 'exit' to cancel.");
            } else {
                System.out.println("Product details:");
                TableConfig.displayProductTable(List.of(product), 0, 1, 1, 1); // Display the product in a table
                return; // Exit the loop after displaying the product
            }
        }
    }

    @Override
    public void deleteProduct() {
        if (products.isEmpty()) {
            System.out.println(Error.ERROR_NO_PRODUCTS);
            return;
        }

        while (true) {
            // Get product ID from the user
            String productId = Validation.getValidatedInput(
                    scanner::nextLine,
                    value -> {
                        if (value.isBlank()) {
                            return new ValidationResult(false, Error.ERROR_OPTION_EMPTY);
                        }
                        return new ValidationResult(true, "");
                    },
                    "Enter the product ID to delete (or type 'exit' to cancel): "
            );

            // Allow the user to exit
            if (productId.equalsIgnoreCase("exit")) {
                System.out.println("Operation canceled.");
                return;
            }

            // Find the product by ID
            Product product = products.stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (product == null) {
                System.out.println(String.format(Error.ERROR_PRODUCT_NOT_FOUND, productId));
                System.out.println("Please try again or type 'exit' to cancel.");
            } else {
                products.remove(product);
                System.out.println("Product with ID " + productId + " deleted successfully.");
                return; // Exit the loop after deleting the product
            }
        }
    }

    @Override
    public void searchProduct() {
        if (products.isEmpty()) {
            System.out.println(Error.ERROR_NO_PRODUCTS);
            return;
        }

        while (true) {
            // Get product name from the user
            String productName = Validation.getValidatedInput(
                    scanner::nextLine,
                    value -> {
                        if (value.isBlank()) {
                            return new ValidationResult(false, Error.ERROR_OPTION_EMPTY);
                        }
                        return new ValidationResult(true, "");
                    },
                    "Enter the product name to search (or type 'exit' to cancel): "
            );

            // Allow the user to exit
            if (productName.equalsIgnoreCase("exit")) {
                System.out.println("Search operation canceled.");
                return;
            }

            // Find products whose names match the input (case-insensitive)
            List<Product> matchingProducts = products.stream()
                    .filter(p -> p.getName().toLowerCase().contains(productName.toLowerCase()))
                    .toList();

            if (matchingProducts.isEmpty()) {
                System.out.println("No products found with name: " + productName);
                System.out.println("Please try again or type 'exit' to cancel.");
            } else {
                System.out.println("Matching products:");
                TableConfig.displayProductTable(matchingProducts, 0, matchingProducts.size(), 1, 1); // Display matching products in a table
                return; // Exit the loop after displaying the products
            }
        }
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
