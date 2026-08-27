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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private JComboBox comboBox1;

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

        initComboBox();


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

                plotterLastDateTextField.setText(taskPlotterDBConnection.getLastDateTime());

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
                        "Самоклейка = " + taskPlotterDBConnection.getCountOfAdhesivePaperForPeriod(datePeriod) + " м.\n" +
                        "Кол-во файлов = " + taskPlotterDBConnection.getSumFiles(datePeriod) + " шт.");

            }
        });


        //test this field
        laserButtonSaveInfoToDB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textFromField = leftPathTextField.getText();
                laserPrinterService = new TaskLaserPrinterService();
                laserPrinterService.parseTxtFileStatisticsLaserPrinter(textFromField);
                laserTextFieldLastDate.setText(laserPrinterDBConnection.getLastDateTime());

                long sumA3 = laserPrinterService.getSumA3Format();
                long sumA4 = laserPrinterService.getSumA4Format();

                laserStatisticsTextArea.setText("Результат\n" +
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
                //База сотрудников
                Map<String, String> people = new HashMap<>();
                people.put("Климов", "klimov_vu");
                people.put("Биглер", "Biegler");
                people.put("Есина", "esina_sv");
                people.put("Гридасов", "ogridasov");
                people.put("Луканин", "lukanin_ns");

                //Берем дату отчета
                String date = leftDatePeriodTextField.getText();

                //Проверка выпадающего списка: 1-все, 2-Климов, 3-Биглер
                String resultItem = comboBox1.getSelectedItem().toString();
                String domainName = people.get(resultItem);

                String A4_80gm = "";
                String A3_80gm = "";
                String A3_160gm = "";

                if (resultItem.equals("Все")) {
                    A4_80gm = laserPrinterDBConnection.getFullStatisticsByDateFormat_A4_80gm(date);
                    A3_80gm = laserPrinterDBConnection.getFullStatisticsByDateFormat_A3_80gm(date);
                    A3_160gm = laserPrinterDBConnection.getFullStatisticsByDateFormat_A3_160gm(date);
                } else {
                    A4_80gm = laserPrinterDBConnection.getStatisticsByDateByNameFormat_A4_80gm(date, domainName);
                    A3_80gm = laserPrinterDBConnection.getStatisticsByDateByNameFormat_A3_80gm(date, domainName);
                    A3_160gm = laserPrinterDBConnection.getStatisticsByDateByNameFormat_A3_160gm(date, domainName);
                }

                //Заполнение текстового поля
                laserStatisticsTextArea.setText("Отчет за период = " + date);
                laserStatisticsTextArea.setText("Сотрудник(и) = " + resultItem);
                laserStatisticsTextArea.setText("А4 80гр = " + A4_80gm);
                laserStatisticsTextArea.setText("А3 80гр = " + A3_80gm);
                laserStatisticsTextArea.setText("А3 160гр = " + A3_160gm);

                laserStatisticsTextArea.setText(
                        "Отчет за период = " + date + "\n" +
                        "Сотрудник(и) = " + resultItem + "\n" +
                        "А4 80гр = " + A4_80gm + "\n" +
                        "А3 80гр = " + A3_80gm + "\n" +
                        "А3 160гр = " + A3_160gm
                );

                //10.08.2026-16.08.2026 - для проверки

//                String A4_80gm = laserPrinterDBConnection.getStatisticsByDateFormat_A4(date);
//                String A3_80gm = laserPrinterDBConnection.getStatisticsByDateFormat_A3_80gm(date);
//                String A3_160gm = laserPrinterDBConnection.getStatisticsByDateFormat_A3_160gm(date);
//                String A3_sum = laserPrinterDBConnection.getStatisticsByDateFormat_A3_All(date);
//                String filesSum = laserPrinterDBConnection.getCountFiles(date);
//
//
//
//
////                laserStatisticsTextArea.setText("Отчет за период: " + date + "\n\n" +
////                        "Xerox Versant\n" +
////                        "А4 (80гр) = " + "240 листов\n" +
////                        "А3 (80гр / 160гр) = " + "1200 / 100 листов\n\n" +
////                        "Xerox C75\n" +
////                        "А4 (80гр) = " + "240 листов\n" +
////                        "А3 (80гр / 160гр) = " + "1200 / 100 листов\n\n" +
////                        "Итого:\n" +
////                        "А4 (80гр) = " + "1000 листов\n" +
////                        "А3 (80гр / 160гр) = " + "1200 / 100 листов\n");
////
//                laserStatisticsTextArea.setText(
//                        "Результат за период: " + date + "\n\n" +
//                        "А4 = " + A4_80gm + " листов\n" +
//                        "А3 = " + A3_sum + " листов\n" +
//                        "Кол-во файлов = " + filesSum + " штук");

            }
        });
    }


    public void initComboBox() {


        System.out.println("initComboBox");
        comboBox1.addItem("Все");
        comboBox1.addItem("Климов");
        comboBox1.addItem("Биглер");
        comboBox1.addItem("Есина");
        comboBox1.addItem("Гридасов");
        comboBox1.addItem("Луканин");

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
