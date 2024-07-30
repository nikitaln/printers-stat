package com.master.plotter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * В excel создаем CSV-файл, затем его сохраняем в txt-файл с кодировкой UTF-8
 * Парсинг txt-файла отчета с принтера XL8000
 * с разделителями запятой
 *
 */


public class TaskPlotter {

    private String startDate = "15.07.2024";
    private String endDate = "21.07.2024";

    public void parsingTxtFile(String path) {

        double allLengthPaper = 0;

        LocalDate startLocalDate = getLocalDateFromString(startDate);
        LocalDate endLocalDate = getLocalDateFromString(endDate);

        File file = new File(path);
        System.out.println(file.length());
        Path filePath = Paths.get(path);

        try {

            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {

                if (line.contains("HP SmartStream") && !line.contains("0,0000")) {

                    String[] fragments = line.split(";");
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

//                    System.out.println(
//                            fragments[0] + "\t" +
//                            fragments[1] + "\t" +
//                            fragments[2] + "\t" +
//                            fragments[3] + "\t" +
//                            fragments[4] + "\t" +
//                            fragments[5] + "\t" +
//                            fragments[6] + "\t" +
//                            fragments[7] + "\t" +
//                            fragments[8] + "\t" +
//                            fragments[9] + "\t" +
//                            fragments[10] + "\t" +
//                            fragments[11] + "\t" +
//                            fragments[12] + "\t" +
//                            fragments[13] + "\t" +
//                            fragments[14] + "\t" +
//                            fragments[15] + "\t" +
//                            fragments[16] + "\t" +
//                            fragments[17] + "\t");

                    String date = getStringDateWithoutTime(fragments[15]);
                    LocalDate localDate = getLocalDateFromString(date);

                    if (fragments[8].contains("Обычная") &&
                            startLocalDate.minusDays(1).isBefore(localDate) &&
                            endLocalDate.plusDays(1).isAfter(localDate)) {

                        String stringLengthPaper = fragments[10].replace(",",".");
                        double doubleLengthPaper = Double.valueOf(stringLengthPaper);
                        allLengthPaper = allLengthPaper + doubleLengthPaper;
                        System.out.println(fragments[15]);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IndexOutOfBoundsException e) {

        }

        System.out.println("Итого - " + allLengthPaper);
    }


    //example input - 25.01.2024
    public LocalDate getLocalDateFromString(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(date, formatter);
    }



    public String getStringDateWithoutTime(String dateAndTime) {
        String[] dateTime = dateAndTime.split("\s");
        return dateTime[0];
    }
}
