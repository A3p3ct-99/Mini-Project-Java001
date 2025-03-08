package org.example.utils;


import org.example.dto.Product;
import org.example.model.ProductEntity;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.example.constant.Config.DATE_FORMAT;

public class ProductUtils {

    public static ProductEntity createProductEntity(String id, String name, String price, String quantity, String date) {
        String removePrice = price.replace("$", "");
        return new ProductEntity(
                Integer.parseInt(id),
                name,
                Double.parseDouble(removePrice),
                Integer.parseInt(quantity),
                LocalDate.parse(date)
        );
    }

    public static Product getProductFromDatabase(ProductEntity entity) {
        return new Product(
                String.valueOf(entity.getId()),
                entity.getName(),
                String.format("%.2f", entity.getPrice()),
                String.valueOf(entity.getQuantity()),
                entity.getDate().format(ofPattern(DATE_FORMAT))
        );
    }

    public static List<Product> readProductsFromFile(String fileName) {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Product product = new Product(data[0], data[1], data[2], data[3], data[4]);
                products.add(product);
            }
        } catch (IOException e) {
            System.out.println();
        }
        return products;
    }

    public static void writeProductToFile(Product product, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(String.join(",", product.getId(), product.getName(), product.getPrice(), product.getQuantity(), product.getDate()));
            writer.newLine();
            System.out.println("Product successfully saved!");
        } catch (IOException e) {
            System.out.println();
        }
    }

    public static void updateProductToFile(Product product, String fileName) {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Product p = new Product(data[0], data[1], data[2], data[3], data[4]);
                products.add(p);
            }
        } catch (IOException e) {
            System.out.println("File not found, creating a new one.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            boolean productUpdated = false;
            for (Product p : products) {
                if (p.getId().equals(product.getId())) {
                    writer.write(String.join(",", product.getId(), product.getName(), product.getPrice(), product.getQuantity(), product.getDate()));
                    productUpdated = true;
                } else {
                    writer.write(String.join(",", p.getId(), p.getName(), p.getPrice(), p.getQuantity(), p.getDate()));
                }
                writer.newLine();
            }
            if (!productUpdated) {
                writer.write(String.join(",", product.getId(), product.getName(), product.getPrice(), product.getQuantity(), product.getDate()));
                writer.newLine();
            }
            System.out.println("Product successfully updated!");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void clearProductFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("");
        } catch (IOException e) {
            System.out.println();
        }
    }
}
