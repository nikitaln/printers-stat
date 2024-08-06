import com.master.MainForm;
import com.master.plotter.TaskPlotterService;

import javax.swing.*;


public class App {

    private static String hpPath = "http://npic5e08b/hp/device/webAccess/index.htm;jsessionid=blir95p7c1?content=accounting";
    static TaskPlotterService plotterService;

    public static void main(String[] args) {

//        plotterService = new TaskPlotterService();
//        plotterService.parseWebPrinterStatistics(hpPath);

        JFrame frame = new JFrame();
        frame.setSize(600, 400);
        frame.setTitle("Printers-Stat");

        MainForm mf = new MainForm();
        frame.add(mf.getMainJPanel());

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}
