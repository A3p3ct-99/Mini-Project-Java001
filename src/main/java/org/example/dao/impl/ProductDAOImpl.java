package org.example.dao.impl;



import org.example.constant.Validation;
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.example.constant.Color.*;
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
        ProductModelImplement productModelImplement = new ProductModelImplement();
        List<ProductEntity>  productEntityList = productModel.getAllProducts();
        String input = getValidatedInput(
                scanner::nextLine,
                value -> {
                    if(!value.matches(REGEX_DELETE_ID)){
                        return new ValidationResult(false,"INVALID ID! ZERO, LEADING ZEROS, CONTAINS NOT-DIGIT CHARACTER, EMPTY STRING, IS NOT ALLOWED  ");
                    }
                    return  new ValidationResult(true,"");
                    },
                "Enter product ID to delete: "
            );
            int productId = Integer.parseInt(input);
            ProductEntity productDeleted = productEntityList.stream().filter(
                            p-> p.getId() == productId)
                    .findFirst()
                    .orElse(null);

            if(productDeleted !=null){
                printSingleTable(productDeleted);
                System.out.print(YELLOW+"Are you sure want to deleted this product? (Y/N):"+RESET);
                String confirmation = scanner.nextLine().trim().toLowerCase();
                if(confirmation.equals("y")){
                    productEntityList.remove(productDeleted);
                    productModelImplement.deleteProduct(productDeleted.getId());
                    System.out.println(GREEN + "Product deleted successfully ✅" + RESET);
                }else{
                    System.out.println(YELLOW + "Deletion cancelled." + RESET);
                }
            }else {
                System.out.println(RED + "Product not found with ID: " + productId + RESET);
            }
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
