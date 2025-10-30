package com.master.laser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskLaserPrinterService {

    TaskLaserPrinterStorage taskLaserPrinterStorage;
    TaskLaserPrinterDBConnection taskLaserPrinterDBConnection;

    //String path = "C:\\Users\\lukanin_ns\\Desktop\\PrintStatService\\VersantNewStat.txt";


    public void parseTxtFileStatisticsLaserPrinter(String path) {

        System.out.println("ВХОД в метод => parseTxtFileStatisticsLaserPrinter");

        taskLaserPrinterStorage = new TaskLaserPrinterStorage();
        //парсинг файла-журнала статистики
        File file = new File(path);

        try {
            List<String> lines = Files.readAllLines(Paths.get(path));

            //начало построчного парсинга файл со 3ьей строки
            for (int i = 2; i < lines.size(); i++) {

                String[] fragments = lines.get(i).split("\t");

                if (!fragments[2].contains("OK")) {
                    System.out.println("Тут=" + fragments[2]);

                    String[] arrayDataTask = new String[6];
                    arrayDataTask[0] = fragments[0];
                    arrayDataTask[1] = fragments[1];
                    arrayDataTask[2] = fragments[2];
                    arrayDataTask[3] = fragments[3];
                    arrayDataTask[4] = "ошибка";
                    arrayDataTask[5] = fragments[5];

                    System.out.println("size - " + fragments.length);
                    System.out.println("format = " + arrayDataTask[4]);
                    System.out.println("format after = " + getCorrectA4Format(arrayDataTask[4]));

                    System.out.println(
                            "Дата: " + arrayDataTask[0] + "\n"
                            + "Имя файла: " + arrayDataTask[1] + "\n"
                            + "Состояние печати: " + arrayDataTask[2] + "\n"
                            + "Кол-во страниц: " + arrayDataTask[3] + "\n"
                            + "Формат: " + arrayDataTask[4] + "\n"
                            + "Пользователь: " + arrayDataTask[5]);


                    TaskLaserPrinter taskLaserPrinter = createTaskLaserPrinter(arrayDataTask, path);
                    taskLaserPrinterStorage.addTaskLaserPrinter(taskLaserPrinter);

                    //передаем в метод по созданию задачи
                } else {

                    System.out.println("size - " + fragments.length);
                    System.out.println("format = " + fragments[4]);
                    System.out.println("format after = " + getCorrectA4Format(fragments[4]));

                    System.out.println(
                            "Дата: " + fragments[0] + "\n"
                            + "Имя файла: " + fragments[1] + "\n"
                            + "Состояние печати: " + fragments[2] + "\n"
                            + "Кол-во страниц: " + fragments[3] + "\n"
                            + "Формат: " + fragments[4] + "\n"
                            + "Пользователь: " + fragments[5]);

                    TaskLaserPrinter taskLaserPrinter = createTaskLaserPrinter(fragments, path);
                    taskLaserPrinterStorage.addTaskLaserPrinter(taskLaserPrinter);
                    //передаем в метод по созданию задачи
                }

                /**TODO
                 * create com.master.laser.TaskLaserPrinter object and ADD to Storage Collection
                 * create Database connection with JDBC-driver
                 * create Database (print_center)
                 * create two tables (plotter_tasks, laser_tasks)
                 */
            }

            taskLaserPrinterDBConnection = new TaskLaserPrinterDBConnection();
//            taskLaserPrinterDBConnection.createTableForLaserPrinter();
            taskLaserPrinterDBConnection.addAllTaskLaserPrinterToDataBse(
                    taskLaserPrinterStorage.getAllLaserTasks());

            System.out.println("Успешное Добавление");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private TaskLaserPrinter createTaskLaserPrinter(String[] fragments, String path) {
        TaskLaserPrinter taskLaserPrinter = new TaskLaserPrinter();

        taskLaserPrinter.setDateTime(getLocalDateTime(fragments[0]));
        taskLaserPrinter.setName(fragments[1]);
        taskLaserPrinter.setStatus(fragments[2]);
        taskLaserPrinter.setCountPage(Long.parseLong(fragments[3]));
        taskLaserPrinter.setFormat(getCorrectA4Format(fragments[4]));
        taskLaserPrinter.setPrinter(getModelPrinter(path));
        taskLaserPrinter.setUsername(fragments[5]);
        System.out.println("printer = " + getModelPrinter(path) + "\n");
        return taskLaserPrinter;
    }



    private String getModelPrinter(String path) {

        if (path.contains("canon")) {
            return "canon165";
        } else if (path.contains("versant")) {
            return "versant3100";
        } else if (path.contains("c75")) {
            return "c75";
        }
        return "нет принтера";
    }



    private LocalDateTime getLocalDateTime(String dateTime) {
        if (dateTime.length() < 19) {

            for (int i = 0; i < dateTime.length(); i++) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
                return localDateTime;
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
        return localDateTime;
    }



    private boolean isLatinStatusSymbols(String status) {
        if (status.equals("OK")) {
            return true;
        } else return false;
    }



    //fix A4 LEF to A4
    private String getCorrectA4Format(String format) {
        if (format.equals("ошибка")) {
            return "ошибка";
        } else if (format.length() > 2) {
            return format.substring(0, 2);
        } else {
            return format;
        }
    }



    public long getSumA3Format() {
        List<TaskLaserPrinter> tasks = taskLaserPrinterStorage.getAllLaserTasks();
        long sumA3Format = 0;

        for (TaskLaserPrinter task : tasks) {
            if (task.getFormat().equals("A3")) {
                sumA3Format = sumA3Format + task.getCountPage();
            }
        }

        return sumA3Format;
    }


    public long getSumA4Format() {
        List<TaskLaserPrinter> tasks = taskLaserPrinterStorage.getAllLaserTasks();
        long sumA4Format = 0;

        for (TaskLaserPrinter task : tasks) {
            if (task.getFormat().equals("A4")) {
                sumA4Format = sumA4Format + task.getCountPage();
            }
        }

        return sumA4Format;
    }

}