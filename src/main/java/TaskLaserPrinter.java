import java.time.LocalDateTime;

public class TaskLaserPrinter {

    private long id;
    private String dateTime;     //дата заявки
    private String name;                //СВАО_А4_Обычная.pdf
    private boolean status;             //true - OK
    private String format;              //A4, А3
    private int countCopy;              //2
    private String typeOfPaper;         //плотная, обычная
    private String stylePrint;          //фальцевание, без
    private long countPage;             //55
    private String username;            //lukanin_ns

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getCountCopy() {
        return countCopy;
    }

    public void setCountCopy(int countCopy) {
        this.countCopy = countCopy;
    }

    public String getTypeOfPaper() {
        return typeOfPaper;
    }

    public void setTypeOfPaper(String typeOfPaper) {
        this.typeOfPaper = typeOfPaper;
    }

    public String getStylePrint() {
        return stylePrint;
    }

    public void setStylePrint(String stylePrint) {
        this.stylePrint = stylePrint;
    }

    public long getCountPage() {
        return countPage;
    }

    public void setCountPage(long countPage) {
        this.countPage = countPage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
