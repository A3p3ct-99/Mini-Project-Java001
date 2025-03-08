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
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

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
        // Parse the environment string
        String environmentStr = "DB_NAME=postgres;DB_PASSWORD=Bunath123;DB_USERNAME=postgres";
        Map<String, String> envVars = new HashMap<>();
        for (String env : environmentStr.split(";")) {
            String[] parts = env.split("=");
            if (parts.length == 2) {
                envVars.put(parts[0], parts[1]);
            }
        }
        String dbName = envVars.get("DB_NAME");
        String dbPassword = envVars.get("DB_PASSWORD");
        String dbUsername = envVars.get("DB_USERNAME");

        String option = getValidatedInput(
                scanner::nextLine,
                value -> {
                    if (!value.matches("[yYnN]")) {
                        return new ValidationResult(false, "Invalid input, please enter y or n");
                    } else {
                        return new ValidationResult(true, "");
                    }
                },
                "Are you sure you want to backup the database '" + dbName + "' (y/n)?: "
        );

        if (option.equalsIgnoreCase("y")) {
            System.out.println("Starting database backup...");
            try {
                // Generate a timestamp for unique filename
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
                String dateStr = now.format(formatter);
                // Create backup filename with timestamp
                String backupFilePath = "D:\\filebackup\\database_backup_" + dateStr + ".sql";

                ProcessBuilder pb = new ProcessBuilder(
                        "C:\\Program Files\\PostgreSQL\\15\\bin\\pg_dump.exe",
                        "--host", "localhost",
                        "--port", "5432",
                        "--username", dbUsername,
                        "--format", "custom",
                        "--file", backupFilePath,
                        dbName);

                // Set the password environment variable
                Map<String, String> env = pb.environment();
                env.put("PGPASSWORD", dbPassword);

                // Redirect error stream to be captured
                pb.redirectErrorStream(true);

                Process p = pb.start();

                // Capture the output to help diagnose the issue
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(p.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }

                int exitCode = p.waitFor();
                if (exitCode == 0) {
                    System.out.println(GREEN + "Database backup completed successfully " + backupFilePath+RESET);
                } else {
                    System.out.println("Database backup failed with exit code: " + exitCode);
                }

                // Clear the password from environment for security
                env.remove("PGPASSWORD");

            } catch (IOException | InterruptedException e) {
                System.out.println("Error during backup: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Database backup cancelled.");
        }
        ProductDAO service = new ProductDAOImpl();
        StockManagementController stockManagementController = new StockManagementController(service);
        stockManagementController.start();
    }

    @Override
    public void restoreDatabase() {
        // Parse the environment string (similar to the backup logic)
        String environmentStr = "DB_NAME=postgres;DB_PASSWORD=Bunath123;DB_USERNAME=postgres";
        Map<String, String> envVars = new HashMap<>();
        for (String env : environmentStr.split(";")) {
            String[] parts = env.split("=");
            if (parts.length == 2) {
                envVars.put(parts[0], parts[1]);
            }
        }
        String dbName = envVars.get("DB_NAME");
        String dbPassword = envVars.get("DB_PASSWORD");
        String dbUsername = envVars.get("DB_USERNAME");

        // Get the backup file path from user input (you can add validation here)
        String backupFilePath = getValidatedInput(
                scanner::nextLine,
                value -> {
                    if (value.isEmpty() || !new File(value).exists()) {
                        return new ValidationResult(false, "Invalid file path or file does not exist.");
                    } else {
                        return new ValidationResult(true, "");
                    }
                },
                "Enter the path of the backup file to restore: "
        );

        String option = getValidatedInput(
                scanner::nextLine,
                value -> {
                    if (!value.matches("[yYnN]")) {
                        return new ValidationResult(false, "Invalid input, please enter y or n");
                    } else {
                        return new ValidationResult(true, "");
                    }
                },
                "Are you sure you want to restore the database '" + dbName + "' from the file '" + backupFilePath + "' (y/n)?: "
        );

        if (option.equalsIgnoreCase("y")) {
            System.out.println("Starting database restore...");

            try {
                // Setup the process to restore the database using pg_restore
                ProcessBuilder pb = new ProcessBuilder(
                        "C:\\Program Files\\PostgreSQL\\15\\bin\\pg_restore.exe",
                        "--host", "localhost",
                        "--port", "5432",
                        "--username", dbUsername,
                        "--dbname", dbName,
                        "--clean", // Drop objects before restoring
                        "--if-exists", // Avoid errors if objects don't exist
                        "--no-password", // Avoid password prompt
                        backupFilePath);

                // Set the password environment variable
                Map<String, String> env = pb.environment();
                env.put("PGPASSWORD", dbPassword);

                // Redirect error stream to be captured
                pb.redirectErrorStream(true);

                Process p = pb.start();

                // Capture the output to help diagnose the issue
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(p.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }

                int exitCode = p.waitFor();
                if (exitCode == 0) {
                    System.out.println(GREEN + "Database restore completed successfully." + RESET);
                } else {
                    System.out.println("Database restore failed with exit code: " + exitCode);
                }

                // Clear the password from environment for security
                env.remove("PGPASSWORD");

            } catch (IOException | InterruptedException e) {
                System.out.println("Error during restore: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Database restore cancelled.");
        }

        ProductDAO service = new ProductDAOImpl();
        StockManagementController stockManagementController = new StockManagementController(service);
        stockManagementController.start();
    }



}
