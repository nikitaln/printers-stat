package com.master.view;

import com.master.gui.DateService;
import com.master.laser.TaskLaserPrinterService;
import com.master.plotter.TaskPlotterService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm extends JFrame {

    private JPanel mainJPanel;
    private JPanel leftJPanel;
    private JPanel rightJPanel;
    private JLabel leftMainLabel;
    private JLabel leftInputDateLabel;
    private JTextField leftDatePeriodTextField;
    private JTextField plotterGetDatePeriodTextField;
    private JLabel rightPanelMainLabel;
    private JLabel plotterSelectLabel;
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
    private JComboBox comboBox2;
    private JLabel plotterDateLabel;

    private DateService mainFormService;

    private TaskLaserPrinterService laserPrinterService;
    private TaskPlotterService plotterService;

    //Конструктор
    public MainForm(TaskLaserPrinterService laserPrinterService, TaskPlotterService plotterService) {
        this.laserPrinterService = laserPrinterService;
        this.plotterService = plotterService;

        //1. рисуем окно с пустыми/placeholder-значениями
        initUI();

        //2. выносим подписку на события выносят в отдельный метод initListeners(),
        //  а сами обработчики — в ещё отдельные методы.
        initListeners();

        //3. грузим данные из БД асинхронно, ПОСЛЕ отрисовки
        loadInitialData();

        initComboBox();
        initComboBox2();


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


    //1. инициализация фрейм
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


    //2. обрабатываем события нажатий
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


    //3. загрузка данных из БД
    private void loadInitialData() {

        new SwingWorker<LastDates, Void>() {

            @Override
            protected LastDates doInBackground() throws Exception {
                // ФОНОВЫЙ ПОТОК: оба запроса последовательно
                String laserDate = laserPrinterService.getLastDateForDisplay();
                String plotterDate = plotterService.getPlotterLastDateForDisplay();
                return new LastDates(laserDate, plotterDate);
            }

            @Override
            protected void done() {
                // EDT: обновляем UI
                try {
                    LastDates data = get();
                    System.out.println(data.plotterDate);
                    laserTextFieldLastDate.setText(data.laserDate());
                    plotterLastDateTextField.setText(data.plotterDate());
                } catch (Exception ex) {
                    laserTextFieldLastDate.setText("ошибка загрузки");
                    plotterLastDateTextField.setText("ошибка загрузки");
                    showError("Не удалось загрузить данные: " + ex.getMessage());
                }
            }
        }.execute();

    }




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

    private void showError(String s) {
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

    public void initComboBox2() {
        comboBox2.addItem("XL8000 (2017)");
        comboBox2.addItem("XL8000 (2021)");
        comboBox2.addItem("XL5100 (2018)");
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

    private void onPlotterSaveData() {
        //Заблокировали кнопку
//        setBusy(true);

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                //вызываем сервис -> затем DAO -> Возвращаем данные
                System.out.println("Нажатие на кнопку SavePlotterData");
                String date = plotterGetDatePeriodTextField.getText();
                int plotterName = comboBox2.getSelectedIndex();
                System.out.println(plotterName);
                System.out.println(date);
                plotterService.parseWebPageAndCheckData(date, plotterName);
                return "";  // фоновый поток
            }
            @Override
            protected void done() {
                setBusy(false);
                try {
                    // метод get() - вернет результат работы в методе doInBackground()
                    resultLabel.setForeground(Color.GREEN);
                    resultLabel.setText("Выполнено");
                }             // EDT
                catch (Exception ex) { /* ... */ }
            }
        }.execute();
    }

    private record LastDates(String laserDate, String plotterDate) {
        
    }


}
