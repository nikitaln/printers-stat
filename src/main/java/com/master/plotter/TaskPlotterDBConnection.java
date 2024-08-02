package com.master.plotter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class TaskPlotterDBConnection {

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



    public void createTable() {
        connection = getConnection();
        String SQL = "CREATE TABLE plotter_stat( " +
                " id INT not NULL AUTO_INCREMENT, " +
                " taskName TEXT, " +
                " taskType VARCHAR (250), " +
                " source VARCHAR (250), " +
                " output VARCHAR (250), " +
                " status VARCHAR (250), " +
                " copyCount INT, " +
                " paperType VARCHAR (250), " +
                " paperConsumption DOUBLE, " +
                " paperLengthConsumption DOUBLE, " +
                " tonerConsumption DOUBLE, " +
                " user VARCHAR(250), "  +
                " dateTime DATETIME, " +
                " printType VARCHAR(250), " +
                " PRIMARY KEY(id));";

        try {
            Statement statement = connection.createStatement();
            statement.execute(SQL);
            System.out.println("таблица plotter_stat создана");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }



    public void addAllTaskPlotter(List<TaskPlotter> tasksPlotter) {
        StringBuilder sqlStringBuilder = new StringBuilder();

        for (TaskPlotter taskPlotter : tasksPlotter) {
            sqlStringBuilder.append("('" +
                    taskPlotter.getTaskName() + "', '" +
                    taskPlotter.getTaskType() + "', '" +
                    taskPlotter.getSource() + "', '" +
                    taskPlotter.getOutput() + "', '" +
                    taskPlotter.getStatus() + "', '" +
                    taskPlotter.getCopyCount() + "', '" +
                    taskPlotter.getPaperType() + "', '" +
                    taskPlotter.getPaperConsumption() + "', '" +
                    taskPlotter.getPaperLengthConsumption() + "', '" +
                    taskPlotter.getTonerConsumption() + "', '" +
                    taskPlotter.getUser() + "', '" +
                    taskPlotter.getDateTime() + "', '" +
                    taskPlotter.getPrintType() + "'), ");
        }

        String sql = sqlStringBuilder.toString();
        int lastIndex = sql.length();
        String sql2 = sql.substring(0, lastIndex - 2);
        String sqlFinal = sql2 + ";";

        System.out.println(sqlFinal);

        connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO plotter_stat(" +
                            " taskName,"  +
                            " taskType," +
                            " source," +
                            " output," +
                            " status," +
                            " copyCount," +
                            " paperType," +
                            " paperConsumption," +
                            " paperLengthConsumption," +
                            " tonerConsumption," +
                            " user," +
                            " dateTime," +
                            " printType) VALUES" + sqlFinal);
            System.out.println("таблица plotter_stat заполнена");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public List<TaskPlotter> getAllTaskPlotter() {
        return null;
    }


}
