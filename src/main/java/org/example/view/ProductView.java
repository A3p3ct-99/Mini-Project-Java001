package org.example.view;


import org.example.dto.Product;
import org.example.functional.Command;
import org.example.model.ProductEntity;
import org.example.service.ProductService;
import org.example.utils.ProductUtils;
import org.example.validation.ValidationResult;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static org.example.constant.Color.*;
import static org.example.constant.Color.RED;
import static org.example.constant.Config.*;
import static org.example.constant.Error.*;
import static org.example.constant.TableConfig.*;
import static org.example.constant.Validation.getValidatedInput;
import static org.example.constant.Validation.validateMenuOption;

public class ProductView {

    private int totalPage = 1;
    private int currentPage = 1;
    private static int lastId = 0;
    Scanner scanner = new Scanner(System.in);
    private final ProductService productService;
    private final HashMap<String, Command> commands = new HashMap<>();

    public ProductView(ProductService productService) {
        this.productService = productService;
        lastId = getLastId();

        commands.put("n", this::nextPage);
        commands.put("p", this::previousPage);
        commands.put("f", this::firstPage);
        commands.put("l", this::lastPage);
        commands.put("g", this::gotoPage);
        commands.put("w", this::insertProduct);
        commands.put("u", this::updateProduct);
        commands.put("r", this::displayProductById);
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

    public void menu() {
        List<Product> products = productService.getAllProducts();
        handlePagination(products);
    }

    private void insertProduct() {
        String id = String.valueOf(++lastId);
        System.out.println("ID: " + id);

        String name = getValidatedInput(
                () -> scanner.nextLine().trim(),
                value -> {
                    if (value.isEmpty()) {
                        return new ValidationResult(false, "Product name cannot be empty.");
                    }
                    else if (!value.matches("^[a-zA-Z0-9 ]+$")) {
                        return new ValidationResult(false, "Invalid input. Allow only letters and numbers!");
                    }
                    return new ValidationResult(true, "");
                },
                ENTER_PRODUCT_NAME
        );

        String price = getValidatedInput(
                () -> scanner.nextLine().trim(),
                value -> {
                    if (value.isEmpty()) {
                        return new ValidationResult(false, "Price not allowed to be empty.");
                    }
                    else if (!value.matches("^\\d+(\\.\\d{1,2})?$")) {
                        return new ValidationResult(false, "Invalid input. Allow only positive numbers!");
                    }
                    return new ValidationResult(true, "");
                },
                ENTER_PRODUCT_PRICE
        );

        String quantity = getValidatedInput(
                () -> scanner.nextLine().trim(),
                value -> {
                    if (value.isEmpty()) {
                        return new ValidationResult(false, "Quantity not allowed to be empty.");
                    }
                    else if (!value.matches(REGEX_PRODUCT_QUANTITY)) {
                        return new ValidationResult(false, "Invalid input. Allow only numbers up to 8 digits.");
                    }
                    return new ValidationResult(true, "");
                },
                ENTER_PRODUCT_QUANTITY
        );

        String date = LocalDate.now().toString();

        Product newProduct = new Product(id, name, price, quantity, date);

        productService.writeProduct(newProduct);

        System.out.println(ENTER_CONTINUE);
        scanner.nextLine();
        menu();
    }

    private int getLastId() {
        List<Product> products = productService.getAllProducts();
        return products.stream()
                .map(Product::getId)
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;
    }

    public void updateProduct() {
        productService.updateProduct();
    }

    public void displayProductById() {
        String id = getValidatedInput(
                scanner::nextLine,
                value -> {
                    if (value.isEmpty()) {
                        return new ValidationResult(false, ERROR_INVALID_ID);
                    } else if (!value.matches("\\d+")) {
                        return new ValidationResult(false, ERROR_INVALID_INPUT);
                    } else if (value.matches("0")) {
                        return new ValidationResult(false, ERROR_INVALID_ID_GREATER_THAN_ZERO);
                    }
                    return new ValidationResult(true, "");
                },
                ENTER_ID_SEARCH
        );
        productService.readProduct(id);
        System.out.println(ENTER_CONTINUE);
        scanner.nextLine();
        menu();
    }

    public void deleteProduct() {
        String id = getValidatedInput(
                scanner::nextLine,
                value -> {
                    if(!value.matches(REGEX_DELETE_ID)){
                        return new ValidationResult(false,"INVALID ID! ZERO, LEADING ZEROS, CONTAINS NOT-DIGIT CHARACTER, EMPTY STRING, IS NOT ALLOWED  ");
                    }
                    return  new ValidationResult(true,"");
                },
                "Enter product ID to delete: "
        );

        List<Product> productEntityList = productService.getAllProducts();
        Product productDeleted = productEntityList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);

        if(productDeleted != null){
            displayProductByIdAndName(productDeleted);
            String confirmation = getValidatedInput(
                    scanner::nextLine,
                    value -> {
                        if (!value.matches("[yYnN]")) {
                            return new ValidationResult(false, "Invalid input, please enter Y or N");
                        }
                        return new ValidationResult(true, "");
                    },
                            "Are you sure want to deleted this product? (Y/N): "
            );
            if(confirmation.equals("y")){
                productService.deleteProduct(productDeleted.getId());
                System.out.println(GREEN + "Product deleted successfully ✅" + RESET);
            }else{
                System.out.println(YELLOW + "Deletion cancelled." + RESET);
            }
        }else {
            System.out.println(RED + "Product not found with ID: " + id + RESET);
        }
        menu();
    }

    public void searchProduct() {
        String name = getValidatedInput(
                scanner::nextLine,
                value -> {
                    if (value.isEmpty()) {
                        return new ValidationResult(false, ERROR_INVALID_PRODUCT_NAME_EMPTY);
                    } else if (!value.matches(REGEX_LETTERS_NUMBERS_SPACES)) {
                        return new ValidationResult(false, "ERROR_INVALID_PRODUCT_NAME_INVALID");
                    }
                    return new ValidationResult(true, "");
                },
                ENTER_PRODUCT_NAME
        );
        productService.searchProduct(name);
        getValidatedInput(
                scanner::nextLine,
                value -> new ValidationResult(true, ""),
                "Enter to continue..."
        );
        menu();
    }

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
        productService.setRowTable(numRows);
        menu();
    }

    private void saveProduct() {
        productService.saveProduct();
    }

    private void unsavedProduct() {
        productService.unsavedProduct();
    }

    private void backUpDatabase() {
        productService.backUpDatabase();
    }

    private void restoreDatabase() {
        productService.restoreDatabase();
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
