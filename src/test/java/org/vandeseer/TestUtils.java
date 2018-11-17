package org.vandeseer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Table;

import java.io.IOException;

public class TestUtils {

    private static final String TARGET_FOLDER = "target";
    private static final float DOCUMENT_PADDING = 50f;

    private TestUtils() {
    }

    public static void createAndSaveDocumentWithTable(Table table, String outputFileName) throws IOException {

        final PDDocument document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try (final PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

            TableDrawer.builder()
                    .contentStream(contentStream)
                    .table(table)
                    .startX(DOCUMENT_PADDING)
                    .startY(page.getMediaBox().getHeight() - DOCUMENT_PADDING)
                    .build()
                    .draw();

        }

        document.save(TARGET_FOLDER + "/" + outputFileName);
        document.close();

    }

}
