package com.saniaky.word;

import com.saniaky.model.BasicModel;
import com.saniaky.util.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public class WordRenderer {

    // User variables
    private static final int IMAGE_MAX_WIDTH_PX = 200;
    private static final int IMAGE_LOAD_TRY_COUNT = 15;
    private static final int IMAGE_LOAD_TIMEOUT = 3000;

    // System variables
    private static final String TITLE_STYLE = "Heading";
    private static final String PARAGRAPH_STYLE = "Paragraph";
    private static final String TEMPLATE_FILE = "template.docx";
    private static final String TEMP_FILE = "targetFile.tmp";
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";

    public void createWord(List<BasicModel> articles, String fileName) {
        XWPFDocument document = createDocument();
        if (document == null) {
            return;
        }

        articles.forEach(article -> addArticle(document, article));
        save(document, fileName);
    }

    private void addArticle(XWPFDocument document, BasicModel article) {
        addTitle(document, article);
        addImageIfExist(document, article);
        addText(document, article);
    }

    private void addText(XWPFDocument document, BasicModel article) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun textRun = paragraph.createRun();

        // Use line breaks from string
        String[] lines = article.getText().split("\n");

        for (String line : lines) {
            if (StringUtils.isBlank(line)) {
                continue;
            }

            paragraph = document.createParagraph();
            paragraph.setStyle(PARAGRAPH_STYLE);
            textRun = paragraph.createRun();
            textRun.setText(line);
        }

        textRun.addBreak(BreakType.PAGE);
    }

    private void addTitle(XWPFDocument document, BasicModel article) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setStyle(TITLE_STYLE);
        XWPFRun title = paragraph.createRun();
        title.setText(article.getTitle());
    }

    private void addImageIfExist(XWPFDocument document, BasicModel article) {
        addImageIfExist(document, article, 0);
    }

    private void addImageIfExist(XWPFDocument document, BasicModel article, int tryCount) {
        String imageUrl = article.getImageUrl();
        if (StringUtils.isBlank(imageUrl)) {
            return;
        }

        try {
            // TODO saniaky: move to model fetcher?
            // If image path is relative - add protocol + domain name
            if (!imageUrl.startsWith("http")) {
                URL site = new URL(article.getUrl());
                imageUrl = article.getUrl().startsWith("/")
                        ? site.getProtocol() + "://" + site.getHost() + "/" + imageUrl
                        : site.getProtocol() + "://" + article.getUrl() + "/" + imageUrl;
            }
            // End

            // Save image to file
            File targetFile = createTempFile();
            URLConnection hc = new URL(imageUrl).openConnection();
            hc.setRequestProperty("User-Agent", USER_AGENT);
            FileUtils.copyInputStreamToFile(hc.getInputStream(), targetFile);
            // End

            BufferedImage bufferedImage = ImageIO.read(FileUtils.openInputStream(targetFile));
            if (bufferedImage == null) {
                System.err.println("Can't open saved image!");
                return;
            }

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

            // If picture type is known - add to word document
            if (pictureType != -1) {
                // Add image to doc
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun link = paragraph.createRun();
                link.addPicture(
                        FileUtils.openInputStream(targetFile),
                        pictureType,
                        imageUrl,
                        Units.toEMU(width), Units.toEMU(height));
            } else {
                System.err.println("Can't add image to document. Unknown image format - " + targetFile.getName());
            }
        } catch (Exception e) {
            if (e.getMessage().startsWith("Server returned HTTP response code: 403")) {
                if (tryCount < IMAGE_LOAD_TRY_COUNT) {
                    Utils.sleep(IMAGE_LOAD_TIMEOUT);
                    addImageIfExist(document, article, tryCount + 1);
                } else {
                    System.err.println("Can't add image to document: " + imageUrl + ", Error: " + e.getMessage());
                }
            }
        }

        Utils.removeFile(TEMP_FILE);
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

    private XWPFDocument createDocument() {
        XWPFDocument document = null;
        try {
            document = new XWPFDocument(getTemplate());
        } catch (IOException e) {
            System.err.println("Can't open template file. Details: " + e.getMessage());
        }
        return document;
    }

    private static void save(XWPFDocument document, String fileName) {
        FileOutputStream out = null;

        try {
            document.enforceUpdateFields();
            File file = new File(fileName);
            out = new FileOutputStream(file);
            document.write(out);

        } catch (IOException e) {
            System.err.println("Can't save file to disk! Details: " + e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private FileInputStream getTemplate() throws FileNotFoundException {
        return new FileInputStream(TEMPLATE_FILE);
    }

    private File createTempFile() {
        return new File(TEMP_FILE);
    }

}
