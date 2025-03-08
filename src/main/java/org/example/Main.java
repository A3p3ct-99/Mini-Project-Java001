package org.example;
import org.example.view.ProductView;
import org.example.service.ProductService;
import org.example.service.ProductServiceImpl;

public class Main {
    public static void main(String[] args) {
        ProductService service = new ProductServiceImpl();
        ProductView productView = new ProductView(service);
        productView.start();

    }
}
