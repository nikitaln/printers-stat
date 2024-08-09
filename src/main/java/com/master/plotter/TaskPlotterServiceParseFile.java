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


    private String path = "C:\\Users\\lukanin_ns\\Downloads\\hp8000\\1.txt";
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

            taskPlotterStorage.printAllTaskPlotter();
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
        System.out.println(fragments[5] + "     " + fragments[16]);
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

}
