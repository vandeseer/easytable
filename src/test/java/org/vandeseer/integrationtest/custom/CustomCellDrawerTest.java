package org.vandeseer.integrationtest.custom;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.PositionedStyledText;
import org.vandeseer.easytable.drawing.cell.TextCellDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
import static org.vandeseer.TestUtils.getActualPdfFor;
import static org.vandeseer.TestUtils.getExpectedPdfFor;

public class CustomCellDrawerTest {

    public static final String FILE_NAME = "customCellDrawerNoLombok.pdf";

    private static final TextCellDrawer<TextCell> CUSTOM_DRAWER = new TextCellDrawer<TextCell>() {
        @Override
        protected void drawText(DrawingContext drawingContext, PositionedStyledText text) throws IOException {
            System.out.println("My custom drawer is called :-)");
            super.drawText(drawingContext, text.toBuilder().text(text.getText().toUpperCase()).build());
        }
    };

    @Test
    public void testCustomCellDrawer() throws IOException {

        try (final PDDocument document = new PDDocument()) {

            final PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (final PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                TableDrawer.builder()
                        .contentStream(contentStream)
                        .table(createSimpleTable())
                        .startX(50)
                        .startY(page.getMediaBox().getHeight() - 50)
                        .build()
                        .draw();

            }

            document.save(TestUtils.TARGET_FOLDER + "/" + FILE_NAME);
        }

        CompareResult compareResult = new PdfComparator<>(getExpectedPdfFor(FILE_NAME), getActualPdfFor(FILE_NAME)).compare();
        assertTrue(compareResult.isEqual());
    }

    private static Table createSimpleTable() {
        return Table.builder()
                .addColumnsOfWidth(100, 100, 100, 100)
                .fontSize(8)
                .font(HELVETICA)
                .addRow(Row.builder()
                        .add(TextCell.builder().drawer(CUSTOM_DRAWER).borderWidth(1).text("One").build())
                        .add(TextCell.builder().borderWidth(1).text("Two").build())
                        .add(TextCell.builder().borderWidth(1).text("Three").build())
                        .add(TextCell.builder().borderWidth(1).text("Four").build())
                        .build())
                .build();
    }

}
