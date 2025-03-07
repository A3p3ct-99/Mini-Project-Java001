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
            System.out.println("No products available to update.");
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
            System.out.println("Product with ID " + productId + " not found.");
            return;
        }

        while (true) {
            System.out.println("Select the field to update:");
            System.out.println("1. Name 2. Quantity 3. Price 4. All fields 5.Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.println("Enter new name:");
                    String newName = scanner.nextLine();
                    productToUpdate.setName(newName);
                    System.out.println("Name updated successfully.");
                    displayProductTable(productToUpdate); // Display updated product in table format
                    break;
                case 2:
                    System.out.println("Enter new quantity:");
                    String newQty = scanner.nextLine();
                    if (isValidQuantity(newQty)) {
                        productToUpdate.setQuantity(newQty);
                        System.out.println("Quantity updated successfully.");
                        displayProductTable(productToUpdate); // Display updated product in table format
                    } else {
                        System.out.println("Invalid quantity. Quantity must be a positive integer.");
                    }
                    break;
                case 3:
                    System.out.println("Enter new price:");
                    String newPrice = scanner.nextLine();
                    if (isValidPrice(newPrice)) {
                        productToUpdate.setPrice(newPrice);
                        System.out.println("Price updated successfully.");
                        displayProductTable(productToUpdate); // Display updated product in table format
                    } else {
                        System.out.println("Invalid price. Price must be a positive number (e.g., 1.99 or 2).");
                    }
                    break;
                case 4:
                    System.out.println("Enter new name:");
                    String newNameAll = scanner.nextLine();
                    System.out.println("Enter new quantity:");
                    String newQtyAll = scanner.nextLine();
                    System.out.println("Enter new price:");
                    String newPriceAll = scanner.nextLine();
                    System.out.println("Enter new date:");
                    String newDateAll = scanner.nextLine();

                    if (isValidQuantity(newQtyAll) && isValidPrice(newPriceAll)) {
                        productToUpdate.setName(newNameAll);
                        productToUpdate.setQuantity(newQtyAll);
                        productToUpdate.setPrice(newPriceAll);
                        productToUpdate.setDate(newDateAll);
                        System.out.println("All fields updated successfully.");
                        displayProductTable(productToUpdate); // Display updated product in table format
                    } else {
                        System.out.println("Invalid quantity or price. Quantity must be a positive integer, and price must be a positive number.");
                    }
                    break;
                case 5:
                    System.out.println("Exiting update menu.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
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
