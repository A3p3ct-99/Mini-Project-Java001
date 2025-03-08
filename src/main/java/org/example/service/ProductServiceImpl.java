package org.example.service;



import org.example.constant.Color;
import org.example.constant.Error;
import org.example.dao.ProductDAO;
import org.example.dao.ProductDAOImpl;
import org.example.dto.Product;
import org.example.model.ProductEntity;
import org.example.utils.ProductUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.example.constant.Config.REGEX_TABLE_OPTION;
import static org.example.constant.Config.printError;
import static org.example.constant.Error.ERROR_INVALID_OPTION;
import static org.example.constant.Error.ERROR_OPTION_EMPTY;
import static org.example.constant.Validation.validateMenuOption;
import static org.example.utils.ProductUtils.createProductEntity;
import static org.example.utils.ProductUtils.writeProductToFile;


public class ProductServiceImpl implements ProductService {

    Scanner scanner = new Scanner(System.in);

    // i assume this one store from insert
    List<Product> products = new ArrayList<>();
    // i assume this one store from update
    List<Product> products2 = new ArrayList<>();

    ProductDAO productDAO = new ProductDAOImpl();

////     Constructor to initialize mock data
//    public ProductServiceImpl() {
//        // Adding 2 mock products to the list
//        products.add(new Product("11", "Keycap", "25.00", "100", "2023-10-01"));
//        products.add(new Product("12", "Switch Keyboard", "12.99", "24", "2023-09-15"));
////        products.add(new Product("13", "Keyboard Lube", "500.00", "30", "2023-08-20"));
//    }

    public ProductServiceImpl() {
        // Adding 2 mock products to the list
        products2.add(new Product("11", "Keycap update", "25.00", "100", "2023-10-01"));
        products2.add(new Product("12", "Switch Keyboard update", "12.99", "24", "2023-09-15"));
//        products.add(new Product("13", "Keyboard Lube", "500.00", "30", "2023-08-20"));
    }

    @Override
    public void writeProduct(Product product) {
        writeProductToFile(product);
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
    public void saveProduct() {
        if (products.isEmpty()) {
            System.out.println(Color.RED + Error.ERROR_PRODUCT_SAVE_ADD_NOT_FOUND + Color.RESET + "\n");
            return;
        }
        if(products2.isEmpty()){
            System.out.println(Color.RED + Error.ERROR_PRODUCT_SAVE_UPDATE_NOT_FOUND + Color.RESET + "\n");
            return;
        }
        while(true){
            String saveOption = validateOptionSave();
            switch (saveOption){
                case "si" -> {
                    List<ProductEntity> productsEntitiesInsert = new ArrayList<>();
                    for (Product product : products) {
                        ProductEntity productEntity = createProductEntity(
                                String.valueOf(product.getId()),  // assuming getId() returns an integer
                                product.getName(),
                                String.valueOf(product.getPrice()),  // assuming getPrice() returns a double
                                String.valueOf(product.getQuantity()),  // assuming getQuantity() returns an integer
                                product.getDate().toString()  // assuming getDate() returns a LocalDate
                        );
                        productsEntitiesInsert.add(productEntity);  // add the converted ProductEntity to the new list
                    }
                    for (ProductEntity product : productsEntitiesInsert) {
                        productDAO.addProduct(product);
                        System.out.println("product " + product.getId() + Color.GREEN +" successfully added." + Color.RESET);
                    }
                    System.out.println();
                    products.clear(); // clear product
                    productsEntitiesInsert.clear(); // clear product entity insert
                    return;
                }
                case "su" -> {
                    List<ProductEntity> productsEntitiesUpdate= new ArrayList<>();
                    // i assume that
                    for (Product product : products2) {
                        ProductEntity productEntity = createProductEntity(
                                String.valueOf(product.getId()),  // assuming getId() returns an integer
                                product.getName(),
                                String.valueOf(product.getPrice()),  // assuming getPrice() returns a double
                                String.valueOf(product.getQuantity()),  // assuming getQuantity() returns an integer
                                product.getDate().toString()  // assuming getDate() returns a LocalDate
                        );
                        productsEntitiesUpdate.add(productEntity);  // add the converted ProductEntity to the new list
                    }
                    for (ProductEntity product : productsEntitiesUpdate) {
                        productDAO.updateProduct(product);
                        System.out.println("product " + product.getId() + Color.GREEN +" successfully updated." + Color.RESET);
                    }
                    System.out.println();
                    products2.clear(); // clear product
                    productsEntitiesUpdate.clear(); // clear product entity insert
                    return;
                }
                case "b" -> {
                    return;
                }
                default -> System.out.println(Color.RED + "\nInvalid Option... Please try again!!!" + Color.RESET);
            }

        }
    }

    private String validateOptionSave(){
        while (true){
            System.out.println(Color.GREEN + "\t'si'" + Color.RESET + " for saving insert products and " + Color.GREEN + "'su' " + Color.RESET +
                    "for saving update products or " + Color.RED + "'b'" + Color.RESET + " for back to menu");
            System.out.print("Enter your option: ");
            String saveOption = scanner.nextLine().toLowerCase();
            if (saveOption.isBlank()) {
                printError(ERROR_OPTION_EMPTY);
            } else {
                return saveOption;
            }
        }
    }

    @Override
    public void unsavedProduct() {

    }

    @Override
    public void setRowTable(String numRows) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rowTable.txt"))) {
            writer.write(numRows);
        } catch (IOException e) {
            System.out.println("An error occurred " + e.getMessage());
        }
    }


    //Bonus point
    @Override
    public void backUpDatabase() {

    }

    @Override
    public void restoreDatabase() {

    }

    @Override
    public List<Product> getAllProducts() {
        var productsEntity = productDAO.getAllProducts();
        return productsEntity.stream().map(ProductUtils::getProductFromDatabase).toList();
    }
}
