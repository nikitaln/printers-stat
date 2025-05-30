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
    private JTextArea leftTextArea;
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


        // test this field
        laserButtonSaveInfoToDB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textFromField = leftPathTextField.getText();
                laserPrinterService = new TaskLaserPrinterService();
                laserPrinterService.parseTxtFileStatisticsLaserPrinter(textFromField);

                long sumA3 = laserPrinterService.getSumA3Format();
                long sumA4 = laserPrinterService.getSumA4Format();

                leftTextArea.setText("versant3100\n" +
                        "A3 = " + sumA3 + "\n" +
                        "A4 = " + sumA4
                );
            }
        });


        laserGetStatisticsFromDB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leftTextArea.setText("Формат A4 = " + laserPrinterDBConnection.getStatisticsByDateFormatA4(leftDatePeriodTextField.getText()) + "\n" +
                        "Формат A3 = " + laserPrinterDBConnection.getStatisticsByDateFormatA3());
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
