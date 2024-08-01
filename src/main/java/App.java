import com.master.plotter.TaskPlotterService;


public class App {

    private static String hpPath = "http://npic5e08b/hp/device/webAccess/index.htm;jsessionid=blir95p7c1?content=accounting";
    static TaskPlotterService plotterService;

    public static void main(String[] args) {

        plotterService = new TaskPlotterService();
        plotterService.parseWebPrinterStatistics(hpPath);

    }
}
