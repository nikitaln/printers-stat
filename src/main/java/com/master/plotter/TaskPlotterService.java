package com.master.plotter;

import com.master.dao.TaskPlotterDao;
import com.master.gui.DateService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskPlotterService {

    private TaskPlotter taskPlotter;
    private TaskPlotterStorage taskPlotterStorage;
    private TaskPlotterDao plotterDao;

    public TaskPlotterService(TaskPlotterDao plotterDao) {
        this.plotterDao = plotterDao;
    }


    private String pathXL8000_2017_0 = "http://npic5e08b/hp/device/webAccess/index.htm;jsessionid=blir95p7c1?content=accounting";    //старый PageWide
    private String pathXL8000_2021_1 = "http://npi477340/hp/device/webAccess/index.htm;jsessionid=dp01bpqil1?content=accounting";    //новый PageWide
    private String pathXL5100_2018_2 = "http://172.20.51.135/hp/device/webAccess/index.htm;jsessionid=t7e4p68mi1?content=accounting";    //PageWide5100


    //Первый метод (работа с датой)
    public void parseWebPageAndCheckData(String dateTime, int numOfPrinter) {
        System.out.println("проверка даты 1");
        DateService dateService = new DateService();
        int numPrinter = numOfPrinter;
        String namePrinter = getNamePrinter(numPrinter);
        String pathWeb = getPathWebPrinter(numPrinter);

        String date = dateTime;
        if (dateService.isCorrectDateFormat(date)) {
            System.out.println("проверка даты 2");
            if (!dateService.isOneDay(date)) {
                System.out.println("проверка даты 3");
                List<LocalDate> dateList = dateService.getAllDaysBetweenTwoDates(date);
                parseWebPrinterStatistics(dateList, pathWeb, namePrinter); //парсинг списка дней
            } else {
                System.out.println("проверка даты 4");
                List<LocalDate> dateListWithOneDate = new ArrayList<>();
                dateListWithOneDate.add(dateService.getLocalDateFromString(date));
                parseWebPrinterStatistics(dateListWithOneDate, pathWeb, namePrinter); //парсинг одного дня
            }
        }
        System.out.println("завершение проверки даты 6");
        System.out.println();
        System.out.println("Можно добавлять данные в БД");
        plotterDao.addAllTaskPlotter(getAllTasks());
    }


    private String getNamePrinter(int numPrinter) {
        if (numPrinter == 0) {
            return "XL8000 2017";
        } else if (numPrinter == 1) {
            return "XL8000 2021";
        } else return "XL5100 2018";
    }


    private String getPathWebPrinter(int numPrinter) {
        if (numPrinter == 0) {
            return pathXL8000_2017_0;
        } else if (numPrinter == 1) {
            return pathXL8000_2021_1;
        } else return pathXL5100_2018_2;
    }


    //парсинг веб-страницы HP
    private void parseWebPrinterStatistics(List<LocalDate> dates, String pathWebPrinter, String nameOfPrinter) {

        System.out.println("парсинг старт 7");

        taskPlotterStorage = new TaskPlotterStorage();

        Connection connection = Jsoup
                .connect(pathWebPrinter)
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
                    taskPlotterStorage.addTaskPlotter(createTaskPlotterObject(cols, nameOfPrinter));

                }
            }

            taskPlotterStorage.printAllTaskPlotter();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private TaskPlotter createTaskPlotterObject(Elements cols, String namePrinter) {

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
        taskPlotter.setPrinterName(namePrinter);

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


    public String getPlotterLastDateForDisplay() {
        return plotterDao.getLastDate();
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