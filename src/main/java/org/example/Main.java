package org.example;
import org.example.controller.StockManagementController;
import org.example.dao.ProductDAO;
import org.example.dao.impl.ProductDAOImpl;

public class Main {
    public static void main(String[] args) {
        ProductDAO service = new ProductDAOImpl();
        StockManagementController stockManagementController = new StockManagementController(service);
        stockManagementController.start();


    }
}
