import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        String downloadDirectory = "E:\\test2\\";
        try {
            Document doc = Jsoup.connect("https://lenta.ru").get();
            Elements img = doc.select("img[src~=(?i)\\.(png|jpe?g)]");
            for(Element element : img) {
                logger.info("Получена ссылка {}", element.attr("abs:src"));
                downloadFromURL(element.attr("abs:src"), downloadDirectory);
            }
            System.out.println("Копирование успешно выполнено. Скопировано " + img.size() + " элементов");

        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        }
    }
    public static void downloadFromURL (String urlStr, String downloadDirectory) throws IOException {
        URL url = new URL(urlStr);
        String fileName = urlStr.substring(urlStr.lastIndexOf("/")+1);;
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(downloadDirectory + fileName);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        logger.info("Создан файл {}", downloadDirectory + fileName);
        fos.close();
        rbc.close();
    }

}
