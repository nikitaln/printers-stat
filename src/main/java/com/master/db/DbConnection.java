package com.master.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private String url = "jdbc:mysql://localhost:3306/printers_stat";
    private String user = "root";
    private String pass = "1234";
    Connection connection;


    //1. установка соединения с БД
    public Connection getConnection() {
        try {
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("соединение с БД установлено");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
