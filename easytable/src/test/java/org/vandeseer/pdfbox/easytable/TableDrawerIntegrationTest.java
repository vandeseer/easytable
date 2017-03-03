package org.vandeseer.pdfbox.easytable;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.pdfbox.easytable.Cell.HorizontalAlignment;
import org.vandeseer.pdfbox.easytable.Row.RowBuilder;
import org.vandeseer.pdfbox.easytable.Table.TableBuilder;

import java.awt.*;

public class TableDrawerIntegrationTest {

    @Test
    public void createDocument() throws Exception {
        final PDDocument document = new PDDocument();
        final PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
        page.setRotation(90);
        document.addPage(page);
        final PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.concatenate2CTM(0, 1, -1, 0, page.findMediaBox().getWidth(), 0);
        final float startY = page.findMediaBox().getWidth() - 30;

        (new TableDrawer(contentStream, createAndGetTableWithAlternatingColors(), 30, startY)).draw();
        contentStream.close();

        document.save("target/alternateRows.pdf");
        document.close();
    }

    @Test
    public void createRingManagerDocument() throws Exception {
        final PDDocument document = new PDDocument();
        final PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
        document.addPage(page);

        final float startY = page.findMediaBox().getHeight() - 150;
        final int startX = 56;

        // We have to draw the image first according to:
        // http://stackoverflow.com/questions/8521290/cant-add-an-image-to-a-pdf-using-pdfbox
        // Can we also draw svg/eps?
//    InputStream in = new FileInputStream(new File("/home/stefan/tmp/rmlogo.jpg"));
//    PDJpeg img = new PDJpeg(document, in);
//    
//    
        final PDPageContentStream contentStream = new PDPageContentStream(document, page);
        Table table = getRingManagerTable();

        (new TableDrawer(contentStream, table, startX, startY)).draw();

        contentStream.setFont(PDType1Font.HELVETICA, 8.0f);
        contentStream.beginText();
        // Is table.getHeight() correct?
        System.out.println(table.getHeight());
        contentStream.moveTextPositionByAmount(startX, startY - (table.getHeight() + 22));
        contentStream.drawString("Dieser Kampf muss der WB nicht entsprechen, da als Sparringskampf angesetzt.");
        contentStream.endText();

        contentStream.close();

        document.save("target/ringmanager.pdf");
        document.close();
    }

    private Table createAndGetTableWithAlternatingColors() {
        TableBuilder tableBuilder = new TableBuilder()
                .addColumn(new Column(20))
                .addColumn(new Column(40))
                .setFontSize(8)
                .setFont(PDType1Font.HELVETICA);

        Color lightGray = new Color(224, 224, 224);
        Color lightBlue = new Color(194, 232, 233);

        for (int i = 0; i < 10; i++) {
            Color backgroundColor = (i % 2 == 0) ? lightBlue : lightGray;

            tableBuilder
                    .addRow(
                            new RowBuilder()
                                    .add(new Cell(String.valueOf(i))
                                            .setBackgroundColor(backgroundColor)
                                            .setBorderWidthBottom(2f)
                                            .setHorizontalAlignment(HorizontalAlignment.RIGHT))
                                    .add(new Cell(String.valueOf(i * 2))
                                            .setBackgroundColor(backgroundColor))
                                    .build());
        }

        return tableBuilder.build();
    }

    private Table getRingManagerTable() {
        final TableBuilder tableBuilder = new TableBuilder()
                .addColumn(new Column(26))
                .addColumn(new Column(70))
                .addColumn(new Column(390))
                .setFontSize(9)
                .setFont(PDType1Font.HELVETICA);

        float borderWidthOuter = 1.5f;
        float borderWidthInner = 1.0f;

        tableBuilder.addRow(new RowBuilder().add(
                new Cell("1.")
                        .setBorderWidthTop(borderWidthOuter)
                        .setBorderWidthLeft(borderWidthOuter)
                        .setBorderWidthRight(borderWidthInner))
                .add(
                        new Cell("WK DBV(s)")
                                .setBorderWidthTop(borderWidthOuter)
                                .setBorderWidthRight(borderWidthInner)
                                .setBorderWidthLeft(borderWidthInner))
                .add(
                        new Cell("Rote Ecke:")
                                .setBorderWidthTop(borderWidthOuter)
                                .setBorderWidthRight(borderWidthOuter)).build());

        tableBuilder.addRow(new RowBuilder().add(
                new Cell("").setBorderWidthLeft(borderWidthOuter))
                .add(
                        new Cell("Jugend")
                                .setBorderWidthRight(borderWidthInner)
                                .setBorderWidthLeft(borderWidthInner)).add(
                        new Cell("Thomas Test, m, FC St. Pauli, 01.01.1998, Jugend, 67,5 kg, 12K (8S, 4N, 0U)")
                                .setBorderWidthBottom(borderWidthInner)
                                .setBorderWidthRight(borderWidthOuter)).build());

        tableBuilder.addRow(new RowBuilder().add(
                new Cell("").setBorderWidthLeft(borderWidthOuter)).add(
                new Cell("3x3")
                        .setBorderWidthRight(borderWidthInner)
                        .setBorderWidthLeft(borderWidthInner)).add(
                new Cell("Blaue Ecke:").setBorderWidthRight(borderWidthOuter)).build());

        tableBuilder.addRow(new RowBuilder().add(
                new Cell("")
                        .setBorderWidthLeft(borderWidthOuter)
                        .setBorderWidthBottom(borderWidthOuter)).add(
                new Cell("10 Uz, KS")
                        .setBorderWidthRight(borderWidthInner)
                        .setBorderWidthLeft(borderWidthInner)
                        .setBorderWidthBottom(borderWidthOuter)).add(
                new Cell("Bernd Beispiel, m, Wedeler TSV, 02.01.1999, Jugend, 68,2 kg, 9K (7S, 2N, 0U)")
                        .setBorderWidthBottom(borderWidthOuter)
                        .setBorderWidthRight(borderWidthOuter)).build());

        return tableBuilder.build();
    }

}
