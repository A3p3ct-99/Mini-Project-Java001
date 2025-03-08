package org.example.dao.impl;



import org.example.constant.Color;
import org.example.constant.TableConfig;
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


public class    ProductDAOImpl implements ProductDAO {

    Scanner scanner = new Scanner(System.in);
    List<Product> products = new ArrayList<>();


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
        System.out.println(Color.GREEN+"'ui' "+Color.RESET + "for saving products and"+Color.GREEN+" 'uu' "+ Color.RESET+"for saving update products or"+Color.RED+" 'b' "+Color.RESET+"for back to menu");
        String unsavedInput = getValidatedInput(
                scanner::nextLine,
                value -> {
                    if (!value.matches("[a-zA-Z]")){
                        return new ValidationResult(false, "Invalid input! Allowed only characters!<");
                    }
                    return new ValidationResult(true, "");
                },"\nEnter your option: "
        );
        switch (unsavedInput){
            case "ui" ->{
                //products should be an insert product collection
                TableConfig.displayUnsaveProductTable(products);
            }
            case "un" ->{
                //products should be an updated product collection
                TableConfig.displayUnsaveProductTable(products);
            }
            default ->{}
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
