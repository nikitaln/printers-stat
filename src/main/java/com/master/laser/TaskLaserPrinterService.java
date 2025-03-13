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

    String path = "C:\\Users\\lukanin_ns\\Desktop\\PrintStatService\\VersantNewStat.txt";


    public void parseTxtFileStatisticsLaserPrinter() {

        System.out.println("ВХОД в метод => parseTxtFileStatisticsLaserPrinter");

        taskLaserPrinterStorage = new TaskLaserPrinterStorage();
        //парсинг файла-журнала статистики
        File file = new File(path);

        try {
            List<String> lines = Files.readAllLines(Paths.get(path));

            //начало построчного парсинга файл со 3ьей строки
            for (int i = 2; i < lines.size() - 1; i++) {

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

                    System.out.println(
                            "\tДата: " + arrayDataTask[0] + " | "
                            + "Имя файла: " + arrayDataTask[1] + " | "
                            + "Состояние печати: " + arrayDataTask[2] + " | "
                            + "Кол-во страниц: " + arrayDataTask[3] + " | "
                            + "Формат: " + arrayDataTask[4] + " | "
                            + "Пользователь: " + arrayDataTask[5]);


                    TaskLaserPrinter taskLaserPrinter = createTaskLaserPrinter(arrayDataTask, path);
                    taskLaserPrinterStorage.addTaskLaserPrinter(taskLaserPrinter);

                    //передаем в метод по созданию задачи
                } else {
                    System.out.println("size - " + fragments.length);

                    System.out.println("Дата: " + fragments[0] + " | "
                            + "Имя файла: " + fragments[1] + " | "
                            + "Состояние печати: " + fragments[2] + " | "
                            + "Пользователь: " + fragments[5] + " | "
                            + "Кол-во страниц: " + fragments[3] + " | "
                            + "Формат: " + fragments[4]);

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
//            taskLaserPrinterDBConnection = new TaskLaserPrinterDBConnection();
//            taskLaserPrinterDBConnection.createTableForLaserPrinter();
//            taskLaserPrinterDBConnection.addAllTaskLaserPrinterToDataBse(
//                    taskLaserPrinterStorage.getAllLaserTasks());
//
//            System.out.println("Успешное Добавление");

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
        taskLaserPrinter.setFormat(fragments[4]);
        taskLaserPrinter.setPrinter(getModelPrinter(path));
        System.out.println("printer = " + getModelPrinter(path));
        return taskLaserPrinter;
    }



    private String getModelPrinter(String path) {

        if (path.contains("CANON")) {
            return "canon165";
        } else if (path.contains("Versant")) {
            return "versant3100";
        } else if (path.contains("C75")) {
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
}