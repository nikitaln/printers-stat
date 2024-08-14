package com.master.laser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class TaskLaserPrinterDBConnection {

    private String url = "jdbc:mysql://localhost:3306/printers_stat";
    private String user = "root";
    private String pass = "1234";
    Connection connection;


    public Connection getConnection() {
        try {
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("соединение с БД установлено");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }



    public void createTableForLaserPrinter() {

        //запрос на создание таблицы
        String SQL = "CREATE TABLE laserPrinterStat( " +
                " id INTEGER not NULL, " +
                " dateTime DATE, " +
                " name VARCHAR (250), " +
                " status VARCHAR (250), " +
                " format VARCHAR (250), " +
                " countCopy INTEGER, " +
                " typeOfPaper VARCHAR (250), " +
                " countPage LONG, " +
                " username VARCHAR (250), " +
                " printer VARCHAR (250));";


        try {
            Connection connection = getConnection();
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
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            statement.executeUpdate("INSERT INTO laserPrinterStat(" +
                    " dateTime, name, status, format, countCopy, typeOfPaper, countPage, username, printer) " +
                    "VALUES (" +
                    "'" + task.getDateTime() + "', " +
                    "'" + task.getName() + "', " +
                    "'" + task.getStatus() + ", " +
                    "'" + task.getFormat() + "', " +
                    task.getCountCopy() + ", " +
                    "'" + task.getTypeOfPaper() + "', " +
                    task.getCountPage() + "', " +
                    "'" + task.getUsername() + "', " +
                    "'" + task.getPrinter() + "');"
            );

            statement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void addAllTaskLaserPrinter(List<TaskLaserPrinter> taskLaserPrinterList) {


    }

}
