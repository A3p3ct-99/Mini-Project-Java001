package org.example.constant;

public class Error {

    public static String ERROR_OPTION_EMPTY = "Option cannot be empty!";
    public static String ERROR_INVALID_OPTION = "Option not found!";

    //update method

    public static final String ERROR_OPTION_NOT_FOUND = "Option not found!";
    public static final String ERROR_OPTION_INVALID = "Invalid input. Please enter only Y or N.";

    // General errors
    public static final String ERROR_NO_PRODUCTS = "No products available to update.";
    public static final String ERROR_PRODUCT_NOT_FOUND = "Product with ID %s not found.";
    public static final String ERROR_INVALID_CHOICE = "Invalid choice. Please enter a number between 1 and 5.";
    public static final String ERROR_INVALID_INPUT = "Invalid input. Please enter a number between 1 and 5.";

    // Validation errors
    public static final String ERROR_INVALID_NAME = "Invalid name. Name must start with a letter and can contain letters, numbers, and spaces.";
    public static final String ERROR_INVALID_QUANTITY = "Invalid quantity. Quantity must be a positive integer.";
    public static final String ERROR_INVALID_PRICE = "Invalid price. Price must be a positive number (e.g., 1.99 or 2).";

    // Success messages
    public static final String SUCCESS_NAME_UPDATED = "Name updated successfully.";
    public static final String SUCCESS_QUANTITY_UPDATED = "Quantity updated successfully.";
    public static final String SUCCESS_PRICE_UPDATED = "Price updated successfully.";
    public static final String SUCCESS_ALL_FIELDS_UPDATED = "All fields updated successfully.";
    public static final String SUCCESS_EXIT = "Exiting update menu.";

    // save method
    public static final String ERROR_PRODUCT_ADD_NOT_FOUND = "No products available to add.";
}
