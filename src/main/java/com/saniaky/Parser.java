package com.saniaky;

import de.jetwick.snacktory.HtmlFetcher;
import de.jetwick.snacktory.JResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author saniaky
 * @since 12/4/16
 */
public class Parser {

//    private static final String URLS_TXT = "urls.txt";
    private static final String URLS_TXT = "urls-02.12.2016.txt";

    private static final int ARTICLES_PER_FILE = 100;

    private static final int IMAGE_MAX_WIDTH_PX = 200;
    private static final int SLEEP_TIMEOUT = 5000;
    private static final int MAX_FAILED_COUNT = 20;
    private static final int RESOLVE_TIMEOUT = 30000;
    private static final String TITLE_STYLE = "Title";
    private static final String TEMPLATE_FILE = "template.docx";
    private static final String TEMP_FILE = "targetFile.tmp";

    public static void main(String[] args) throws IOException, InvalidFormatException, InterruptedException {
        Parser parser = new Parser();
        List<String> urls = parser.getUrls();
        HtmlFetcher fetcher = new HtmlFetcher();
        XWPFDocument document = createDocument();

        int fileNumber = 1;
        int articlesNumber = 0;
        int failedCount = 0;
        int totalFailedCount = 0;
        List<String> failedUrls = new ArrayList<>();

        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i);

            JResult result;
            try {
                result = fetcher.fetchAndExtract(url, RESOLVE_TIMEOUT, true);
            } catch (Exception e) {
                if (e.getMessage().equals("Invalid Http response") && failedCount < MAX_FAILED_COUNT) {
                    i--;
                    failedCount++;
                    System.err.println("Не получается загрузить статью: " + url + ", попытка " + failedCount);
                    Thread.sleep(SLEEP_TIMEOUT);
                } else {
                    System.err.println("Пропущена статья - " + url + ", Error: " + e.getMessage());
                    failedCount = 0;
                    totalFailedCount++;
                    failedUrls.add(url);
                }
                continue;
            }
            failedCount = 0;

            parser.addArticle(document, result);
            articlesNumber++;

            if (articlesNumber % ARTICLES_PER_FILE == 0) {
                save(document, getFilename(fileNumber));
                document = createDocument();
                articlesNumber = 0;
                fileNumber++;
            }

            System.out.println(String.format("%s-%s: %s (%s)", fileNumber, articlesNumber, result.getTitle(), result.getUrl()));
        }

        if (articlesNumber != 0) {
            save(document, getFilename(fileNumber));
        }

        System.out.println("Всего пропущено статей: " + totalFailedCount);
        for (String failedUrl : failedUrls) {
            System.out.println(failedUrl);
        }
        // TODO AKoh write to file failedUrls.txt
    }

    private static String getFilename(int fileNum) {
        return String.format("Стартапы %d.docx", fileNum);
    }

    private static XWPFDocument createDocument() throws IOException {
        return new XWPFDocument(new FileInputStream(TEMPLATE_FILE));
    }

    private void addArticle(XWPFDocument document, JResult article) {
        addTitle(document, article);
        addImageIfExist(document, article);
        addText(document, article);
    }

    private void addText(XWPFDocument document, JResult article) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun textRun = paragraph.createRun();
        textRun.setText(article.getText());
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        textRun.addBreak(BreakType.PAGE);
    }

    private void addTitle(XWPFDocument document, JResult article) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setStyle(TITLE_STYLE);
        XWPFRun title = paragraph.createRun();
        title.setText(article.getTitle());
    }

    private void addImageIfExist(XWPFDocument document, JResult article) {
        String imageUrl = article.getImageUrl();
        if (StringUtils.isNotEmpty(imageUrl)) {
            try {
                // Save image to file
                InputStream imageStream = new URL(imageUrl).openStream();
                File targetFile = new File(TEMP_FILE);
                FileUtils.copyInputStreamToFile(imageStream, targetFile);

                BufferedImage bufferedImage = ImageIO.read(FileUtils.openInputStream(targetFile));
                if (bufferedImage != null) {
                    int width = bufferedImage.getWidth();
                    int height = bufferedImage.getHeight();

                    // Scale image
                    if (width > IMAGE_MAX_WIDTH_PX) {
                        double ratio = (double) height / width;
                        width = IMAGE_MAX_WIDTH_PX;
                        height = (int) (ratio * width);
                    }

                    // Detect picture type
                    int pictureType = getPictureType(targetFile);
                    if (pictureType != -1) {

                        // Add image to doc
                        XWPFParagraph paragraph = document.createParagraph();
                        XWPFRun link = paragraph.createRun();
                        link.addPicture(
                                FileUtils.openInputStream(targetFile),
                                pictureType,
                                imageUrl,
                                Units.toEMU(width), Units.toEMU(height));
                    }
                }
            } catch (IOException | ArrayIndexOutOfBoundsException | InvalidFormatException e) {
                System.err.println("Не получается добавить изображение: " + imageUrl + ", Error: " + e.getMessage());
            }
        }
    }

    private int getPictureType(File targetFile) throws IOException {
        String formatName = "";
        ImageInputStream iis = ImageIO.createImageInputStream(targetFile);
        Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
        while (imageReaders.hasNext()) {
            ImageReader reader = imageReaders.next();
            formatName = reader.getFormatName();
        }

        switch (formatName.toLowerCase()) {
            case "jpg":
                return Document.PICTURE_TYPE_JPEG;
            case "jpeg":
                return Document.PICTURE_TYPE_JPEG;
            case "png":
                return Document.PICTURE_TYPE_PNG;
            case "gif":
                return Document.PICTURE_TYPE_GIF;
            case "bmp":
                return Document.PICTURE_TYPE_BMP;
            default:
                return -1;
        }
    }

    private List<String> getUrls() {
        URL resource = getResourceUrl(URLS_TXT);
        if (resource == null) {
            System.out.println("Файл со ссылками не найден!");
            return Collections.emptyList();
        }

        String fileName = resource.getFile();
        try {
            return Files.readAllLines(new File(fileName).toPath());
        } catch (IOException e) {
            System.out.println("Не получается прочитать файл со ссылками - " + e.getMessage());
        }

        return Collections.emptyList();
    }

    private URL getResourceUrl(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResource(fileName);
    }

    private static void save(XWPFDocument document, String name) throws IOException {
        document.enforceUpdateFields();
        File file = new File(name);
        FileOutputStream out = new FileOutputStream(file);
        document.write(out);
        out.close();
    }
}
