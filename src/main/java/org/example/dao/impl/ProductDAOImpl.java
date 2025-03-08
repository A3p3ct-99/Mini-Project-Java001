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
    List<Product> insertedProduct = new ArrayList<>();
    List<Product> updatedProduct = new ArrayList<>();


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
        //Noted: Data will duplicate each time of this method reuse. So if merge it just remove the example data below and put the match collection
        //example inserted data
        insertedProduct.add(new Product("7", "Ipad", "450", "5", "12/04/2024"));
        insertedProduct.add(new Product("8", "Jacked", "50", "10", "12/04/2024"));
        //example updated data
        updatedProduct.add(new Product("7", "Iphone", "450", "5", "12/04/2024"));
        updatedProduct.add(new Product("8", "Jacked", "50", "20", "12/04/2024"));
        System.out.println(Color.GREEN+"'ui' "+Color.RESET + "for saving products and"+Color.GREEN+" 'uu' "+ Color.RESET+"for saving update products or"+Color.RED+" 'b' "+Color.RESET+"for back to menu");
        while (true){
            String unsavedInput = getValidatedInput(
                    scanner::nextLine,
                    value -> {
                        if(value.isEmpty()){
                            return new ValidationResult(false, "Input can not be empty!");
                        }
                        else if (!value.matches("^[a-zA-Z]+$")){
                            return new ValidationResult(false, "Invalid input! Allowed only characters!<");
                        }
                        return new ValidationResult(true, "");
                    },"\nEnter your option: "
            );
            switch (unsavedInput.toLowerCase()){
                case "ui" ->{
                    //products should be an insert product collection
                    TableConfig.displayUnsaveProductTable(insertedProduct);
                }
                case "uu" ->{
                    //products should be an updated product collection
                    TableConfig.displayUnsaveProductTable(updatedProduct);
                }
                case "b" ->{
                    return;
                }
                default -> {
                    System.out.println(Color.RED+"❌ Invalid option! Please enter a valid option ('ui', 'uu', 'b')! Try again."+Color.RESET);
                    continue;
                }
            }
            System.out.println(Color.YELLOW+ "Enter to continue..."+Color.RESET);
            scanner.nextLine();
            break;
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
