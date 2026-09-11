package com.master.view;

import com.master.gui.MainFormService;
import com.master.laser.TaskLaserPrinterDBConnection;
import com.master.laser.TaskLaserPrinterService;
import com.master.plotter.TaskPlotterDBConnection;
import com.master.plotter.TaskPlotterService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainForm extends JFrame {

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

        //1. рисуем окно с пустыми/placeholder-значениями
        initUI();

        //2. выносим подписку на события выносят в отдельный метод initListeners(),
        //  а сами обработчики — в ещё отдельные методы.
        initListeners();

        //3. грузим данные из БД асинхронно, ПОСЛЕ отрисовки
        loadInitialData();




        mainFormService = new MainFormService();
        plotterService = new TaskPlotterService();
        dbConnection = new TaskPlotterDBConnection();
        taskPlotterDBConnection = new TaskPlotterDBConnection();
        laserPrinterDBConnection = new TaskLaserPrinterDBConnection();


//        //вывод последней даты отчета для Лазерного принтера
//        laserTextFieldLastDate.setText(laserPrinterDBConnection.getLastDateTime());
//
//
//        //вывод последней даты отчета для Плоттера
//        plotterLastDateTextField.setText(taskPlotterDBConnection.getLastDateTime());

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
//        plotterButtonGetInfoFromDB.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("НАЖАЛИ BUTTON");
//                String datePeriod = plotterDatePeriodTextField.getText();
//                plotterStatisticsTextArea.setText(
//                        "отчет за период: " + datePeriod + "\n\n" +
//                                "Обычная = " + taskPlotterDBConnection.getCountOfThinPaperForPeriod(datePeriod) + " м.\n" +
//                                "Плотная = " + taskPlotterDBConnection.getCountOfHeavyPaperForPeriod(datePeriod) + " м.\n" +
//                                "Самоклейка = " + taskPlotterDBConnection.getCountOfAdhesivePaperForPeriod(datePeriod) + " м.\n" +
//                                "Кол-во файлов = " + taskPlotterDBConnection.getSumFiles(datePeriod) + " шт.");
//
//            }
//        });


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
                String Files_Sum = "";

                if (resultItem.equals("Все")) {
                    A4_80gm = laserPrinterDBConnection.getFullStatisticsByDateFormat_A4_80gm(date);
                    A3_80gm = laserPrinterDBConnection.getFullStatisticsByDateFormat_A3_80gm(date);
                    A3_160gm = laserPrinterDBConnection.getFullStatisticsByDateFormat_A3_160gm(date);
                } else {
                    A4_80gm = laserPrinterDBConnection.getStatisticsByDateByNameFormat_A4_80gm(date, domainName);
                    A3_80gm = laserPrinterDBConnection.getStatisticsByDateByNameFormat_A3_80gm(date, domainName);
                    A3_160gm = laserPrinterDBConnection.getStatisticsByDateByNameFormat_A3_160gm(date, domainName);
                    Files_Sum = laserPrinterDBConnection.getCountFilesByName(date, domainName);
                }

                //Заполнение текстового поля
                laserStatisticsTextArea.setText(
                        "Отчет за период = " + date + "\n" +
                                "Сотрудник(и) = " + resultItem + "\n" +
                                "А4 80гр = " + A4_80gm + "\n" +
                                "А3 80гр = " + A3_80gm + "\n" +
                                "А3 160гр = " + A3_160gm + "\n" +
                                "Кол-во файлов = " + Files_Sum + " шт."
                );
            }
        });


        getStatFromPlotter();
    }




    //рисуем главное окно с компонентами
    public void initUI() {
        setSize(900, 600);
        setTitle("Статистика печати на принтерах");
        add(getMainJPanel());

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);    //окно появляется в центре монитора
        //frame.pack();

        laserTextFieldLastDate.setText("идет загрузка...");
        plotterLastDateTextField.setText("идет загрузка...");

    }

    private void initListeners() {}

    private void loadInitialData() {

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


    private void getStatFromPlotter() {
        plotterButtonGetInfoFromDB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("PRESS BUTTON GET STAT FROM PLOTTER");
            }
        });
    }
}
