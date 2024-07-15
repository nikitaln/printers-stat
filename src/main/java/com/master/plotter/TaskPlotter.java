package com.master.plotter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * В excel создаем CSV-файл, затем его сохраняем в txt-файл с кодировкой UTF-8
 * Парсинг txt-файла отчета с принтера XL8000
 * с разделителями запятой
 *
 */


public class TaskPlotter {

    private String csvPath = "C:\\Users\\lukanin_ns\\Downloads\\xl8000.csv";

    public void parsingTxtFile(String path) {

        File file = new File(path);
        System.out.println(file.length());
        Path filePath = Paths.get(path);

        try {

            System.out.println("Проверка1");
            List<String> lines = Files.readAllLines(filePath);
            System.out.println("Проверка2");


            for (String line : lines) {

                if (line.contains("HP SmartStream") && !line.contains("0,0000")) {

                    String[] fragments = line.split(";");
                    System.out.println(
                            fragments[0] + "\t" +
                            fragments[1] + "\t" +
                            fragments[2] + "\t" +
                            fragments[3] + "\t" +
                            fragments[4] + "\t" +
                            fragments[5] + "\t" +
                            fragments[6] + "\t" +
                            fragments[7] + "\t" +
                            fragments[8] + "\t" +
                            fragments[9] + "\t" +
                            fragments[10] + "\t" +
                            fragments[11] + "\t" +
                            fragments[12] + "\t" +
                            fragments[13] + "\t" +
                            fragments[14] + "\t" +
                            fragments[15] + "\t" +
                            fragments[16] + "\t" +
                            fragments[17] + "\t");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IndexOutOfBoundsException e) {

        }


    }
}
