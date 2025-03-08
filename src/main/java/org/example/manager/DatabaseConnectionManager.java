package org.example.manager;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.example.constant.Config.DB_URL;

public class DatabaseConnectionManager {

    private static final String URL = DB_URL;
    private static final String USER = System.getenv("DB_USERNAME");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");
    private Connection connection;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
