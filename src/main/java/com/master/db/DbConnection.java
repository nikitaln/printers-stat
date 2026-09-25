package com.master.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private static String URL = "jdbc:mysql://localhost:3306/printers_stat";
    private static String USER = "root";
    private static String PASS = "1234";

    private static Connection conn;

    private DbConnection() {}   // запрещаем создавать экземпляры

    public static Connection get() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("Соединение с БД установлено");
            }
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось подключиться к БД", e);
        }
    }

    public static void close() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Соединение с БД закрыто");
            } catch (SQLException ignored) {
            } finally {
                conn = null;
            }
        }
    }
}
