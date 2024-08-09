package com.master.gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainFormService {

    //обработка ошибок с вводом даты
    //подготовка готового результата для вывода в TextArea

    private String regexOneDay = "[0-9]{2}[.][0-9]{2}[.][0-9]{4}";    //regex соответствует формату дата 01.02.2024
    private String regexFewDays = "[0-9]{2}[.][0-9]{2}[.][0-9]{4}[-][0-9]{2}[.][0-9]{2}[.][0-9]{4}";    //regex соответствует формату дата 01.02.2024-02.01.2024



    //проверка последовательности введенных дат
    public boolean isCorrectDateFormat(String date) {
        if (date.matches(regexOneDay) || date.matches(regexFewDays)) {
            return true;
        }
        else {
            return false;
        }
    }



    //один день или нет
    public boolean isOneDay(String date) {
        if (date.matches(regexOneDay)) {
            return true;
        }
        else return false;
    }



    //отделить даты
    public String[] splitDate(String dates) {
        return dates.split("-");
    }



    //возвращает строку в виде LocalDate
    public LocalDate getLocalDateFromString(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(date, formatter);
    }



    //возвращает все даты между 2-мя датами
    public List<LocalDate> getAllDaysBetweenTwoDates(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();
        while (!startDate.isAfter(endDate)) {
            dates.add(startDate);
            startDate = startDate.plusDays(1);
        }
        return dates;
    }



    public List<LocalDate> getAllDaysBetweenTwoDates(String dates) {

        String[] twoDates = splitDate(dates);
        LocalDate startDate = getLocalDateFromString(twoDates[0]);
        LocalDate endDate = getLocalDateFromString(twoDates[1]);

        List<LocalDate> listDates = new ArrayList<>();
        while (!startDate.isAfter(endDate)) {
            listDates.add(startDate);
            startDate = startDate.plusDays(1);
        }
        return listDates;
    }



    public boolean isCorrectPeriodOfDate(LocalDate dayStart, LocalDate dayEnd) {
        if (dayStart.isBefore(dayEnd)) {
            return true;
        }
        return false;
    }

}