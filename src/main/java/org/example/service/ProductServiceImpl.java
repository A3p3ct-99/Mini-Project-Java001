package org.example.service;
import org.example.utils.ProductUtils;
import org.example.view.ProductView;
import org.example.dao.ProductDAOImpl;
import org.example.dto.Product;
import org.example.model.ProductEntity;
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
import static org.example.constant.Color.LIGHT_GREEN;
import static org.example.constant.Config.*;
import static org.example.constant.TableConfig.displayProductTable;
import static org.example.constant.Validation.getValidatedInput;


public class ProductServiceImpl implements ProductService {

    Scanner scanner = new Scanner(System.in);
    List<Product> products = new ArrayList<>();
    ProductDAOImpl productDAO = new ProductDAOImpl();

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
    public void searchProduct(String name) {
        var productsEntity = productDAO.getProductsContainNameIgnoreCase(name);
        if(productsEntity.isEmpty())
        {
            System.out.println(RED+"❌ Product is  not found"+ RESET);
        } else {
            var products = productsEntity.filter(productEntities -> !productEntities.isEmpty())
                    .map(productEntities -> productEntities.stream().map(ProductUtils::getProductFromDatabase).toList())
                    .orElseGet(ArrayList::new);
            displayProductTable(products, 0, products.size(), 1, 1);
        }
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
