package org.vandeseer.integrationtest;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.io.IOException;

import static org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode.APPEND;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;

public class FinalYTest {

    @Test
    public void testFinalY() throws IOException {

        try (final PDDocument document = new PDDocument()) {
            for (int i = 0; i < 10; i++) {
                final PDPage page = new PDPage(PDRectangle.A4);
                try (final PDPageContentStream contentStream = new PDPageContentStream(document, page, APPEND, false)) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(100, 100);
                    contentStream.showText("text number " + i);
                    contentStream.endText();
                }
                document.addPage(page);
            }

            for (int i = 0; i < document.getNumberOfPages(); i++) {
                try (final PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(i), APPEND, false)) {
                    TableDrawer tableDrawer = TableDrawer.builder()
                            .contentStream(contentStream)
                            .table(createHeaderTableForPage(i))
                            .startX(50)
                            .startY(document.getPage(i).getMediaBox().getHeight() - 50)
                            .build();
                    tableDrawer.draw();

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(100, tableDrawer.getFinalY() - 12);
                    contentStream.showText("text after final y " + tableDrawer.getFinalY());
                    contentStream.endText();
                }
            }

            document.save("target/finalY.pdf");
        }
    }

    private static Table createHeaderTableForPage(int pageNumber) {
        return Table.builder()
                .addColumnsOfWidth(100, 100, 100, 100)
                .fontSize(8)
                .font(HELVETICA)
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("Header").build())
                        .add(TextCell.builder().borderWidth(1).text("of").build())
                        .add(TextCell.builder().borderWidth(1).text("Page").build())
                        .add(TextCell.builder().borderWidth(1).text(String.valueOf(pageNumber)).build())
                        .build())
                .build();
    }

}
