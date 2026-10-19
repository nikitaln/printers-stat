package com.master.dao;


import com.master.plotter.TaskPlotter;

import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskPlotterDao {

    private final Connection conn;

    public TaskPlotterDao(Connection conn) {
        this.conn = conn;
    }


    //ГОТОВ
    //создание таблица plotter_stat
//    public void createTable() {
//        String SQL = "CREATE TABLE plotter_stat( " +
//                " id INT not NULL AUTO_INCREMENT, " +
//                " taskName TEXT, " +
//                " taskType VARCHAR (250), " +
//                " source VARCHAR (250), " +
//                " output VARCHAR (250), " +
//                " status VARCHAR (250), " +
//                " copyCount INT, " +
//                " paperType VARCHAR (250), " +
//                " paperConsumption DOUBLE, " +
//                " paperLengthConsumption DOUBLE, " +
//                " tonerConsumption DOUBLE, " +
//                " user VARCHAR(250), "  +
//                " dateTime DATETIME, " +
//                " printType VARCHAR(250), " +
//                " PRIMARY KEY(id));";
//
//        try {
//            Statement statement = conn.createStatement();
//            statement.execute(SQL);
//            System.out.println("таблица plotter_stat создана");
//            statement.close();
//            conn.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }



    //ГОТОВ
    //добавление списка задач в таблицу plotter_stat
    public void addAllTaskPlotter(List<TaskPlotter> tasksPlotter) {
        System.out.println("зашли в метод add to DB 1 ");

        if (tasksPlotter == null || tasksPlotter.isEmpty()) {
            return;
        }

        System.out.println("не null не пустой 2 = " + tasksPlotter.size());

        final String sql = "INSERT INTO plotter_stat(" +
                "taskName, taskType, source, output, status, copyCount, " +
                "paperType, paperConsumption, paperLengthConsumption, " +
                "tonerConsumption, user, dateTime, printType, printerName) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // если БД поддерживает

        System.out.println("Составили запрос 3");

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int batchSize = 0;

            for (TaskPlotter t : tasksPlotter) {

                int i = 1;

                ps.setString(i++, t.getTaskName());
                ps.setString(i++, t.getTaskType());
                ps.setString(i++, t.getSource());
                ps.setString(i++, t.getOutput());
                ps.setString(i++, t.getStatus());
                ps.setInt   (i++, t.getCopyCount());
                ps.setString(i++, t.getPaperType());
                ps.setDouble(i++, t.getPaperConsumption());
                ps.setDouble(i++, t.getPaperLengthConsumption());
                ps.setDouble(i++, t.getTonerConsumption());
                ps.setString(i++, t.getUser());
                ps.setObject(i++, t.getDateTime()); // или setObject
                ps.setString(i++, t.getPrintType());
                ps.setString(i++, t.getPrinterName());
                ps.addBatch();
                batchSize++;

                // сбрасываем батч каждые 500 записей
                if (batchSize % 500 == 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
                System.out.println("прошлись в цикле 4");
            }

            if (batchSize % 500 != 0) {
                ps.executeBatch();
            }

            System.out.println("Таблица plotter_stat заполнена. Добавлено: " + batchSize);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка вставки в plotter_stat", e);
        }
    }



    public LocalDate getLocalDateFromString(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(date, formatter);
    }



    public List<TaskPlotter> getAllTaskPlotter() {
        return null;
    }



    //ГОТОВ
    //проверка на уникальность заявки по дате
    public boolean containsDateTime(LocalDateTime dateTime) {

        String sql = "SELECT dateTime FROM plotter_stat " +
                "WHERE dateTime = " + "'" + dateTime + "'";


        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()) {

                String dateTimeString = resultSet.getString(1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);

                if (localDateTime.equals(dateTime)) {
                    resultSet.close();
                    statement.close();
                    conn.close();
                    return true;
                }
            }

            resultSet.close();
            statement.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }



    //ГОТОВ
    //получение длинны потраченной Плотной бумаги за период
    public String getCountOfHeavyPaperForPeriod(String period) {
        //31.03.2025-04.04.2025
        String regex = "[-]";
        String[] array = period.split(regex);
        System.out.println("array0 = " + array[0].toString());
        System.out.println("array1 = " + array[1]);
        String startDate = getLocalDateFromString(array[0]).toString();
        String endDate = getLocalDateFromString(array[1]).toString();
        System.out.println(startDate);



        String sql = "SELECT SUM(`paperLengthConsumption`) `length`" +
                "FROM `plotter_stat` " +
                "WHERE " +
                "`paperType` LIKE '%плотная%' AND " +
                "`dateTime` >= '" + startDate + "' AND " +
                "`dateTime` < '" + endDate + "';";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            Double heavyLength = 0.0;
            while (resultSet.next()) {
                heavyLength = resultSet.getDouble("length");
            }

            preparedStatement.close();
            resultSet.close();
            conn.close();
            return roundLength(heavyLength);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    //ГОТОВ
    //получение длинны потраченной Обычной (Тонкой) бумаги за период
    public String getCountOfThinPaperForPeriod(String period) {
        //31.03.2025-04.04.2025
        String regex = "[-]";
        String[] array = period.split(regex);
        System.out.println("array0 = " + array[0].toString());
        System.out.println("array1 = " + array[1]);
        String startDate = getLocalDateFromString(array[0]).toString();
        String endDate = getLocalDateFromString(array[1]).toString();

        System.out.println(startDate);

        String sql = "SELECT SUM(`paperLengthConsumption`) `length`" +
                "FROM `plotter_stat` " +
                "WHERE " +
                "`paperType` LIKE '%обычная%' AND " +
                "`dateTime` >= '" + startDate + "' AND " +
                "`dateTime` < '" + endDate + "';";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            Double thinLength = 0.0;
            while (resultSet.next()) {
                thinLength = resultSet.getDouble("length");
                System.out.println("thinLength = " + thinLength);
            }

            preparedStatement.close();
            resultSet.close();
            conn.close();
            return roundLength(thinLength);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //ГОТОВ
    public String getLastDate() {

        String sql = "SELECT `dateTime` " +
                "FROM `plotter_stat` " +
                "ORDER BY `dateTime` DESC " +
                "LIMIT 1;";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            System.out.println("plotter loop");
            ResultSet resultSet = preparedStatement.executeQuery();
            String lastDate = "";
            while (resultSet.next()) {

                lastDate = resultSet.getString("dateTime");
                System.out.println("plotter DB=" + lastDate);
            }
            preparedStatement.close();
            resultSet.close();
            return lastDate;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    //ГОТОВ
    //получение длинны потраченной самоклеящейся бумаги за период
    public String getCountOfAdhesivePaperForPeriod(String period) {
        //31.03.2025-04.04.2025
        String regex = "[-]";
        String[] array = period.split(regex);
        System.out.println("array0 = " + array[0].toString());
        System.out.println("array1 = " + array[1]);
        String startDate = getLocalDateFromString(array[0]).toString();
        String endDate = getLocalDateFromString(array[1]).toString();

        System.out.println(startDate);



        String sql = "SELECT SUM(`paperLengthConsumption`) `length`" +
                "FROM `plotter_stat` " +
                "WHERE " +
                "`paperType` LIKE '%пленка%' AND " +
                "`dateTime` >= '" + startDate + "' AND " +
                "`dateTime` < '" + endDate + "';";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            Double adhesiveLength = 0.0;
            while (resultSet.next()) {
                adhesiveLength = resultSet.getDouble("length");
                System.out.println("thinLength = " + adhesiveLength);
            }

            preparedStatement.close();
            resultSet.close();
            conn.close();
            return roundLength(adhesiveLength);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    //округление полученных результатов до 2-ух знаков после запятой
    public String roundLength(Double lengthValue) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String result = decimalFormat.format(lengthValue);
        return result;
    }


    //ГОТОВ
    public String getSumFiles(String datePeriod) {

        String[] dates = datePeriod.split("-");

        System.out.println("start=" + dates[0]);
        System.out.println("end=" + dates[1]);

        String filesCount = "";
        String startDate = getLocalDateFromString(dates[0]).toString();
        String endDate = getLocalDateFromString(dates[1]).toString();

        String sqlFormatA3 = "SELECT COUNT(*) `countFiles` FROM `plotter_stat` " +
                "WHERE " +
                "`dateTime` >= '" + startDate + "' AND " +
                "`dateTime` < '" + endDate + "';";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlFormatA3);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                filesCount = resultSet.getString("countFiles");
                System.out.println("Count Files = " + filesCount);
            }

            preparedStatement.close();
            resultSet.close();
            conn.close();
            return filesCount;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
