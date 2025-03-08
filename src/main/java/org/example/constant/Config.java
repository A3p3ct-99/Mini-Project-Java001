package org.example.constant;


import static org.example.constant.Color.*;

public class Config {

    public static final String DB_URL = "jdbc:postgresql://localhost:5432/" + System.getenv("DB_NAME");

    // SQL Queries
    public static final String QUERY_UPDATE = "UPDATE products SET name = ?, price = ?, quantity = ?, date = ? WHERE id = ?";
    public static final String QUERY_SELECT_ALL = "SELECT * FROM products ORDER BY id";
    public static final String QUERY_INSERT = "INSERT INTO products (name, price, quantity, date) VALUES (?, ?, ?, ?)";
    public static final String QUERY_DELETE = "DELETE FROM products WHERE id = ?";


    public static final String DATE_FORMAT = "yyyy-MM-dd";

    //Regex
    public static final String REGEX_TABLE_OPTION = "^(?i)(N|P|F|L|G|W|R|U|D|S|Se|sa|Un|Ba|Re|E)$";
    public static final String REGEX_PRODUCT_ID = "[1-9][0-9]*";
    public static final String REGEX_PRODUCT_QUANTITY = "^\\d{1,8}$";
    public static final String REGEX_NUMBER = "^[0-9]+$";


    public static final String REGEX_DELETE_ID = "[1-9][0-9]*";


    //Enter Message
    public static final String ENTER_OPTION = "Choose an option(): ";
    public static final String ENTER_PRODUCT_NAME = "Enter product name: ";
    public static final String ENTER_PRODUCT_ID = "Enter product ID: ";
    public static final String ENTER_PRODUCT_PRICE = "Enter product price: ";
    public static final String ENTER_PRODUCT_QUANTITY = "Enter product quantity: ";
    public static final String ENTER_PRODUCT_DATE = "Enter product date: ";
    public static final String ENTER_SEARCH_NAME = "Enter product name to search: ";
    public static final String ENTER_ROWS = "Enter rows to show: ";
    public static final String ENTER_PAGE_NUMBER = "Enter page number: ";
    public static final String ENTER_CONTINUE = "Press Enter to continue...";
    public static final String ENTER_ID_SEARCH = "Enter ID to search: ";

    public static void printError(String message){
        System.out.println("\n" + RED + "❌ " + message + RESET);
    }

    public static void printSuccess(String message){
        System.out.println("\n" + GREEN + "✅ " + message + RESET);
    }
}
