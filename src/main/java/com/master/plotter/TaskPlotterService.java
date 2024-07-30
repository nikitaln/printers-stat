package com.master.plotter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskPlotterService {

    //парсинг веб-страницы HP
    public void parseWebPrinterStatistics(String path) {

        Connection connection = Jsoup.connect("http://npic5e08b/hp/device/webAccess/index.htm?content=accounting");
        try {
            AtomicInteger i = new AtomicInteger();
            Document document = connection.get();
            Elements elements = document.select("tr");
            elements.forEach(element -> {

                if (element.hasClass("treeTableTitleRowClosed treeTableTopLevel")) {

                    System.out.println(i + " " + element.text());
                    i.set(i.get() + 1);
                }


            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
