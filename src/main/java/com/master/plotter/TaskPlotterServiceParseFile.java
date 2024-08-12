package com.master.plotter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskPlotterServiceParseFile {

    TaskPlotterStorage taskPlotterStorage;
    TaskPlotterDBConnection dbConnection;


    private String path = "C:\\Users\\lukanin_ns\\Downloads\\hp8000\\test.txt";
    private String startDate = "08.07.2024";
    private String endDate = "12.07.2024";



    public void parseTxtFileWithHpStat() {

        taskPlotterStorage = new TaskPlotterStorage();


        File file = new File(path);
        Path filePath = Paths.get(path);

        try {
            List<String> lines = Files.readAllLines(Paths.get(path));

            for (String line : lines) {
                if (line.contains("HP SmartStream") &&
                        !line.contains("0,0000")) {

                    String[] fragments = line.split(";");

                    saveToTaskPlotterStorage(
                            createTaskPlotter(fragments));
                }
            }

            dbConnection = new TaskPlotterDBConnection();
            dbConnection.getConnection();
            dbConnection.createTable();
            dbConnection.addAllTaskPlotter(taskPlotterStorage.getAllPlotterTasks());


            System.out.println("данные сохранены в БД");


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public TaskPlotter createTaskPlotter(String[] fragments) {
        // [0] - имя документа
        // [1] - тип задания (печать)
        // [2] - источник задания
        // [3] - конечный выход (укладчик и тд)
        // [4] - состояние (отпечатано)
        // [5] - копии
        // [6] - тип затрат
        // [7] - значение затрат
        // [8] - тип бумаги
        // [9] - расход бумаги
        // [10] - длина бумаги
        // [11] - длина сканирования
        // [12] - одна категория
        // [13] - использованные чернила
        // [14] - имя пользователя
        // [15] - дата печати
        // [15] - качество печати

        TaskPlotter taskPlotter;
        taskPlotter = new TaskPlotter();

        taskPlotter.setTaskName(fragments[0]);
        taskPlotter.setTaskType(fragments[1]);
        taskPlotter.setSource(fragments[2]);
        taskPlotter.setOutput(fragments[3]);
        taskPlotter.setStatus(fragments[4]);
        taskPlotter.setCopyCount(Integer.parseInt(fragments[5]));
        taskPlotter.setPaperType(fragments[8]);

        taskPlotter.setPaperConsumption(Double.valueOf(fragments[9].replace(",", ".")));
        taskPlotter.setPaperLengthConsumption(Double.valueOf(fragments[10].replace(",", ".")));
        taskPlotter.setTonerConsumption(Double.valueOf(fragments[13].replace(",", ".")));

        taskPlotter.setUser(fragments[14]);
        taskPlotter.setDateTime(getLocalDateTimeFromString(fragments[15]));
        taskPlotter.setPrintType(fragments[16]);

        return taskPlotter;
    }



    private LocalDateTime getLocalDateTimeFromString(String dateTime) {
        //input - 18.07.2024 16:08:11
        System.out.println(dateTime.length());
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



    private void saveToTaskPlotterStorage(TaskPlotter taskPlotter) {

        taskPlotterStorage.addTaskPlotter(taskPlotter);
    }



    private void printFragments(String[] array) {
        System.out.println("[0]=" + array[0]);
        System.out.println("[1]=" + array[1]);
        System.out.println("[2]=" + array[2]);
        System.out.println("[3]=" + array[3]);
        System.out.println("[4]=" + array[4]);
        System.out.println("[5]=" + array[5]);
        System.out.println("[6]=" + array[6]);
        System.out.println("[7]=" + array[7]);
        System.out.println("[8]=" + array[8]);
        System.out.println("[9]=" + array[9]);
        System.out.println("[10]=" + array[10]);
        System.out.println("[11]=" + array[11]);
        System.out.println("[12]=" + array[12]);
        System.out.println("[13]=" + array[13]);
        System.out.println("[14]=" + array[14]);
        System.out.println("[15]=" + array[15]);
        System.out.println("[16]=" + array[16]);
        System.out.println("[17]=" + array[17]);
    }
}
