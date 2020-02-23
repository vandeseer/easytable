package org.vandeseer;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Table;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

public class TestUtils {

    public static final String TARGET_FOLDER = "target";
    public static final String TARGET_SUBFOLDER_REGRESSION = "/regression";

    private static final float PADDING = 50f;

    private static final PDDocument PD_DOCUMENT_FOR_IMAGES = new PDDocument();

    private TestUtils() {
    }

    public static void createAndSaveDocumentWithTable(String outputFileName, Table table) throws IOException {
        createAndSaveDocumentWithTables(outputFileName, table);
    }

    public static void createAndSaveDocumentWithTables(String outputFileName, Collection<Table> tables) throws IOException {
        createAndSaveDocumentWithTables(new PDDocument(), outputFileName, tables.toArray(new Table[0]));
    }

    public static void createAndSaveDocumentWithTables(String outputFileName, Table... tables) throws IOException {
        createAndSaveDocumentWithTables(new PDDocument(), outputFileName, tables);
    }

    private static void createAndSaveDocumentWithTables(PDDocument document, String outputFileName, Table... tables) throws IOException {

        final PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        float startY = page.getMediaBox().getHeight() - PADDING;

        try (final PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

            for (final Table table : tables) {

                TableDrawer.builder()
                        .page(page)
                        .contentStream(contentStream)
                        .table(table)
                        .startX(PADDING)
                        .startY(startY)
                        .endY(PADDING)
                        .build()
                        .draw(() -> document, () -> new PDPage(PDRectangle.A4), PADDING);

                startY -= (table.getHeight() + PADDING);
            }

        }

        document.save(TARGET_FOLDER + "/" + outputFileName);
        document.close();

    }

    public static PDImageXObject createTuxImage() throws IOException {
        final byte[] tuxBytes = IOUtils.toByteArray(Objects.requireNonNull(TestUtils.class.getClassLoader().getResourceAsStream("tux.png")));
        return PDImageXObject.createFromByteArray(PD_DOCUMENT_FOR_IMAGES, tuxBytes, "tux");
    }

    public static PDImageXObject createGliderImage() throws IOException {
        final byte[] gliderBytes = IOUtils.toByteArray(Objects.requireNonNull(TestUtils.class.getClassLoader().getResourceAsStream("glider.png")));
        return PDImageXObject.createFromByteArray(PD_DOCUMENT_FOR_IMAGES, gliderBytes, "glider");
    }

    public static PDImageXObject createSampleImage() throws IOException {
        final byte[] sampleBytes = IOUtils.toByteArray(Objects.requireNonNull(TestUtils.class.getClassLoader().getResourceAsStream("sample.png")));
        return PDImageXObject.createFromByteArray(PD_DOCUMENT_FOR_IMAGES, sampleBytes, "sample");
    }

    public static void assertRegressionFolderExists() {
        new File(TARGET_FOLDER + TARGET_SUBFOLDER_REGRESSION).mkdirs();
    }

    public static String getRegressionFolder() {
        return TARGET_FOLDER + TARGET_SUBFOLDER_REGRESSION;
    }

}
