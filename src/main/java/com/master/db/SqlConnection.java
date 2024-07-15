package com.master.db;

import com.master.laser.TaskLaserPrinter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlConnection {

    private String url = "jdbc:mysql://localhost:3306/print_center";
    private String user = "root";
    private String pass = "1234";


    public void createTableForLaserPrinter() {

        //запрос на создание таблицы
        String SQL = "CREATE TABLE laserPrinterStat( " +
                " id INTEGER not NULL, " +
                " dateTime VARCHAR(100), " +
                " name VARCHAR (250), " +
                " status VARCHAR (250), " +
                " format VARCHAR (250), " +
                " countCopy INTEGER, " +
                " typeOfPaper VARCHAR (250), " +
                " stylePrint VARCHAR (250), " +
                " countPage LONG, " +
                " username VARCHAR (250));";


        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();
            statement.executeUpdate(SQL);
            System.out.println("Table successfully created...");
            statement.close();
            connection.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void addTaskLaserPrinter(TaskLaserPrinter task) {

        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();

            statement.executeUpdate("INSERT INTO laserPrinterStat(" +
                    " id, dateTime, name, status, format, countCopy, typeOfPaper, stylePrint, countPage, username) " +
                    "VALUES (" +
                            task.getId() + ", " +
                            "'" + task.getDateTime() + "', " +
                            "'" + task.getName() + "', " +
                            task.isStatus() + ", " +
                            "'" + task.getFormat() + "', " +
                            task.getCountCopy() + ", " +
                            "'" + task.getTypeOfPaper() + "', " +
                            "'" + task.getStylePrint() + "', " +
                            task.getCountPage() + ", " +
                            "'" + task.getUsername() + "');"
                    );

            statement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
