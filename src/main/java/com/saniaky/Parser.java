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
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

/**
 * @author saniaky
 * @since 12/4/16
 */
@SuppressWarnings("SameParameterValue")
public class Parser {

    private static final String URLS_TXT = "urls-prod.txt";
    private static final int ARTICLES_PER_FILE = 100;


    private static final int IMAGE_MAX_WIDTH_PX = 200;
    private static final int resolveTimeout = 10000;
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
//        XWPFDocument document = new XWPFDocument();
         XWPFDocument document = new XWPFDocument(new FileInputStream("template.docx"));
//         addCustomHeadingStyle(document, TITLE_STYLE, 1);
        return document;
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
                    imageStream = new URL(imageUrl).openStream(); // TODO saniaky: reuse stream
                    int width = bimg.getWidth();
                    int height = bimg.getHeight();
                    if (width > IMAGE_MAX_WIDTH_PX) {
                        double ratio = (double) height / width;
                        width = IMAGE_MAX_WIDTH_PX;
                        height = (int) (ratio * width);
                    }
                    width = Units.toEMU(width);
                    height = Units.toEMU(height);
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
        String fileName = classLoader.getResource(URLS_TXT).getFile();
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("Файл " + URLS_TXT + " не найден!");
        }
        return Files.readAllLines(file.toPath());
    }

    private static void save(XWPFDocument document, String name) throws IOException {
        document.enforceUpdateFields();
        File file = new File(name);
        FileOutputStream out = new FileOutputStream(file);
        document.write(out);
        out.close();
    }

    private static void addCustomHeadingStyle(XWPFDocument docxDocument, String strStyleId, int headingLevel) {
        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);

        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);

        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf(headingLevel));

        // lower number > style is more prominent in the formats bar
        ctStyle.setUiPriority(indentNumber);

        CTOnOff onoffnull = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(onoffnull);

        // style shows up in the formats bar
        ctStyle.setQFormat(onoffnull);

        // style defines a heading of the given level
        CTPPr ppr = CTPPr.Factory.newInstance();
        ppr.setOutlineLvl(indentNumber);
        ctStyle.setPPr(ppr);

        XWPFStyle style = new XWPFStyle(ctStyle);

        // is a null op if already defined
        XWPFStyles styles = docxDocument.createStyles();

        style.setType(STStyleType.PARAGRAPH);
        styles.addStyle(style);
    }
}
