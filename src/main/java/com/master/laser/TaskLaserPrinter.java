package com.master.laser;

import java.time.LocalDateTime;

public class TaskLaserPrinter {

    private long id;
    private String name;                //СВАО_А4_Обычная.pdf
    private String  status;             //true - OK
    private String format;              //A4, А3
    private int countCopy;              //2
    private String typeOfPaper;         //плотная, обычная
    private long countPage;             //55
    private String username;            //lukanin_ns
    private LocalDateTime dateTime;     //дата заявки
    private String printer;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getCountCopy() {
        return countCopy;
    }

    public void setCountCopy(int countCopy) {
        this.countCopy = countCopy;
    }

    public String getTypeOfPaper() {
        return typeOfPaper;
    }

    public void setTypeOfPaper(String typeOfPaper) {
        this.typeOfPaper = typeOfPaper;
    }

    public long getCountPage() {
        return countPage;
    }

    public void setCountPage(long countPage) {
        this.countPage = countPage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }
}
