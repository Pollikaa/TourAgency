package org.example.touragency.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private static final String username = "root";
    private static final String password = "@BaRt7788";
    private static final String url = "jdbc:mysql://localhost:3306/agency_travel1";

    private static DataBase instance; // єдиний екземпляр
    private Connection connection;

    private DataBase() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Базу даних підключено успішно!");
        } catch (SQLException e) {
            System.out.println("Помилка при підключенні до бази даних: " + e.getMessage());
        }
    }

    public static synchronized DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}

