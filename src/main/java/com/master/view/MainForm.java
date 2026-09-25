package com.master.view;

import com.master.gui.DateService;
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
    private JButton plotterSaveStatisticsButton;
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
    private JComboBox comboBoxSelectPlotter;

    private DateService mainFormService;

    //--------------------------------------------------
    private TaskLaserPrinterService laserPrinterService;
    private TaskPlotterService plotterService;



    public MainForm(TaskLaserPrinterService laserPrinterService, TaskPlotterService plotterService) {
        this.laserPrinterService = laserPrinterService;
        this.plotterService = plotterService;

        mainFormService = new DateService();


        //1. рисуем окно с пустыми/placeholder-значениями
        initUI();

        //2. выносим подписку на события выносят в отдельный метод initListeners(),
        //  а сами обработчики — в ещё отдельные методы.
        initListeners();

        //3. грузим данные из БД асинхронно, ПОСЛЕ отрисовки
        loadInitialData();


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
        plotterSaveStatisticsButton.addActionListener(new ActionListener() {

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



        //получение статистики по печати на лазерных принтерах
        /**
         * Доделать кнопку
         */
//        laserGetStatisticsFromDB.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                //База сотрудников
//                Map<String, String> people = new HashMap<>();
//                people.put("Климов", "klimov_vu");
//                people.put("Биглер", "Biegler");
//                people.put("Есина", "esina_sv");
//                people.put("Гридасов", "ogridasov");
//                people.put("Луканин", "lukanin_ns");
//
//                //Берем дату отчета
//                String date = leftDatePeriodTextField.getText();
//
//                //Проверка выпадающего списка: 1-все, 2-Климов, 3-Биглер
//                String resultItem = comboBox1.getSelectedItem().toString();
//                String domainName = people.get(resultItem);
//
//                String A4_80gm = "";
//                String A3_80gm = "";
//                String A3_160gm = "";
//                String Files_Sum = "";
//
//                if (resultItem.equals("Все")) {
//                    A4_80gm = laserPrinterDBConnection.getFullStatisticsByDateFormat_A4_80gm(date);
//                    A3_80gm = laserPrinterDBConnection.getFullStatisticsByDateFormat_A3_80gm(date);
//                    A3_160gm = laserPrinterDBConnection.getFullStatisticsByDateFormat_A3_160gm(date);
//                } else {
//                    A4_80gm = laserPrinterDBConnection.getStatisticsByDateByNameFormat_A4_80gm(date, domainName);
//                    A3_80gm = laserPrinterDBConnection.getStatisticsByDateByNameFormat_A3_80gm(date, domainName);
//                    A3_160gm = laserPrinterDBConnection.getStatisticsByDateByNameFormat_A3_160gm(date, domainName);
//                    Files_Sum = laserPrinterDBConnection.getCountFilesByName(date, domainName);
//                }
//
//                //Заполнение текстового поля
//                laserStatisticsTextArea.setText(
//                        "Отчет за период = " + date + "\n" +
//                                "Сотрудник(и) = " + resultItem + "\n" +
//                                "А4 80гр = " + A4_80gm + "\n" +
//                                "А3 80гр = " + A3_80gm + "\n" +
//                                "А3 160гр = " + A3_160gm + "\n" +
//                                "Кол-во файлов = " + Files_Sum + " шт."
//                );
//            }
//        });


        getStatFromPlotter();
    }


    private void initUI() {
        setSize(900, 600);
        setTitle("Статистика печати на принтерах");
        add(getMainJPanel());

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);    //окно появляется в центре монитора
        //frame.pack();

        laserTextFieldLastDate.setText("идет загрузка...");
        plotterLastDateTextField.setText("идет загрузка...");

    }

    //обрабатываем события нажатий
    private void initListeners() {
        //1. сохранить данные в БД для лазера
        laserButtonSaveInfoToDB.addActionListener(e -> onLaserSaveData());
        //2. взять отчет из БД для лазера
        //laserGetStatisticsFromDB.addActionListener();
        //3. сохранить статистику плоттера
        plotterSaveStatisticsButton.addActionListener(e -> onPlotterSaveData());
        //4. взять отчет из БД для плоттера
       //plotterButtonGetInfoFromDB.addActionListener();
    }

//_________________В РАЗРАБОТКЕ КНОПКА СОХРАНЕНИЯ ДЛЯ ЛАЗЕРНОГО ПРИНТЕРА________________________________________________
    /**
     * В РАЗРАБОТКЕ КНОПКА СОХРАНЕНИЯ ДАННЫХ ДЛЯ ЛАЗЕРНЫХ ПРИНТЕРОВ
     */
    private void onLaserSaveData() {
        //Заблокировали кнопку
        setBusy(true);

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                //вызываем сервис -> затем DAO -> Возвращаем данные
                System.out.println("PRESS BUTTON 1");
                String textFromField = leftPathTextField.getText();
                laserPrinterService.parseTxtFileStatisticsLaserPrinter(textFromField);
                System.out.println("Файлы добавлены в БД");
                return "";  // фоновый поток
            }
            @Override
            protected void done() {
                setBusy(false);
                try {
                    // метод get() - вернет результат работы в методе doInBackground()
                    System.out.println("done() = выводить результат на панель");
                }             // EDT
                catch (Exception ex) { /* ... */ }
            }
        }.execute();
    }


    //Блокировка кнопки на время выполнения команды
    private void setBusy(boolean busy) {
        laserButtonSaveInfoToDB.setEnabled(!busy);
    }

//___________________НЕ ТРОГАТЬ_________________________________________________________________________________________

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







//-------------------------КНОПКА СОХРАНИТЬ ПАРСИНГ ОТЧЕТА ПЛОТТЕРА

    private void onPlotterSaveData() {
        //Заблокировали кнопку
//        setBusy(true);

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                //вызываем сервис -> затем DAO -> Возвращаем данные
                System.out.println("Нажатие на кнопку SavePlotterData");
                String date = plotterGetDatePeriodTextField.getText();
                plotterService.

                return "";  // фоновый поток
            }
            @Override
            protected void done() {
                setBusy(false);
                try {
                    // метод get() - вернет результат работы в методе doInBackground()

                }             // EDT
                catch (Exception ex) { /* ... */ }
            }
        }.execute();
    }

//-----------------------------------------------------------------




}
