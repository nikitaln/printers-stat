import com.master.plotter.TaskPlotterService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class App {

    static TaskPlotterService plotterService;

    public static void main(String[] args) {
//        TaskPlotter taskPlotter = new TaskPlotter();
//        taskPlotter.parsingTxtFile("C:\\Users\\lukanin_ns\\Downloads\\xl80002.txt");
        plotterService = new TaskPlotterService();
        plotterService.parseWebPrinterStatistics("hello");
    }





//    private static String path = "C:\\Users\\lukanin_ns\\Desktop\\test-java-folder\\versant_stat_2406_3006.txt";
//    private static Storage storage;
//    private static com.master.db.SqlConnection sqlConnection;
//
//    public static void main(String[] args) {
//        sqlConnection = new com.master.db.SqlConnection();
//        sqlConnection.createTableForLaserPrinter();
//        parsingTxtFile(path);
//    }
//
//
//    public static void parsingTxtFile(String path) {
//
//        int id = 0;
//
//        //парсинг файла-журнала статистики
//        File file = new File(path);
//        try {
//            List<String> lines = Files.readAllLines(Paths.get(path));
//
//            for (String line : lines) {
//
//                /**TODO
//                 * delete first line for good parsing this file
//                 */
//
//                String[] fragments = line.split("\t");
//
//                //[0] - дата и время
//                //[1] - имя файла
//                //[2] - статус
//                //[3] - пользователь
//                //[4] - кол-во страниц
//                //[5] - общее количество напечатанных черно-белых страниц
//                //[6] - общее количество напечатанных цветных страниц
//                //[7] - количество напечатанных копий задания
//                //[8] - страницы
//                //[9] - формат страницы (могут быть пустые строки)
//                //[10] - тип материала для печати (могут быть пустые строки)
//
//                System.out.println("Дата: " + fragments[1] + " | " + "Кол-во стр: " + fragments[4] + " | " + "Пользователь: " + fragments[3]);
//                com.master.laser.TaskLaserPrinter taskLaserPrinter = new com.master.laser.TaskLaserPrinter();
//
//                id = id + 1;
//                taskLaserPrinter.setId(id);
//                taskLaserPrinter.setDateTime(fragments[0]);
//                taskLaserPrinter.setName(fragments[1]);
//                taskLaserPrinter.setStatus(true);
//                taskLaserPrinter.setFormat("A3");
//                taskLaserPrinter.setCountCopy(Integer.parseInt(fragments[7]));
//                taskLaserPrinter.setTypeOfPaper("обычная");
//                taskLaserPrinter.setStylePrint("фальц");
//                taskLaserPrinter.setCountPage(Long.parseLong(fragments[4]));
//                taskLaserPrinter.setUsername(fragments[3]);
//
//                sqlConnection.addTaskLaserPrinter(taskLaserPrinter);
//
//                System.out.println("success to add task");
//
//
//
//                /**TODO
//                 * create com.master.laser.TaskLaserPrinter object and ADD to Storage Collection
//                 * create Database connection with JDBC-driver
//                 * create Database (print_center)
//                 * create two tables (plotter_tasks, laser_tasks)
//                 */
//
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }


    public static void createTypeOfPaper() {

    }
}
