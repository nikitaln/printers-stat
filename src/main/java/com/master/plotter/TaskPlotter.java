package com.master.plotter;

import java.time.LocalDateTime;


public class TaskPlotter {

    private int id;
    private String taskName;
    private String taskType;
    private String source;
    private String output;
    private String status;
    private int copyCount;
    private String paperType;
    private double paperConsumption;
    private double paperLengthConsumption;
    private double tonerConsumption;
    private String user;
    private LocalDateTime dateTime;
    private String printType;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCopyCount() {
        return copyCount;
    }

    public void setCopyCount(int copyCount) {
        this.copyCount = copyCount;
    }

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public double getPaperConsumption() {
        return paperConsumption;
    }

    public void setPaperConsumption(double paperConsumption) {
        this.paperConsumption = paperConsumption;
    }

    public double getPaperLengthConsumption() {
        return paperLengthConsumption;
    }

    public void setPaperLengthConsumption(double paperLengthConsumption) {
        this.paperLengthConsumption = paperLengthConsumption;
    }

    public double getTonerConsumption() {
        return tonerConsumption;
    }

    public void setTonerConsumption(double tonerConsumption) {
        this.tonerConsumption = tonerConsumption;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    @Override
    public String toString() {
        return "TaskPlotter{" +
                "taskName='" + taskName + '\'' +
                ", taskType='" + taskType + '\'' +
                ", source='" + source + '\'' +
                ", output='" + output + '\'' +
                ", status='" + status + '\'' +
                ", copyCount=" + copyCount +
                ", paperType='" + paperType + '\'' +
                ", paperConsumption=" + paperConsumption +
                ", paperLengthConsumption=" + paperLengthConsumption +
                ", tonerConsumption=" + tonerConsumption +
                ", user='" + user + '\'' +
                ", dateTime=" + dateTime +
                ", printType='" + printType + '\'' +
                '}';
    }

}
