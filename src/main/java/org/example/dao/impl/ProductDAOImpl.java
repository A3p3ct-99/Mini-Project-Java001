package org.example.dao.impl;
import org.example.controller.StockManagementController;
import org.example.dao.ProductDAO;
import org.example.dto.Product;
import org.example.model.ProductEntity;
import org.example.model.impl.ProductModelImplement;
import org.example.validation.ValidationResult;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.example.constant.Color.*;
import static org.example.constant.Color.LIGHT_GREEN;
import static org.example.constant.Config.*;
import static org.example.constant.Validation.getValidatedInput;


public class ProductDAOImpl implements ProductDAO {

    Scanner scanner = new Scanner(System.in);
    List<Product> products = new ArrayList<>();
    ProductModelImplement productModel = new ProductModelImplement();

    @Override
    public void writeProduct() {

    }

    @Override
    public void readProduct() {

    }

    @Override
    public void updateProduct() {

    }

    @Override
    public void deleteProduct() {

    }

    @Override
    public void searchProduct() {
        List<ProductEntity> products1 = productModel.getAllProducts();
        String name = getValidatedInput(
                scanner::nextLine,
                value -> {
                    if (value.isEmpty()) {
                        return new ValidationResult(false, "ERROR_INVALID_PRODUCT_NAME_EMPTY");
                    } else if (!value.matches(REGEX_LETTERS_NUMBERS_SPACES)) {
                        return new ValidationResult(false, "ERROR_INVALID_PRODUCT_NAME_INVALID");
                    }
                    else {
                        return new ValidationResult(true, "");
                    }
                },
                ENTER_PRODUCT_NAME
        );
        ProductEntity product = products1.stream()
                .filter(productEntity -> productEntity.getName().toLowerCase().equals(name.toLowerCase().trim()) )
                .findFirst()
                .orElse(null);
        if(product != null)
        {
            printSingleTable(product);
        }else {
            System.out.println(RED+"❌ Product is  not found"+ RESET);
        }
        ProductDAO service = new ProductDAOImpl();
        StockManagementController stockManagementController = new StockManagementController(service);
        stockManagementController.start();

    }
    public void printSingleTable(ProductEntity  productEntity)
    {
        Table table = new Table(5, BorderStyle.UNICODE_ROUND_BOX_WIDE, ShownBorders.ALL);
        CellStyle style = new CellStyle(CellStyle.HorizontalAlign.CENTER);

        table.addCell(LIGHT_YELLOW + "ID", style);
        table.addCell(LIGHT_BLUE +  "Name" + RESET, style);
        table.addCell(LIGHT_CYAN +  "Unit Price", style);
        table.addCell(LIGHT_PURPLE + "Quantity", style);
        table.addCell(LIGHT_GREEN + "Imported Date", style);
        //set width column table
        table.setColumnWidth(0, 10, 10);
        table.setColumnWidth(1, 35, 35);
        table.setColumnWidth(2, 15, 15);
        table.setColumnWidth(3, 10, 10);
        table.setColumnWidth(4, 15, 15);
        table.addCell(LIGHT_GREEN + productEntity.getId(), style);
        table.addCell(CYAN + ITALIC + productEntity.getName() , style);
        table.addCell(RED + ITALIC + productEntity.getPrice(), style);
        table.addCell(BLUE + productEntity.getQuantity(), style);
        table.addCell(PINK + productEntity.getDate(), style);
        System.out.println(table.render());
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
