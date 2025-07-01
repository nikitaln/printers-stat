package com.master;

import com.master.gui.MainFormService;
import com.master.laser.TaskLaserPrinterDBConnection;
import com.master.laser.TaskLaserPrinterService;
import com.master.plotter.TaskPlotterDBConnection;
import com.master.plotter.TaskPlotterService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainForm {

    private JPanel mainJPanel;
    private JPanel leftJPanel;
    private JPanel rightJPanel;
    private JLabel leftMainLabel;
    private JLabel leftInputDateLabel;
    private JTextField leftDatePeriodTextField;
    private JTextField plotterGetDatePeriodTextField;
    private JLabel rightPanelMainLabel;
    private JLabel plotterDateLabel;
    private JButton laserButtonSaveInfoToDB;
    private JButton plotterGetAndSaveStatisticsButton;
    private JTextArea laserStatisticsTextArea;
    private JTextArea rightTextArea;
    private JButton plotterButtonGetInfoFromDB;
    private JButton laserGetStatisticsFromDB;
    private JLabel leftPathWithTxtFileLabel;
    private JTextField leftPathTextField;
    private JLabel leftTextLabelLastDate;
    private JPanel parseTxtFilePanel;
    private JPanel statisticsLaserPanel;
    private JPanel parseWebPlotterPagePanel;
    private JTextField plotterDatePeriodTextField;
    private JPanel statisticsPlotterPanel;
    private JTextArea plotterStatisticsTextArea;
    private JLabel plotterStatisticsLabel;
    private JLabel plotterDatePeriodLabel;
    private JLabel resultLabel;
    private JLabel nameResultLabel;
    private JTextField laserTextFieldLastDate;
    private JLabel laserLabelLastDate;
    private JTextField plotterLastDateTextField;
    private JLabel plotterLabelLastDate;

    private TaskPlotterService plotterService;
    private MainFormService mainFormService;
    private TaskPlotterDBConnection dbConnection;
    private TaskLaserPrinterDBConnection laserPrinterDBConnection;
    private TaskLaserPrinterService laserPrinterService;
    private TaskPlotterDBConnection taskPlotterDBConnection;


    public MainForm() {

        mainFormService = new MainFormService();
        plotterService = new TaskPlotterService();
        dbConnection = new TaskPlotterDBConnection();
        taskPlotterDBConnection = new TaskPlotterDBConnection();
        laserPrinterDBConnection = new TaskLaserPrinterDBConnection();


        //вывод последней даты отчета для Лазерного принтера
        laserTextFieldLastDate.setText(laserPrinterDBConnection.getLastDateTime());


        //вывод последней даты отчета для Плоттера
        plotterLastDateTextField.setText(taskPlotterDBConnection.getLastDateTime());


        //кнопка парсинга web-страницы отчетом
        /**
         * TODO: по нажатию кнопки сканировать страницу и добавлять не достающие отчеты
         */
        plotterGetAndSaveStatisticsButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String date = plotterGetDatePeriodTextField.getText();
                if (mainFormService.isCorrectDateFormat(date)) {

                    if (!mainFormService.isOneDay(date)) {
                        List<LocalDate> dateList = mainFormService.getAllDaysBetweenTwoDates(date);
                        plotterService.parseWebPrinterStatistics(dateList);
                    } else {
                        List<LocalDate> dateListWithOneDate = new ArrayList<>();
                        dateListWithOneDate.add(mainFormService.getLocalDateFromString(date));
                        plotterService.parseWebPrinterStatistics(dateListWithOneDate);
                    }
                }

                plotterService.printAllTasks();
                dbConnection.getConnection();
                dbConnection.addAllTaskPlotter(plotterService.getAllTasks());
                resultLabel.setForeground(Color.GREEN);
                resultLabel.setText("Выполнено");
            }
        });


        //получение статистики по печати на плоттере за период
        /**
         * TODO: доделать возможность отчета за 1 день + тестирование
         */
        plotterButtonGetInfoFromDB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("НАЖАЛИ BUTTON");
                String datePeriod = plotterDatePeriodTextField.getText();
                plotterStatisticsTextArea.setText(
                        "отчет за период: " + datePeriod + "\n\n" +
                        "Обычная = " + taskPlotterDBConnection.getCountOfThinPaperForPeriod(datePeriod) + " м.\n" +
                        "Плотная = " + taskPlotterDBConnection.getCountOfHeavyPaperForPeriod(datePeriod) + " м.\n" +
                        "Самоклейка = " + taskPlotterDBConnection.getCountOfAdhesivePaperForPeriod(datePeriod) + " м." );

            }
        });


        //test this field
        laserButtonSaveInfoToDB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textFromField = leftPathTextField.getText();
                laserPrinterService = new TaskLaserPrinterService();
                laserPrinterService.parseTxtFileStatisticsLaserPrinter(textFromField);

                long sumA3 = laserPrinterService.getSumA3Format();
                long sumA4 = laserPrinterService.getSumA4Format();

                laserStatisticsTextArea.setText("versant3100\n" +
                        "A3 = " + sumA3 + "\n" +
                        "A4 = " + sumA4
                );
            }
        });


        //получение статистики по печати на лазерных принтерах
        /**
         * Доделать кнопку
         */
        laserGetStatisticsFromDB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = leftDatePeriodTextField.getText();
                String A4_80gm = laserPrinterDBConnection.getStatisticsByDateFormat_A4(date);
                String A3_80gm = laserPrinterDBConnection.getStatisticsByDateFormat_A3_80gm(date);
                String A3_160gm = laserPrinterDBConnection.getStatisticsByDateFormat_A3_160gm(date);
                String A3_sum = laserPrinterDBConnection.getStatisticsByDateFormat_A3_All(date);




//                laserStatisticsTextArea.setText("Отчет за период: " + date + "\n\n" +
//                        "Xerox Versant\n" +
//                        "А4 (80гр) = " + "240 листов\n" +
//                        "А3 (80гр / 160гр) = " + "1200 / 100 листов\n\n" +
//                        "Xerox C75\n" +
//                        "А4 (80гр) = " + "240 листов\n" +
//                        "А3 (80гр / 160гр) = " + "1200 / 100 листов\n\n" +
//                        "Итого:\n" +
//                        "А4 (80гр) = " + "1000 листов\n" +
//                        "А3 (80гр / 160гр) = " + "1200 / 100 листов\n");
//
                laserStatisticsTextArea.setText(
                        "Результат за период: " + date + "\n\n" +
                        "А4 = " + A4_80gm + " листов\n" +
                        "А3 = " + A3_sum + " листов");

            }
        });
    }


    public JPanel getMainJPanel() {
        return mainJPanel;
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
