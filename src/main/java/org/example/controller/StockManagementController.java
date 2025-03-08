package org.example.controller;


import org.example.dto.Product;
import org.example.functional.Command;
import org.example.model.impl.ProductModelImplement;
import org.example.dao.ProductDAO;
import org.example.utils.ProductUtils;
import org.example.validation.ValidationResult;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.Math.floor;
import static org.example.constant.Color.RESET;
import static org.example.constant.Color.YELLOW;
import static org.example.constant.Config.*;
import static org.example.constant.TableConfig.displayProductTable;
import static org.example.constant.Validation.getValidatedInput;
import static org.example.constant.Validation.validateMenuOption;

public class StockManagementController {

    int totalPage = 1;
    int currentPage = 1;

    private final ProductDAO stockService;
    private ProductModelImplement productView;

    Scanner scanner = new Scanner(System.in);
    private final HashMap<String, Command> commands = new HashMap<>();

    public StockManagementController(ProductDAO stockService) {
        this.productView = new ProductModelImplement();
        this.stockService = stockService;

        commands.put("n", this::nextPage);
        commands.put("p", this::previousPage);
        commands.put("f", this::firstPage);
        commands.put("l", this::lastPage);
        commands.put("g", this::gotoPage);
        commands.put("w", this::insertProduct);
        commands.put("u", this::updateProduct);
        commands.put("r", this::displayProduct);
        commands.put("d", this::deleteProduct);
        commands.put("s", this::searchProduct);
        commands.put("se", this::setRowTable);
        commands.put("sa", this::saveProduct);
        commands.put("un", this::unsavedProduct);
        commands.put("ba", this::backUpDatabase);
    }

    public void start() {
        menu();
        scanner.close();
    }

    private void menu() {
        var productsEntity = productView.getAllProducts();
        List<Product> products = productsEntity.stream().map(ProductUtils::getProductFromDatabase).toList();
        handlePagination(products);
    }

    private static int lastUsedId = 0;

    private void insertProduct() {
        System.out.println("=> Choose an option() : w");
        lastUsedId++; // Increment the ID
        String id = String.valueOf(lastUsedId);
        System.out.println("ID: " + id);


        String name = getValidatedInput(
                () -> {
                    System.out.print("Input Product Name: ");
                    return scanner.nextLine().trim();
                },
                value -> {
                    if (value.isEmpty()) {
                        return new ValidationResult(false, "Product name cannot be empty.");
                    }
                    if (!value.matches("^[a-zA-Z0-9 ]+$")) {
                        return new ValidationResult(false, "Invalid input. Allow only letters and numbers!");
                    }
                    return new ValidationResult(true, "");
                },
                "Input Product Name: "
        );

        String price = getValidatedInput(
                () -> {
                    System.out.print("Enter price: ");
                    return scanner.nextLine().trim();
                },
                value -> {
                    if (value.isEmpty()) {
                        return new ValidationResult(false, "Price not allowed to be empty.");
                    }
                    if (!value.matches("^\\d+(\\.\\d{1,2})?$")) {
                        return new ValidationResult(false, "Invalid input. Allow only positive numbers!");
                    }
                    return new ValidationResult(true, "");
                },
                "Enter price: "
        );

        String quantity = getValidatedInput(
                () -> {
                    System.out.print("Enter quantity: ");
                    return scanner.nextLine().trim();
                },
                value -> {
                    if (!value.matches("^\\d{1,8}$")) {
                        return new ValidationResult(false, "Invalid input. Allow only numbers up to 8 digits.");
                    }
                    return new ValidationResult(true, "");
                },
                "Enter quantity: "
        );


        String date = java.time.LocalDate.now().toString();


        Product newProduct = new Product(id, name, price, quantity, date);


        stockService.writeProduct(newProduct);


        System.out.println("Enter to continue.....");
        scanner.nextLine();
        menu();
    }
    private String getValidatedInput(Supplier<String> inputSupplier, Function<String, ValidationResult> validator, String prompt) {
        while (true) {
            String input = inputSupplier.get();
            ValidationResult result = validator.apply(input);
            if (result.isValid()) {
                return input;
            }
            System.out.println("Invalid input. " + result.getErrorMessage());
        }
    }





    private void updateProduct() {
        stockService.updateProduct();
    }

    private void displayProduct() {
        stockService.readProduct();
    }

    private void deleteProduct() {
        stockService.deleteProduct();
    }

    private void searchProduct() {
        stockService.searchProduct();
    }

    private void setRowTable() {
        stockService.setRowTable();
        menu();
    }

    private void saveProduct() {
        stockService.saveProduct();
    }

    private void unsavedProduct() {
        stockService.unsavedProduct();
    }

    private void backUpDatabase() {
        stockService.backUpDatabase();
    }

    private void restoreDatabase() {
        stockService.restoreDatabase();
    }

    private void exit() {
        System.exit(0);
    }

    private void handlePagination(List<Product> products) {
        int rowTable = Integer.parseInt(getRowTable());
        totalPage = (int) Math.ceil((double) products.size() / rowTable);
        if (currentPage > totalPage) currentPage = totalPage;
        int start = (currentPage - 1) * rowTable;
        int end = Math.min(currentPage * rowTable, products.size());
        displayProductTable(products, start, end, totalPage, currentPage);
        printFooterTable();
        System.out.print(YELLOW + ENTER_OPTION + RESET);
        String option = scanner.nextLine();
        if (option.equalsIgnoreCase("e")) exit();
        if (!validateMenuOption(option)) menu();
        commands.getOrDefault(option, this::restoreDatabase).execute();
    }

    private void nextPage() {
        if (currentPage < totalPage) {
            currentPage++;
        }
        menu();
    }

    private void previousPage() {
        if (currentPage > 1) {
            currentPage--;
        }
        menu();
    }

    private void firstPage() {
        currentPage = 1;
        menu();
    }

    private void lastPage() {
        currentPage = totalPage;
        menu();
    }

    private void gotoPage() {
        String input = getValidatedInput(
                scanner::nextLine,
                value -> {
                    if (!value.matches("\\d+")) {
                        return new ValidationResult(false, "Invalid input, please enter a number");
                    }
                    return new ValidationResult(true, "");
                },
                ENTER_PAGE_NUMBER
        );
        int page = Integer.parseInt(input);
        if (page < 1 || page > totalPage) {
            System.out.println("Invalid page number, please enter a number between 1 and " + totalPage);
            gotoPage();
        } else {
            currentPage = page;
            menu();
        }
    }

    private String getRowTable() {
        try (BufferedReader reader = new BufferedReader(new FileReader("rowTable.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                return line;
            }
        } catch (IOException e) {
            System.out.println("An error occurred " + e.getMessage());
        }
        return "3";
    }
}