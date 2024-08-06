package com.master;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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



    public MainForm() {
        rightButtonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(rightDateTextField.getText());
            }
        });
    }



    public JPanel getMainJPanel() {
        return mainJPanel;
    }


}
