package com.master.plotter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TaskPlotterStorage {

    private List<TaskPlotter> taskPlotterList = new ArrayList<>();



    public void addTaskPlotter(TaskPlotter taskPlotter) {
        taskPlotterList.add(taskPlotter);
    }



    public void printAllTaskPlotter() {
        for (TaskPlotter task : taskPlotterList) {
            System.out.println(task.toString());
        }
    }



    public List<TaskPlotter> getAllPlotterTasks() {
        return taskPlotterList.stream().toList();
    }



    public double getLengthHeavyPaper() {
        double length = 0;

        for (TaskPlotter task : taskPlotterList) {
            if (task.getPaperType().contains("особоплотная бумага")) {
                length = length + task.getPaperLengthConsumption();
            }
        }

        double roundedNumber = Math.round(length * 100.0) / 100.0;
        System.out.println("Общая длина плотная бум=" + roundedNumber);
        return roundedNumber;
    }



    public double getLengthThinPaper() {
        double length = 0;

        for (TaskPlotter task : taskPlotterList) {
            if (task.getPaperType().contains("обычная") || task.getPaperType().contains("Обычная")) {
                length = length + task.getPaperLengthConsumption();
            }
        }

        double roundedNumber = Math.round(length * 100.0) / 100.0;
        System.out.println("Общая длина тонкая бум=" + roundedNumber);
        return roundedNumber;
    }
}
