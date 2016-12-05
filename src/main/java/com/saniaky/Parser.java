package com.saniaky;

import de.jetwick.snacktory.HtmlFetcher;
import de.jetwick.snacktory.JResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

/**
 * @author saniaky
 * @since 12/4/16
 */
public class Parser {

    private static final String URLS_TXT = "urls.txt";
    private static final int ARTICLES_PER_FILE = 100;


    private static final int IMAGE_MAX_WIDTH_PX = 200;
    private static final int resolveTimeout = 30000;
    private static final String TITLE_STYLE = "Title";

    public static void main(String[] args) throws IOException, InvalidFormatException {
        Parser parser = new Parser();
        List<String> urls = parser.getUrls();
        HtmlFetcher fetcher = new HtmlFetcher();
        XWPFDocument document = createDocument();

        int fileNumber = 1;
        int articlesNumber = 0;

        for (int row = 0; row < urls.size(); row++) {
            JResult res;
            try {
                res = fetcher.fetchAndExtract(urls.get(row), resolveTimeout, true);
            } catch (Exception e) {
                System.out.println("Не получается загрузить статью: " + urls.get(row));
                System.out.println("Причина: " + e.getMessage());
                continue;
            }

            String text = res.getText();
            String title = res.getTitle();
            String imageUrl = res.getImageUrl();
            System.out.println("Строка " + (row + 1) + ": " + title);
            parser.addArticle(document, title, text, imageUrl);
            articlesNumber++;

            if (articlesNumber % ARTICLES_PER_FILE == 0) {
                save(document, getFilename(fileNumber));
                document = createDocument();
                articlesNumber = 0;
                fileNumber++;
            }
        }

        if (articlesNumber != 0) {
            save(document, getFilename(fileNumber));
        }
    }

    private static String getFilename(int fileNum) {
        return String.format("Стартапы %d.docx", fileNum);
    }

    private static XWPFDocument createDocument() throws IOException {
        return new XWPFDocument(new FileInputStream("template.docx"));
    }

    private void addArticle(XWPFDocument document, String articleTitle, String articleText, String imageUrl)
            throws IOException, InvalidFormatException {

        // Add title
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setStyle(TITLE_STYLE);
        XWPFRun title = paragraph.createRun();
        title.setText(articleTitle);

        // Add image if exist
        if (StringUtils.isNotEmpty(imageUrl)) {
            paragraph = document.createParagraph();
            XWPFRun link = paragraph.createRun();
            try {
                InputStream imageStream = new URL(imageUrl).openStream();
                BufferedImage bimg = ImageIO.read(imageStream);
                if (bimg != null) {
                    int width = bimg.getWidth();
                    int height = bimg.getHeight();
                    if (width > IMAGE_MAX_WIDTH_PX) {
                        double ratio = (double) height / width;
                        width = IMAGE_MAX_WIDTH_PX;
                        height = (int) (ratio * width);
                    }
                    width = Units.toEMU(width);
                    height = Units.toEMU(height);
                    imageStream = new URL(imageUrl).openStream(); // TODO saniaky: reuse stream
                    link.addPicture(imageStream, Document.PICTURE_TYPE_JPEG, imageUrl, width, height);
                }
            } catch (IOException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Не получается загрузить изображение: " + imageUrl);
                System.out.println("Причина: " + e.getMessage());
            }
        }

        // Article text
        paragraph = document.createParagraph();
        XWPFRun article = paragraph.createRun();
        article.setText(articleText);
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        article.addBreak(BreakType.PAGE);
    }

    private List<String> getUrls() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(URLS_TXT);
        if (resource == null) {
            System.out.println("Файл со ссылками не найден!");
            return Collections.emptyList();
        }

        String fileName = resource.getFile();
        return Files.readAllLines(new File(fileName).toPath());
    }

    private static void save(XWPFDocument document, String name) throws IOException {
        document.enforceUpdateFields();
        File file = new File(name);
        FileOutputStream out = new FileOutputStream(file);
        document.write(out);
        out.close();
    }
}
