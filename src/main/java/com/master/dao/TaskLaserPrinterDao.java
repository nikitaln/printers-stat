package com.master.dao;

import com.master.db.DbConnection;
import com.master.laser.TaskLaserPrinter;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskLaserPrinterDao {

    private final Connection conn;

    public TaskLaserPrinterDao(Connection conn) {
        this.conn = conn;
    }


    // ГОТОВ
//    public void createTableForLaserPrinter() {
//
//        //запрос на создание таблицы
//        String SQL = "CREATE TABLE laser_stat( " +
//                " id INT not NULL AUTO_INCREMENT, " +
//                " dateTime DATETIME, " +
//                " fileName VARCHAR (250), " +
//                " printStatus VARCHAR (250), " +
//                " countPages LONG, " +
//                " format VARCHAR (250), " +
//                " username VARCHAR (250), " +
//                " printerName VARCHAR (250), " +
//                " PRIMARY KEY(id));";
//
//        try {
//            Statement statement = conn.getConnection().createStatement();
//            statement.executeUpdate(SQL);
//            System.out.println("Table successfully created...");
//            statement.close();
//            conn.closeConnection();
//
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }


    // ГОТОВ
    public void addTaskLaserPrinter(TaskLaserPrinter task) {

        try {
            Statement statement = conn.createStatement();

            statement.executeUpdate("INSERT INTO laserPrinterStat(" +
                    " dateTime, fileName, printStatus, format, countPages, username, printerName) " +
                    "VALUES (" +
                    "'" + task.getDateTime() + "', " +
                    "'" + task.getName() + "', " +
                    "'" + task.getStatus() + ", " +
                    "'" + task.getFormat() + "', " +
                    "'" + task.getCountPage() + "', " +
                    "'" + task.getUsername() + "', " +
                    "'" + task.getPrinter() + "');"
            );

            statement.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ГОТОВ
    public String getLastDateTime() {
        String sql = "SELECT `dateTime` " +
                "FROM `laser_stat` " +
                "ORDER BY `dateTime` DESC " +
                "LIMIT 1;";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            String lastDate = "";
            while (resultSet.next()) {
                lastDate = resultSet.getString("dateTime");
                System.out.println("last date = " + lastDate);
            }

            preparedStatement.close();
            resultSet.close();
            conn.close();
            return lastDate;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ГОТОВ
    public String getFullStatisticsByDateFormat_A4_80gm(String datePeriod) {
        //31.03.2025-04.04.2025

        String[] dates = datePeriod.split("-");

        System.out.println("start=" + dates[0]);
        System.out.println("end=" + dates[1]);


        String A4result = "";
        String startDate = getLocalDateFromString(dates[0]).toString();
        String endDate = getLocalDateFromString(dates[1]).toString();

        String sqlFormatA4 = "SELECT SUM(`countPages`) `sum` FROM `laser_stat` " +
                "WHERE " +
                "`dateTime` > '" + startDate + "' AND " +
                "`dateTime` <= '" + endDate + "' AND " +
                "`format` LIKE 'A4';";


        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlFormatA4);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                A4result = resultSet.getString("sum");
                System.out.println("last date = " + A4result);
            }

            preparedStatement.close();
            resultSet.close();
            conn.close();
            return A4result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ГОТОВ
    public String getFullStatisticsByDateFormat_A3_80gm(String datePeriod) {
        //31.03.2025-04.04.2025

        String[] dates = datePeriod.split("-");

        System.out.println("start=" + dates[0]);
        System.out.println("end=" + dates[1]);

        String A3result = "";
        String startDate = getLocalDateFromString(dates[0]).toString();
        String endDate = getLocalDateFromString(dates[1]).toString();

        String sqlFormatA3 = "SELECT SUM(`countPages`) `sum` FROM `laser_stat` " +
                "WHERE " +
                "`dateTime` > '" + startDate + "' AND " +
                "`dateTime` <= '" + endDate + "' AND " +
                "`format` LIKE 'A3' AND " +
                "`fileName` LIKE '%Обычная%';";


        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlFormatA3);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                A3result = resultSet.getString("sum");
                System.out.println("last date = " + A3result);
            }

            preparedStatement.close();
            resultSet.close();
            conn.close();
            return A3result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ГОТОВ
    public String getFullStatisticsByDateFormat_A3_160gm(String datePeriod) {
        //31.03.2025-04.04.2025

        String[] dates = datePeriod.split("-");

        System.out.println("start=" + dates[0]);
        System.out.println("end=" + dates[1]);

        String A3result = "";
        String startDate = getLocalDateFromString(dates[0]).toString();
        String endDate = getLocalDateFromString(dates[1]).toString();

        String sqlFormatA3 = "SELECT SUM(`countPages`) `sum` FROM `laser_stat` " +
                "WHERE " +
                "`dateTime` > '" + startDate + "' AND " +
                "`dateTime` <= '" + endDate + "' AND " +
                "`format` LIKE 'A3' AND " +
                "`fileName` LIKE '%Плотная%';";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlFormatA3);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                A3result = resultSet.getString("sum");
                System.out.println("last date = " + A3result);
            }

            preparedStatement.close();
            resultSet.close();
            conn.close();
            return A3result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ГОТОВ
    public String getStatisticsByDateFormat_A3_All(String datePeriod) {
        //31.03.2025-04.04.2025

        String[] dates = datePeriod.split("-");

        System.out.println("start=" + dates[0]);
        System.out.println("end=" + dates[1]);

        String A3result = "";
        String startDate = getLocalDateFromString(dates[0]).toString();
        String endDate = getLocalDateFromString(dates[1]).toString();

        String sqlFormatA3 = "SELECT SUM(`countPages`) `sum` FROM `laser_stat` " +
                "WHERE " +
                "`dateTime` > '" + startDate + "' AND " +
                "`dateTime` <= '" + endDate + "' AND " +
                "`format` LIKE 'A3';";


        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlFormatA3);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                A3result = resultSet.getString("sum");
                System.out.println("last date = " + A3result);
            }

            preparedStatement.close();
            resultSet.close();
            conn.close();
            return A3result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ГОТОВ
    public LocalDate getLocalDateFromString(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(date, formatter);
    }

    // ГОТОВ
    public String getCountFiles(String datePeriod) {
        String[] dates = datePeriod.split("-");

        System.out.println("start=" + dates[0]);
        System.out.println("end=" + dates[1]);

        String filesCount = "";
        String startDate = getLocalDateFromString(dates[0]).toString();
        String endDate = getLocalDateFromString(dates[1]).toString();

        String sqlFormatA3 = "SELECT COUNT(*) `countFiles` FROM `laser_stat` " +
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

    // ГОТОВ
    public String getCountFilesByName(String datePeriod, String domainName) {
        String[] dates = datePeriod.split("-");

        System.out.println("start=" + dates[0]);
        System.out.println("end=" + dates[1]);

        String filesCount = "";
        String startDate = getLocalDateFromString(dates[0]).toString();
        String endDate = getLocalDateFromString(dates[1]).toString();

        String sqlFormatA3 = "SELECT COUNT(*) `countFiles` FROM `laser_stat` " +
                "WHERE " +
                "`username` LIKE '" + domainName + "'" + " AND" +
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

    // ГОТОВ
    public String getStatisticsByDateByNameFormat_A4_80gm(String datePeriod, String domainName) {
        //31.03.2025-04.04.2025

        String[] dates = datePeriod.split("-");

        System.out.println("start=" + dates[0]);
        System.out.println("end=" + dates[1]);


        String A4result = "";
        String startDate = getLocalDateFromString(dates[0]).toString();
        String endDate = getLocalDateFromString(dates[1]).toString();

        String sqlFormatA4 = "SELECT SUM(`countPages`) `sum` FROM `laser_stat` " +
                "WHERE " +
                "`dateTime` > '" + startDate + "' AND " +
                "`dateTime` <= '" + endDate + "' AND " +
                "`username` LIKE '" + domainName + "'" + " AND" +
                "`format` LIKE 'A4';";


        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlFormatA4);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                A4result = resultSet.getString("sum");
                System.out.println("last date = " + A4result);
            }

            preparedStatement.close();
            resultSet.close();
            conn.close();
            return A4result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ГОТОВ
    public String getStatisticsByDateByNameFormat_A3_80gm(String datePeriod, String domainName) {
        //31.03.2025-04.04.2025

        String[] dates = datePeriod.split("-");

        System.out.println("start=" + dates[0]);
        System.out.println("end=" + dates[1]);

        String A3result = "";
        String startDate = getLocalDateFromString(dates[0]).toString();
        String endDate = getLocalDateFromString(dates[1]).toString();

        String sqlFormatA3 = "SELECT SUM(`countPages`) `sum` FROM `laser_stat` " +
                "WHERE " +
                "`dateTime` > '" + startDate + "' AND " +
                "`dateTime` <= '" + endDate + "' AND " +
                "`username` LIKE '" + domainName + "'" + " AND" +
                "`format` LIKE 'A3' AND " +
                "`fileName` LIKE '%Обычная%';";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlFormatA3);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                A3result = resultSet.getString("sum");
                System.out.println("last date = " + A3result);
            }

            preparedStatement.close();
            resultSet.close();
            conn.close();
            return A3result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ГОТОВ
    public String getStatisticsByDateByNameFormat_A3_160gm(String datePeriod, String domainName) {
        //31.03.2025-04.04.2025

        String[] dates = datePeriod.split("-");

        System.out.println("start=" + dates[0]);
        System.out.println("end=" + dates[1]);

        String A3result = "";
        String startDate = getLocalDateFromString(dates[0]).toString();
        String endDate = getLocalDateFromString(dates[1]).toString();

        String sqlFormatA3 = "SELECT SUM(`countPages`) `sum` FROM `laser_stat` " +
                "WHERE " +
                "`dateTime` > '" + startDate + "' AND " +
                "`dateTime` <= '" + endDate + "' AND " +
                "`username` LIKE '" + domainName + "'" + " AND" +
                "`format` LIKE 'A3' AND " +
                "`fileName` LIKE '%Плотная%';";


        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlFormatA3);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                A3result = resultSet.getString("sum");
                System.out.println("last date = " + A3result);
            }

            preparedStatement.close();
            resultSet.close();
            conn.close();
            return A3result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void addAllTaskLaserPrinterToDataBse(List<TaskLaserPrinter> taskLaserPrinterList) {
        System.out.println("Вход в новый метод с PrepareStatement = 1");
        String sql = "INSERT INTO laser_stat(dateTime, fileName, printStatus, " +
                "format, countPages, username, printerName) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        System.out.println("Вход цикл с PrepareStatement = 2");
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (TaskLaserPrinter t : taskLaserPrinterList) {
                ps.setObject(1, t.getDateTime());
                ps.setString(2, t.getName());
                ps.setString(3, t.getStatus());
                ps.setString(4, t.getFormat());
                ps.setInt(5, (int) t.getCountPage());
                ps.setString(6, t.getUsername());
                ps.setString(7, t.getPrinter());
                ps.addBatch();  // ← накопление

                int batchCount = 0;

                if (batchCount++ % 1000 == 0) {
                    ps.executeBatch();  // ← отправка пачки
                }
            }
            ps.executeBatch();  // остаток
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Таблица заполнена с PrepareStatement = 3");
    }
}
