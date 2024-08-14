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

    String path = "C:\\Users\\lukanin_ns\\Desktop\\Отчеты с лазерных принтеров\\C75.txt";


    public void parseTxtFileStatisticsLaserPrinter() {
        taskLaserPrinterStorage = new TaskLaserPrinterStorage();
        //парсинг файла-журнала статистики
        File file = new File(path);

        try {
            List<String> lines = Files.readAllLines(Paths.get(path));

            for (int i = 2; i < lines.size(); i++) {

                String[] fragments = lines.get(i).split("\t");

                if (fragments.length < 8) {
                    System.out.println("Тут=" + fragments[2]);
                    String[] arrayDataTask = new String[8];
                    arrayDataTask[0] = fragments[0];
                    arrayDataTask[1] = fragments[1];
                    arrayDataTask[2] = fragments[2];
                    arrayDataTask[3] = fragments[3];
                    arrayDataTask[4] = fragments[4];
                    arrayDataTask[5] = fragments[5];
                    arrayDataTask[6] = "Нет данных";
                    arrayDataTask[7] = "Нет данных";

                    System.out.println("\tДата: " + arrayDataTask[0] + " | "
                            + "Имя файла: " + arrayDataTask[1] + " | "
                            + "Состояние печати: " + arrayDataTask[2] + " | "
                            + "Пользователь: " + arrayDataTask[3] + " | "
                            + "Кол-во страниц: " + arrayDataTask[4] + " | "
                            + "Кол-во копий: " + arrayDataTask[5] + " | "
                            + "Формат: " + arrayDataTask[6] + " | "
                            + "Тип бумаги: " + arrayDataTask[7]);


                    TaskLaserPrinter taskLaserPrinter = createTaskLaserPrinter(arrayDataTask, path);
                    taskLaserPrinterStorage.addTaskLaserPrinter(taskLaserPrinter);
                    //передаем в метод по созданию задачи
                } else {
                    System.out.println("Дата: " + fragments[0] + " | "
                            + "Имя файла: " + fragments[1] + " | "
                            + "Состояние печати: " + fragments[2] + " | "
                            + "Пользователь: " + fragments[3] + " | "
                            + "Кол-во страниц: " + fragments[4] + " | "
                            + "Кол-во копий: " + fragments[5] + " | "
                            + "Формат: " + fragments[6] + " | "
                            + "Тип бумаги: " + fragments[7]);

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
        taskLaserPrinter.setFormat(fragments[6]);
        taskLaserPrinter.setCountPage(Long.parseLong(fragments[4]));
        taskLaserPrinter.setCountCopy(Integer.parseInt(fragments[5]));
        taskLaserPrinter.setTypeOfPaper(fragments[7]);
        taskLaserPrinter.setUsername(fragments[3]);
        taskLaserPrinter.setPrinter(getModelPrinter(path));
        return taskLaserPrinter;
    }



    private String getModelPrinter(String path) {

        if (path.contains("CANON")) {
            return "canon165";
        } else if (path.contains("VERSANT")) {
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
}