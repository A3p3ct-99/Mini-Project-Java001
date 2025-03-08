package org.example.dao.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.example.constant.Color;
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
import java.util.stream.Collectors;

import static org.example.constant.Config.ENTER_ROWS;
import static org.example.constant.Config.REGEX_TABLE_OPTION;
import static org.example.constant.Validation.getValidatedInput;
import static org.example.utils.ProductUtils.getProductFromDatabase;


public class ProductDAOImpl implements ProductDAO {

    Scanner scanner = new Scanner(System.in);
    List<Product> products = new ArrayList<>();
    List<Product> insertedProduct = new ArrayList<>();
    List<Product> updatedProduct = new ArrayList<>();

    // Constructor to initialize mock data
    public ProductDAOImpl() {
        // Adding 3 mock products to the list
        products.add(new Product("1", "Laptop", "1200.00", "10", "2023-10-01"));
        products.add(new Product("2", "Smartphone", "800.00", "25", "2023-09-15"));
        products.add(new Product("3", "Tablet", "500.00", "30", "2023-08-20"));
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
                            return new ValidationResult(false, "Input cannot be empty!");
                        }
                        return new ValidationResult(true, "");
                    },
                    "Enter the product name to search (or type 'exit' to cancel): "
            );

            // Allow the user to exit
            if (productName.equalsIgnoreCase("exit")) {
                System.out.println("Operation canceled.");
                return;
            }

            // Find products by name (case-insensitive search)
            List<Product> foundProducts = products.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(productName))
                    .collect(Collectors.toList());

            if (foundProducts.isEmpty()) {
                System.out.println("No products found with name: " + productName);
                // Display an empty table
                TableConfig.displayProductTable(new ArrayList<>(), 0, 1, 1, 1);
            } else {
                System.out.println("Found products:");
                TableConfig.displayProductTable(foundProducts, 0, 1, 1, 1); // Display matching products in a table
            }

            System.out.println(Color.YELLOW + "Press Enter to continue..." + Color.RESET);
            scanner.nextLine();
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
    public void updateProduct() {
        if (products.isEmpty()) {
            System.out.println(Error.ERROR_NO_PRODUCTS);
            return;
        }

        // Find the product to update
        Product productToUpdate = null;
        while (productToUpdate == null) {
            String productId = Validation.getValidatedInput(
                    scanner::nextLine,
                    value -> value.isBlank() ? new ValidationResult(false, Error.ERROR_OPTION_EMPTY) : new ValidationResult(true, ""),
                    "Enter the product ID to update: "
            );

            for (Product product : products) {
                if (product.getId().equals(productId)) {
                    productToUpdate = product;
                    break;
                }
            }

            if (productToUpdate == null) {
                System.out.println(String.format(Error.ERROR_PRODUCT_NOT_FOUND, productId));
            }
        }

        // Create a copy of the product to update
        Product updatedProductCopy = new Product(
                productToUpdate.getId(),
                productToUpdate.getName(),
                productToUpdate.getPrice(),
                productToUpdate.getQuantity(),
                productToUpdate.getDate()
        );

        // Update product fields
        while (true) {
            System.out.println("Select the field to update:");
            System.out.println("1. Name 2. Quantity 3. Price 4. All fields 5. Exit");

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
                case 1: // Update name
                    String newName = Validation.getValidatedInput(
                            scanner::nextLine,
                            value -> isValidName(value) ? new ValidationResult(true, "") : new ValidationResult(false, Error.ERROR_INVALID_NAME),
                            "Enter new name: "
                    );
                    updatedProductCopy.setName(newName);
                    System.out.println(Error.SUCCESS_NAME_UPDATED);
                    TableConfig.displayProductTable(List.of(updatedProductCopy), 0, 1, 1, 1);
                    break;

                case 2: // Update quantity
                    String newQty = Validation.getValidatedInput(
                            scanner::nextLine,
                            value -> isValidQuantity(value) ? new ValidationResult(true, "") : new ValidationResult(false, Error.ERROR_INVALID_QUANTITY),
                            "Enter new quantity: "
                    );
                    updatedProductCopy.setQuantity(newQty);
                    System.out.println(Error.SUCCESS_QUANTITY_UPDATED);
                    TableConfig.displayProductTable(List.of(updatedProductCopy), 0, 1, 1, 1);
                    break;

                case 3: // Update price
                    String newPrice = Validation.getValidatedInput(
                            scanner::nextLine,
                            value -> isValidPrice(value) ? new ValidationResult(true, "") : new ValidationResult(false, Error.ERROR_INVALID_PRICE),
                            "Enter new price: "
                    );
                    updatedProductCopy.setPrice(newPrice);
                    System.out.println(Error.SUCCESS_PRICE_UPDATED);
                    TableConfig.displayProductTable(List.of(updatedProductCopy), 0, 1, 1, 1);
                    break;

                case 4: // Update all fields
                    String newNameAll = Validation.getValidatedInput(
                            scanner::nextLine,
                            value -> isValidName(value) ? new ValidationResult(true, "") : new ValidationResult(false, Error.ERROR_INVALID_NAME),
                            "Enter new name: "
                    );
                    updatedProductCopy.setName(newNameAll);

                    String newQtyAll = Validation.getValidatedInput(
                            scanner::nextLine,
                            value -> isValidQuantity(value) ? new ValidationResult(true, "") : new ValidationResult(false, Error.ERROR_INVALID_QUANTITY),
                            "Enter new quantity: "
                    );
                    updatedProductCopy.setQuantity(newQtyAll);

                    String newPriceAll = Validation.getValidatedInput(
                            scanner::nextLine,
                            value -> isValidPrice(value) ? new ValidationResult(true, "") : new ValidationResult(false, Error.ERROR_INVALID_PRICE),
                            "Enter new price: "
                    );
                    updatedProductCopy.setPrice(newPriceAll);

                    System.out.println(Error.SUCCESS_ALL_FIELDS_UPDATED);
                    TableConfig.displayProductTable(List.of(updatedProductCopy), 0, 1, 1, 1);
                    break;

                case 5: // Exit
                    System.out.println(Error.SUCCESS_EXIT);
                    return; // Exit the method without saving changes

                default:
                    System.out.println(Error.ERROR_INVALID_CHOICE);
            }

            // Add the updated copy to the updatedProduct list
            updatedProduct.add(updatedProductCopy);
            System.out.println("Product updated temporarily. Use 'saveProduct' to save changes.");
            return; // Exit the method after updating
        }
    }

    @Override
    public void saveProduct() {
        System.out.println(Color.GREEN + " 'si' " + Color.RESET + "for saving inserted products and" + Color.GREEN + " 'uu' " + Color.RESET + "for saving updated products or" + Color.RED + " 'b' " + Color.RESET);
        System.out.println("Choose an option:");


        String saveInput = getValidatedInput(
                scanner::nextLine,
                value -> {
                    if (value.isEmpty()) {
                        return new ValidationResult(false, "Input cannot be empty!");
                    } else if (!value.matches("^[a-zA-Z]+$")) {
                        return new ValidationResult(false, "Invalid input! Only characters are allowed.");
                    }
                    return new ValidationResult(true, "");
                },
                "\nEnter your option: "
        );

        switch (saveInput.toLowerCase()) {
            case "si" -> {
                if (insertedProduct.isEmpty()) {
                    System.out.println("No inserted products to save.");
                } else {
                    products.addAll(insertedProduct); // Save inserted products
                    insertedProduct.clear(); // Clear the temporary list
                    System.out.println("Inserted products saved successfully.");
                }
            }
            case "su" -> {
                if (updatedProduct.isEmpty()) {
                    System.out.println("No updated products to save.");
                } else {
                    // Update the main products list with changes
                    for (Product updated : updatedProduct) {
                        products.removeIf(p -> p.getId().equals(updated.getId())); // Remove old version
                        products.add(updated); // Add updated version
                    }
                    updatedProduct.clear(); // Clear the temporary list
                    System.out.println("Updated products saved successfully.");
                }
            }
            default ->
                    System.out.println(Color.RED + "❌ Invalid option! Please enter a valid option ('si', 'su')! Try again." + Color.RESET);
        }
    }

    @Override
    public void unsavedProduct() {
        System.out.println(Color.GREEN + " 'ui' " + Color.RESET + "for see the  inserted products and" + Color.GREEN + " 'uu' " + Color.RESET + "for see the updated products or" + Color.RED + " 'b' " + Color.RESET);

        while (true) {
            String unsavedInput = getValidatedInput(
                    scanner::nextLine,
                    value -> {
                        if (value.isEmpty()) {
                            return new ValidationResult(false, "Input cannot be empty!");
                        } else if (!value.matches("^[a-zA-Z]+$")) {
                            return new ValidationResult(false, "Invalid input! Only characters are allowed.");
                        }
                        return new ValidationResult(true, "");
                    },
                    "\nEnter your option: "
            );

            switch (unsavedInput.toLowerCase()) {
                case "ui" -> {
                    if (insertedProduct.isEmpty()) {
                        System.out.println("No inserted products to display. Showing empty table.");
                        TableConfig.displayUnsaveProductTable(new ArrayList<>()); // Display empty table
                    } else {
                        TableConfig.displayUnsaveProductTable(insertedProduct);
                    }
                }
                case "uu" -> {
                    if (updatedProduct.isEmpty()) {
                        System.out.println("No updated products to display. Showing empty table.");
                        TableConfig.displayUnsaveProductTable(new ArrayList<>()); // Display empty table
                    } else {
                        TableConfig.displayUnsaveProductTable(updatedProduct);
                    }
                }
                case "b" -> {
                    return;
                }
                default ->
                        System.out.println(Color.RED + "❌ Invalid option! Please enter a valid option ('ui', 'uu', 'b')! Try again." + Color.RESET);
            }

            System.out.println(Color.YELLOW + "Press Enter to continue..." + Color.RESET);
            scanner.nextLine();
        }
    }

    //Bonus point
    @Override
    public void backUpDatabase() {
    }

    @Override
    public void restoreDatabase() {
    }
}
