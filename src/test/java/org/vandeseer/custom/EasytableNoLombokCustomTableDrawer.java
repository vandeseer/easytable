package org.vandeseer.custom;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellText;

import java.awt.*;
import java.io.IOException;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;

public class EasytableNoLombokCustomTableDrawer {

    private static class CustomTableDrawer extends TableDrawer {

        CustomTableDrawer(float startX, float startY, PDPageContentStream contentStream, Table table) {
            super(startX, startY, contentStream, table, 0);
        }

        // TODO

//        @Override
//        protected void drawText(String text, PDFont font, int fontSize, Color color, float x, float y) throws IOException {
//            System.out.println("My custom table drawer is called :-)");
//            super.drawText(text.toLowerCase(), font, fontSize, color, x, y, super.contentStream);
//        }

    }

    public static void main(String[] args) throws IOException {
        final PDDocument document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try (final PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

            EasytableNoLombokCustomTableDrawer.CustomTableDrawer tableDrawer =
                    new EasytableNoLombokCustomTableDrawer.CustomTableDrawer(
                            50,
                            page.getMediaBox().getHeight() - 50,
                            contentStream,
                            createSimpleTable()
                    );

            tableDrawer.draw();

        }

        document.save("target/customTableDrawerNoLombok.pdf");
        document.close();
    }

    private static Table createSimpleTable() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 100, 100, 100)
                .fontSize(8)
                .font(HELVETICA);

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().borderWidth(1).text("One").build())
                .add(CellText.builder().borderWidth(1).text("Two").build())
                .add(CellText.builder().borderWidth(1).text("Three").build())
                .add(CellText.builder().borderWidth(1).text("Four").build())
                .build());

        return tableBuilder.build();
    }

}
