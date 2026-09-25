import com.master.dao.TaskLaserPrinterDao;
import com.master.dao.TaskPlotterDao;
import com.master.db.DbConnection;
import com.master.plotter.TaskPlotterService;
import com.master.view.MainForm;
import com.master.laser.TaskLaserPrinterService;

import javax.swing.*;
import java.sql.Connection;


public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                Connection conn = DbConnection.get();

                TaskLaserPrinterDao laserPrinterDao = new TaskLaserPrinterDao(conn);
                TaskPlotterDao plotterDao = new TaskPlotterDao(conn);

                TaskLaserPrinterService laserPrinterService = new TaskLaserPrinterService(laserPrinterDao);
                TaskPlotterService plotterService = new TaskPlotterService(plotterDao);

                MainForm form = new MainForm(laserPrinterService, plotterService);
                form.setVisible(true);

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try { conn.close(); } catch (Exception ignored) {}
                }));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Ошибка запуска: " + ex.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
