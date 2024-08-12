package com.master.plotter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskPlotterService {


    private TaskPlotter taskPlotter;
    private TaskPlotterStorage taskPlotterStorage;

    private String path = "http://npic5e08b/hp/device/webAccess/index.htm;jsessionid=blir95p7c1?content=accounting";


    //парсинг веб-страницы HP
    public void parseWebPrinterStatistics(List<LocalDate> dates) {

        taskPlotterStorage = new TaskPlotterStorage();

        Connection connection = Jsoup
                .connect(path)
                .maxBodySize(Integer.MAX_VALUE);

        try {

            Document document = connection.get();
            Elements table = document.select("table")
                    .get(4).
                    getElementsByClass("treeTableTitleRowClosed treeTableTopLevel");

            //создание объектов Задачи
            for (Element row : table) {
                Elements cols = row.select("td");

                //если задача попадает в нужную дату, то создаем задачу
                if (isEqualInputUserDate(getLocalDateTimeFromString(cols.get(16).text()), dates)) {

                    System.out.println(cols.get(0).text()); //имя файла
                    taskPlotterStorage.addTaskPlotter(createTaskPlotterObject(cols));

                }
            }

            taskPlotterStorage.printAllTaskPlotter();
//            taskPlotterDBConnection = new TaskPlotterDBConnection();
//            taskPlotterDBConnection.createTable();
//            taskPlotterDBConnection.addAllTaskPlotter(taskPlotterStorage.getAllPlotterTasks());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private TaskPlotter createTaskPlotterObject(Elements cols) {

        taskPlotter = new TaskPlotter();
        taskPlotter.setTaskName(cols.get(0).text());
        taskPlotter.setTaskType(cols.get(1).text());
        taskPlotter.setSource(cols.get(2).text());
        taskPlotter.setOutput(cols.get(3).text());
        taskPlotter.setStatus(cols.get(4).text());
        taskPlotter.setCopyCount(Integer.valueOf(cols.get(5).text()));
        taskPlotter.setPaperType(cols.get(10).text());
        taskPlotter.setPaperConsumption(
                getPaperConsumptionFromString(cols.get(11).text()));

        taskPlotter.setPaperLengthConsumption(
                getPaperLengthConsumptionFromString(cols.get(12).text()));

        taskPlotter.setTonerConsumption(
                getTonerConsumptionFromString(cols.get(14).text()));

        taskPlotter.setUser(cols.get(15).text());
        taskPlotter.setDateTime(
                getLocalDateTimeFromString(cols.get(16).text()));
        taskPlotter.setPrintType(cols.get(17).text());

        return taskPlotter;
    }



    private double getPaperConsumptionFromString(String paperSquare) {
        //input - 1,0710 м²
        //output - 1,0710
        //String square = "1,0710 м²";
        String regex = "[0-9]";
        String squareDigitStr = "";
        for (int i = 0; i < paperSquare.length(); i++) {
            char symbol = paperSquare.charAt(i);
            String symbolStr = String.valueOf(symbol);
            if (symbolStr.matches(regex) || symbolStr.equals(",")) {
                squareDigitStr = squareDigitStr + symbolStr;
            }
        }
        squareDigitStr = squareDigitStr.replace(",", ".");
        return Double.valueOf(squareDigitStr);
    }



    private double getPaperLengthConsumptionFromString(String paperLength) {
        //input - 1,2700 м
        //output - 1,2700
        //String paperLength = "1,2700 м";
        String regex = "[0-9]";
        String lengthDigitStr = "";
        for (int i = 0; i < paperLength.length(); i++) {
            char symbol = paperLength.charAt(i);
            String symbolStr = String.valueOf(symbol);
            if (symbolStr.matches(regex) || symbolStr.equals(",")) {
                lengthDigitStr = lengthDigitStr + symbolStr;
            }
        }
        lengthDigitStr = lengthDigitStr.replace(",", ".");
        return Double.valueOf(lengthDigitStr);
    }



    private double getTonerConsumptionFromString(String toner) {
        String regex = "[0-9]";
        String tonerDigitStr = "";
        for (int i = 0; i < toner.length(); i++) {
            char symbol = toner.charAt(i);
            String symbolStr = String.valueOf(symbol);
            if (symbolStr.matches(regex) || symbolStr.equals(",")) {
                tonerDigitStr = tonerDigitStr + symbolStr;
            }
        }
        tonerDigitStr = tonerDigitStr.replace(",", ".");
        return Double.valueOf(tonerDigitStr);
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


    //Первый способ
    private void parseWebPrinterStatisticsWithoutUseTable() {

        Connection connection = Jsoup
                .connect("http://npic5e08b/hp/device/webAccess/index.htm;jsessionid=blir95p7c1?content=accounting")
                .maxBodySize(Integer.MAX_VALUE);
        try {
            AtomicInteger i = new AtomicInteger();
            AtomicInteger j = new AtomicInteger();
            Document document = connection.get();

            Elements elements = document.select("tr");
            elements.forEach(element -> {

                if (element.hasClass("treeTableTitleRowClosed treeTableTopLevel")) {

                    String[] array = new String[15];

                    i.set(i.get() + 1);
                    if (i.get() == 1) {

                        element.select("tr.treeTableTopLevel td").forEach(element1 -> {
                                    if (!element1.text().isEmpty()) {
                                        //0 ВАО_А1_1копия_БезФальц_ОбычнаяБумага_реновация2
                                        //1 Печать
                                        //2 HP SmartStream
                                        //3 Укладчик
                                        //4 отменено пользователем
                                        //5 0
                                        //6 Всего
                                        //7 0
                                        //8 Обычная бумага (< 90 г/кв.м)
                                        //9 1,0710 м²
                                        //10 1,2700 м
                                        //11 1,54 мл.
                                        //12 Biegler
                                        //13 18.07.2024 16:08:11
                                        //14 Линии/быстро
                                        array[j.get()] = element1.text();
                                        System.out.println(j + " " + element1.text());
                                        j.set(j.get() + 1);
                                    }
                                }
                        );
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private boolean isEqualInputUserDate(LocalDateTime localDateTime, List<LocalDate> dates) {

        for (LocalDate date : dates) {
            if (date.isEqual(localDateTime.toLocalDate())) {
                return true;
            }
        }
        return false;
    }



    public double getLengthHeavyPaper() {
        return taskPlotterStorage.getLengthHeavyPaper();
    }



    public double getLengthThinPaper() {
        return taskPlotterStorage.getLengthThinPaper();
    }



    public void printAllTasks() {
        taskPlotterStorage.printAllTaskPlotter();
    }



    public List<TaskPlotter> getAllTasks() {
        return taskPlotterStorage.getAllPlotterTasks();
    }
}

//                System.out.println(cols.get(0).text()); //имя файла
//                System.out.println(cols.get(1).text()); //тип задания
//                System.out.println(cols.get(2).text()); //источник задания
//                System.out.println(cols.get(3).text()); //выход задания на печать
//                System.out.println(cols.get(4).text()); //состояние (отпечатано)
//                System.out.println(cols.get(5).text()); //копии
//                System.out.println(cols.get(6).text()); //тип затрат (НЕ РАБОТАЕТ)
//                System.out.println(cols.get(7).text()); //тип затрат (Всего)
//                System.out.println(cols.get(8).text()); //НЕ РАБОТАЕТ
//                System.out.println(cols.get(9).text()); //значение затрат
//                System.out.println(cols.get(10).text());    //тип бумаги
//                System.out.println(cols.get(11).text());    //расход бумаги
//                System.out.println(cols.get(12).text());    //длина бумаги
//                System.out.println(cols.get(13).text());    //одна категория
//                System.out.println(cols.get(14).text());    //использование чернил
//                System.out.println(cols.get(15).text());    //имя пользователя
//                System.out.println(cols.get(16).text());    //дата
//                System.out.println(cols.get(17).text());    //качество печати