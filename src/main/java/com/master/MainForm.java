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
    private JTextArea textArea1;
    private JTextArea textArea2;
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
                String date = rightDateTextField.getText();
                if (mainFormService.isCorrectDateFormat(date)) {
                    List<LocalDate> dateList = mainFormService.getAllDaysBetweenTwoDates(date);
                    plotterService.parseWebPrinterStatistics(dateList);
                }

                textArea2.append("Плотная бумага = " + plotterService.getLengthHeavyPaper() + "м");
            }
        });
    }



    public JPanel getMainJPanel() {
        return mainJPanel;
    }


}
