package org.example.constant;


import static org.example.constant.Color.*;

public class Config {

    public static final String DB_URL = "jdbc:postgresql://localhost:5432/" + System.getenv("DB_NAME");
    public static final String INSERT_PRODUCT_FILE_NAME = "insert_products.csv";
    public static final String UPDATE_PRODUCT_FILE_NAME = "update_products.csv";
    public static final String BACKUP_FILE_NAME = "backup.sql";

    // SQL Queries
    public static final String QUERY_SELECT_ALL = "SELECT * FROM products ORDER BY id";
    public static final String QUERY_UPDATE = "UPDATE products SET product_name = ?, unit_price = ?, quantity = ?, created_at = ? WHERE id = ?";
    public static final String QUERY_INSERT = "INSERT INTO products (id, product_name, unit_price, quantity, created_at) VALUES (?, ?, ?, ?, ?)";

    public static final String QUERY_DELETE = "DELETE FROM products WHERE id = ?";
    public static final String QUERY_SEARCH_CONTAIN_NAME = "SELECT * FROM products WHERE product_name ILIKE ?";


    public static final String DATE_FORMAT = "yyyy-MM-dd";

    //Regex
    public static final String REGEX_TABLE_OPTION = "^(?i)(N|P|F|L|G|W|R|U|D|S|Se|sa|Un|Ba|Re|E)$";
    public static final String REGEX_PRODUCT_ID = "[1-9][0-9]*";
    public static final String REGEX_LETTERS_NUMBERS_SPACES = "[a-zA-Z0-9\\s]+";
    public static final String REGEX_PRODUCT_QUANTITY = "^\\d{1,8}$";
    public static final String REGEX_NUMBER = "[0-9]+";
    public static final String REGEX_DELETE_ID = "[1-9][0-9]*";
    public static final String REGEX_SEARCH_NAME = "[a-zA-Z0-9\\s]+";
    public static final String REGEX_PAGE_NUMBER = "[1-9][0-9]*";
    public static final String REGEX_ROWS = "[1-9][0-9]*";
    public static final String REGEX_CHOICE_UPDATE = "[1-5]";


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
    public static final String ENTER_CHOICE_UPDATE = "Choose an option to update: ";

    public static void printError(String message){
        System.out.println("\n" + RED + "❌ " + message + RESET);
    }

    public static void printSuccess(String message){
        System.out.println("\n" + GREEN + "✅ " + message + RESET);
    }
}
