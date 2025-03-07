package org.example.dao.impl;



import org.example.constant.Error;
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

        System.out.println("Enter the product ID to update:");
        String productId = scanner.nextLine(); // Read the product ID as a String

        Product productToUpdate = null;
        for (Product product : products) {
            if (product.getId().equals(productId)) { // Use .equals() for String comparison
                productToUpdate = product;
                break;
            }
        }

        if (productToUpdate == null) {
            System.out.println(String.format(Error.ERROR_PRODUCT_NOT_FOUND, productId));
            return;
        }

        while (true) {
            System.out.println("Select the field to update:");
            System.out.println("1. Name 2. Quantity 3. Price 4. All fields 5.Exit");

            int choice = 0;
            boolean validChoice = false;
            while (!validChoice) {
                try {
                    choice = scanner.nextInt(); // Read the menu choice
                    scanner.nextLine(); // Consume the newline character
                    if (choice >= 1 && choice <= 5) {
                        validChoice = true; // Valid choice
                    } else {
                        System.out.println(Error.ERROR_INVALID_CHOICE);
                    }
                } catch (java.util.InputMismatchException e) {
                    System.out.println(Error.ERROR_INVALID_INPUT);
                    scanner.nextLine(); // Clear the invalid input from the scanner buffer
                }
            }

            switch (choice) {
                case 1:
                    boolean retryName = true;
                    while (retryName) {
                        System.out.print("Enter new name: ");
                        String newName = scanner.nextLine();
                        if (isValidName(newName)) {
                            productToUpdate.setName(newName);
                            System.out.println(Error.SUCCESS_NAME_UPDATED);
                            displayProductTable(productToUpdate); // Display updated product in table format
                            retryName = false;
                        } else {
                            System.out.println(Error.ERROR_INVALID_NAME);
                            retryName = askToRetry(); // Ask if the user wants to retry
                        }
                    }
                    break;
                case 2:
                    boolean retryQty = true;
                    while (retryQty) {
                        System.out.print("Enter new quantity: ");
                        String newQty = scanner.nextLine();
                        if (isValidQuantity(newQty)) {
                            productToUpdate.setQuantity(newQty);
                            System.out.println(Error.SUCCESS_QUANTITY_UPDATED);
                            displayProductTable(productToUpdate); // Display updated product in table format
                            retryQty = false;
                        } else {
                            System.out.println(Error.ERROR_INVALID_QUANTITY);
                            retryQty = askToRetry(); // Ask if the user wants to retry
                        }
                    }
                    break;
                case 3:
                    boolean retryPrice = true;
                    while (retryPrice) {
                        System.out.print("Enter new price: ");
                        String newPrice = scanner.nextLine();
                        if (isValidPrice(newPrice)) {
                            productToUpdate.setPrice(newPrice);
                            System.out.println(Error.SUCCESS_PRICE_UPDATED);
                            displayProductTable(productToUpdate); // Display updated product in table format
                            retryPrice = false;
                        } else {
                            System.out.println(Error.ERROR_INVALID_PRICE);
                            retryPrice = askToRetry(); // Ask if the user wants to retry
                        }
                    }
                    break;
                case 4:
                    boolean retryNameAll = true;
                    while (retryNameAll) {
                        System.out.print("Enter new name: ");
                        String newNameAll = scanner.nextLine();
                        if (isValidName(newNameAll)) {
                            productToUpdate.setName(newNameAll);
                            retryNameAll = false;
                        } else {
                            System.out.println(Error.ERROR_INVALID_NAME);
                            retryNameAll = askToRetry(); // Ask if the user wants to retry
                        }
                    }

                    boolean retryQtyAll = true;
                    while (retryQtyAll) {
                        System.out.print("Enter new quantity: ");
                        String newQtyAll = scanner.nextLine();
                        if (isValidQuantity(newQtyAll)) {
                            productToUpdate.setQuantity(newQtyAll);
                            retryQtyAll = false;
                        } else {
                            System.out.println(Error.ERROR_INVALID_QUANTITY);
                            retryQtyAll = askToRetry(); // Ask if the user wants to retry
                        }
                    }

                    boolean retryPriceAll = true;
                    while (retryPriceAll) {
                        System.out.print("Enter new price: ");
                        String newPriceAll = scanner.nextLine();
                        if (isValidPrice(newPriceAll)) {
                            productToUpdate.setPrice(newPriceAll);
                            retryPriceAll = false;
                        } else {
                            System.out.println(Error.ERROR_INVALID_PRICE);
                            retryPriceAll = askToRetry(); // Ask if the user wants to retry
                        }
                    }

                    System.out.println(Error.SUCCESS_ALL_FIELDS_UPDATED);
                    displayProductTable(productToUpdate); // Display updated product in table format
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

    // Helper method to ask the user if they want to retry
    private boolean askToRetry() {
        while (true) {
            System.out.println("Do you want to try again? (Y/N):");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y")) {
                return true; // Retry
            } else if (input.equals("N")) {
                return false; // Stop
            } else {
                System.out.println(Error.ERROR_OPTION_INVALID); // Updated constant
            }
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
