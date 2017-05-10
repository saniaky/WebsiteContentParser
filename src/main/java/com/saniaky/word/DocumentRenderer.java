package com.saniaky.word;

import com.saniaky.Utils;
import com.saniaky.model.BasicModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
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
public class DocumentRenderer {

    // User variables
    private static final int ARTICLES_PER_FILE = 100;
    private static final int IMAGE_MAX_WIDTH_PX = 200;

    // System variables
    private static final String TITLE_STYLE = "Heading";
    private static final String PARAGRAPH_STYLE = "Paragraph";
    private static final String TEMPLATE_FILE = "template.docx";
    private static final String TEMP_FILE = "targetFile.tmp";
    private static final int IMAGE_LOAD_TRY_COUNT = 15;
    private static final int IMAGE_LOAD_TIMEOUT = 3000;

    private static String getFilename(int fileNum) {
        return String.format("Стартапы %d.docx", fileNum);
    }

    private static void save(XWPFDocument document, String name) {
        try {
            document.enforceUpdateFields();
            File file = new File(name);
            FileOutputStream out = new FileOutputStream(file);
            document.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createWord(List<BasicModel> articles) {
        XWPFDocument document = createDocument();
        int fileNumber = 1;

        for (int i = 0; i < articles.size(); i++) {
            BasicModel article = articles.get(i);

            // Create new document
            if (i != 0 && i % ARTICLES_PER_FILE == 0) {
                save(document, getFilename(fileNumber));
                fileNumber++;
                document = createDocument();
            }

            addArticle(document, article);
        }

        // TODO AKoh check if we need to save it again
        save(document, getFilename(fileNumber));
    }

    private XWPFDocument createDocument() {
        try {
            return new XWPFDocument(new FileInputStream(TEMPLATE_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addArticle(XWPFDocument document, BasicModel article) {
        if (document != null) {
            addTitle(document, article);
            addImageIfExist(document, article, 0);
            addText(document, article);
        }
    }

    private void addText(XWPFDocument document, BasicModel article) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun textRun = paragraph.createRun();

        // User line breaks from string
        String[] lines = article.getText().split("\n");

        for (String line : lines) {
            if (StringUtils.isBlank(line)) {
                continue;
            }

            paragraph = document.createParagraph();
            paragraph.setStyle(PARAGRAPH_STYLE);

            //paragraph.setFirstLineIndent(565);
            //paragraph.setAlignment(ParagraphAlignment.BOTH);

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

    private void addImageIfExist(XWPFDocument document, BasicModel article, int tryCount) {
        String imageUrl = article.getImageUrl();
        if (StringUtils.isNotEmpty(imageUrl)) {
            try {
                // If image path are relative
                if (!imageUrl.startsWith("http")) {
                    URL site = new URL(article.getUrl());

                    if (article.getUrl().startsWith("/")) {
                        imageUrl = site.getProtocol() + "://" + site.getHost() + "/" + imageUrl;
                    } else {
                        imageUrl = site.getProtocol() + "://" + article.getUrl() + "/" + imageUrl;
                    }
                }

                // Save image to file
                File targetFile = new File(TEMP_FILE);
                URL url = new URL(imageUrl);
                URLConnection hc = url.openConnection();
                hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
                FileUtils.copyInputStreamToFile(hc.getInputStream(), targetFile);

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
            } catch (Exception e) {
                if (e.getMessage().startsWith("Server returned HTTP response code: 403")) {
                    if (tryCount < IMAGE_LOAD_TRY_COUNT) {
                        Utils.sleep(IMAGE_LOAD_TIMEOUT);
                        addImageIfExist(document, article, tryCount + 1);
                        System.err.println("Не получается добавить изображение: " + imageUrl + ". Попытка: " + tryCount + "/" + IMAGE_LOAD_TRY_COUNT);
                    } else {
                        System.err.println("Не получается добавить изображение: " + imageUrl + ", Error: " + e.getMessage());
                    }
                }
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

}
