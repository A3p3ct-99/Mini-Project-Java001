package org.example.constant;


import static org.example.constant.Color.*;

public class Config {

    public static final String DB_URL = "jdbc:postgresql://localhost:5432/" + System.getenv("DB_NAME");
    public static final String TABLE_NAME = "products";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String REGEX_TABLE_OPTION = "^(?i)(N|P|F|L|G|W|R|U|D|S|Se|sa|Un|Ba|Re|E)$";
    public static final String REGEX_PRODUCT_ID = "[1-9][0-9]*";
    public static final String REGEX_LETTERS_NUMBERS_SPACES = "[a-zA-Z0-9\\s]+";
//    public static final String  REGEX_HAVE_SYMBOLS= "^[a-zA-Z0-9\\s]+$";


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

    public static void printFooterTable() {
        System.out.println("\t".repeat(8) + "----------- Menu -----------");
        System.out.printf("\t" + "   %sN.%s Next Page      %sP.%s Previous Page      %sF.%s First Page      %sL.%s Last Page      %sG.%s Goto\n", LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET);
        System.out.println();
        System.out.printf("%sW)%s Write        %sR)%s Read          %sU)%s Update        %sD)%s Delete        %sS)%s Search (name)        %sSe)%s Set rows\n", LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET);
        System.out.printf("%ssa)%s Save        %sUn)%s Unsaved      %sBa)%s Backup       %sRe)%s Restore      %sE)%s Exit\n", LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET, LIGHT_GREEN, RESET);
        System.out.println("\t".repeat(8) + "----------------------------");
    }

    public static void printTable(String id, String name, String price, String quantity, String date){
        System.out.printf("| %-5s | %-20s | %-10s | %-10s | %-10s |\n", id, name, price, quantity, date);
    }

    public static void printError(String message){
        System.out.println("\n" + RED + "❌ " + message + RESET);
    }

    public static void printSuccess(String message){
        System.out.println("\n" + GREEN + "✅ " + message + RESET);
    }
}
