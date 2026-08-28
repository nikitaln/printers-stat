package com.master.laser;


import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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


    /**
     * @param dateTime
     * @return проверять поле DateTime и PrinterName
     */
    //проверка на уникальность записи в БД
    public boolean containsDateTime(LocalDateTime dateTime, String printerName) {

        String sql = "SELECT dateTime FROM plotter_stat " +
                "WHERE dateTime = " + "'" + dateTime + "'";

        connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {

                String dateTimeString = resultSet.getString(1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);

                if (localDateTime.equals(dateTime)) {
                    resultSet.close();
                    statement.close();
                    connection.close();
                    return true;
                }
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }


    public void createTableForLaserPrinter() {

        //запрос на создание таблицы
        String SQL = "CREATE TABLE laser_stat( " +
                " id INT not NULL AUTO_INCREMENT, " +
                " dateTime DATETIME, " +
                " fileName VARCHAR (250), " +
                " printStatus VARCHAR (250), " +
                " countPages LONG, " +
                " format VARCHAR (250), " +
                " username VARCHAR (250), " +
                " printerName VARCHAR (250), " +
                " PRIMARY KEY(id));";


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
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void addAllTaskLaserPrinterToDataBse(List<TaskLaserPrinter> taskLaserPrinterList) {
        StringBuilder sqlStringBuilder = new StringBuilder();

        for (TaskLaserPrinter taskLaserPrinter : taskLaserPrinterList) {
            sqlStringBuilder.append("('" +
                    taskLaserPrinter.getDateTime() + "', '" +
                    taskLaserPrinter.getName() + "', '" +
                    taskLaserPrinter.getStatus() + "', '" +
                    taskLaserPrinter.getFormat() + "', '" +
                    taskLaserPrinter.getCountPage() + "', '" +
                    taskLaserPrinter.getUsername() + "', '" +
                    taskLaserPrinter.getPrinter() + "'), ");
        }

        String sql = sqlStringBuilder.toString();
        int lastIndex = sql.length();
        String sql2 = sql.substring(0, lastIndex - 2);
        String sqlFinal = sql2 + ";";

        System.out.println(sql2);


        connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO laser_stat(" +
                    " dateTime," +
                    " fileName," +
                    " printStatus," +
                    " format," +
                    " countPages," +
                    " username," +
                    " printerName) VALUES" + sqlFinal);

            System.out.println("таблица laser_stat заполнена");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public String getLastDateTime() {
        String sql = "SELECT `dateTime` " +
                "FROM `laser_stat` " +
                "ORDER BY `dateTime` DESC " +
                "LIMIT 1;";


        connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            String lastDate = "";
            while (resultSet.next()) {
                lastDate = resultSet.getString("dateTime");
                System.out.println("last date = " + lastDate);
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();
            return lastDate;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


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

        connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlFormatA4);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                A4result = resultSet.getString("sum");
                System.out.println("last date = " + A4result);
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();
            return A4result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


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

        connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlFormatA3);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                A3result = resultSet.getString("sum");
                System.out.println("last date = " + A3result);
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();
            return A3result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


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

        connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlFormatA3);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                A3result = resultSet.getString("sum");
                System.out.println("last date = " + A3result);
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();
            return A3result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


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

        connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlFormatA3);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                A3result = resultSet.getString("sum");
                System.out.println("last date = " + A3result);
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();
            return A3result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public LocalDate getLocalDateFromString(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(date, formatter);
    }


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

        connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlFormatA3);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                filesCount = resultSet.getString("countFiles");
                System.out.println("Count Files = " + filesCount);
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();
            return filesCount;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


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

        connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlFormatA3);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                filesCount = resultSet.getString("countFiles");
                System.out.println("Count Files = " + filesCount);
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();
            return filesCount;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


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

        connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlFormatA4);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                A4result = resultSet.getString("sum");
                System.out.println("last date = " + A4result);
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();
            return A4result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


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

        connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlFormatA3);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                A3result = resultSet.getString("sum");
                System.out.println("last date = " + A3result);
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();
            return A3result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


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

        connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlFormatA3);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                A3result = resultSet.getString("sum");
                System.out.println("last date = " + A3result);
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();
            return A3result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
