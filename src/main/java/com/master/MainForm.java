package com.master;

import com.master.gui.MainFormService;
import com.master.plotter.TaskPlotterService;
import com.master.plotter.TaskPlotterStorage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class MainForm {
    private JPanel mainJPanel;
    private JPanel leftJPanel;
    private JPanel rightJPanel;
    private JLabel leftMainLabel;
    private JLabel leftInputDateLabel;
    private JTextField textField1;
    private JTextField rightDateTextField;
    private JLabel rightMainLabel;
    private JLabel rightInputDateLabel;
    private JButton leftButtonSearch;
    private JButton rightButtonSearch;
    private JTextArea leftTextArea;
    private JTextArea rightTextArea;
    private JButton rightSaveDbButton;
    private JButton leftSaveDbButton;
    private JLabel leftPathWithTxtFileLabel;
    private JTextField leftPathTextField;

    private TaskPlotterService plotterService;
    private MainFormService mainFormService;



    public MainForm() {

        mainFormService = new MainFormService();
        plotterService = new TaskPlotterService();

        rightButtonSearch.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                rightTextArea.setText("");
                String date = rightDateTextField.getText();
                if (mainFormService.isCorrectDateFormat(date)) {
                    List<LocalDate> dateList = mainFormService.getAllDaysBetweenTwoDates(date);
                    plotterService.parseWebPrinterStatistics(dateList);
                }

                rightTextArea.append("Отчет за период: " + date + "\n\n"
                        + "Плотная бумага=" + plotterService.getLengthHeavyPaper() + "м" + "\n"
                        + "Обычная бумага=" + plotterService.getLengthThinPaper() + "м");
            }
        });
    }



    public JPanel getMainJPanel() {
        return mainJPanel;
    }


}
