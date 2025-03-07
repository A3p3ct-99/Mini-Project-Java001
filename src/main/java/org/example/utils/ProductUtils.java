package org.example.utils;


import org.example.dto.Product;
import org.example.model.ProductEntity;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.example.constant.Config.DATE_FORMAT;

public class ProductUtils {

    public static ProductEntity createProductEntity(String id, String name, String price, String quantity, String date) {

        return new ProductEntity(
                Integer.parseInt(id),
                name,
                Double.parseDouble(price),
                Integer.parseInt(quantity),
                LocalDate.parse(date)
        );
    }

    public static Product getProductFromDatabase(ProductEntity entity) {
        return new Product(
                String.valueOf(entity.getId()),
                entity.getName(),
                String.format("$%.2f", entity.getPrice()),
                String.valueOf(entity.getQuantity()),
                entity.getDate().format(ofPattern(DATE_FORMAT))
        );
    }
}
