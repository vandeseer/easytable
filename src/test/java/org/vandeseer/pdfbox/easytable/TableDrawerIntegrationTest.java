package org.vandeseer.pdfbox.easytable;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.pdfbox.easytable.Row.RowBuilder;
import org.vandeseer.pdfbox.easytable.Table.TableBuilder;

import java.awt.*;
import java.io.IOException;

import static java.awt.Color.LIGHT_GRAY;
import static java.awt.Color.WHITE;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.*;
import static org.vandeseer.pdfbox.easytable.Cell.HorizontalAlignment.CENTER;
import static org.vandeseer.pdfbox.easytable.Cell.HorizontalAlignment.RIGHT;

public class TableDrawerIntegrationTest {

    private static final Color BLUE_DARK = new Color(76, 129, 190);
    private static final Color BLUE_LIGHT_1 = new Color(186, 206, 230);
    private static final Color BLUE_LIGHT_2 = new Color(218, 230, 242);

    @Test
    public void createSampleDocumentWithCellSpanning() throws Exception {
        TableBuilder tableBuilder = TableBuilder.newBuilder()
                .addColumnOfWidth(300)
                .addColumnOfWidth(120)
                .addColumnOfWidth(70)
                .setFontSize(8)
                .setFont(HELVETICA);

        tableBuilder.addRow(RowBuilder.newBuilder()
                .add(Cell.withText("Pur").span(2).setBackgroundColor(Color.YELLOW).setHorizontalAlignment(CENTER).withAllBorders())
                .add(Cell.withText("Booz"))
                .setBackgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(RowBuilder.newBuilder()
                .add(Cell.withText("Hey").setHorizontalAlignment(RIGHT))
                .add(Cell.withText("Ho!"))
                .add(Cell.withText("Fu.").setBackgroundColor(Color.ORANGE))
                .setBackgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(RowBuilder.newBuilder()
                .add(Cell.withText("Bar").setHorizontalAlignment(RIGHT))
                .add(Cell.withText("Baz").span(2).setBackgroundColor(Color.CYAN).setHorizontalAlignment(CENTER).withAllBorders())
                .setBackgroundColor(Color.GREEN)
                .build());

        createDocumentWithTable(tableBuilder.build(), "target/sampleWithCellSpanning.pdf");
    }

    @Test
    public void createExcelLikeSampleDocument() throws Exception {
        // Some data
        Object[][] data = {
                { "Whisky"  , 134.4 , 145.98 },
                { "Beer"    , 768.2 , 677.9  },
                { "Gin"     , 456.45, 612.0  },
                { "Vodka"   , 302.71 , 465.2 }
        };

        // Define the table structure first
        TableBuilder tableBuilder = TableBuilder.newBuilder()
                .addColumnOfWidth(100)
                .addColumnOfWidth(50)
                .addColumnOfWidth(50)
                .addColumnOfWidth(50)
                .setFontSize(8)
                .setFont(HELVETICA)
                .setBorderColor(Color.WHITE);

        // Add the header row ...
        Row headerRow = RowBuilder.newBuilder()
                .add(Cell.withText("Product").withAllBorders())
                .add(Cell.withText("2018").withAllBorders().setHorizontalAlignment(CENTER))
                .add(Cell.withText("2019").withAllBorders().setHorizontalAlignment(CENTER))
                .add(Cell.withText("Total").withAllBorders().setHorizontalAlignment(CENTER))
                .setBackgroundColor(BLUE_DARK)
                .withTextColor(Color.WHITE)
                .setFont(PDType1Font.HELVETICA_BOLD)
                .setFontSize(9)
                .build();

        tableBuilder.addRow(headerRow);

        // ... and some data rows
        double grandTotal = 0;
        for (int i = 0; i < data.length; i++) {
            Object[] dataRow = data[i];
            double total = (double) dataRow[1] + (double) dataRow[2];
            grandTotal += total;

            tableBuilder.addRow(RowBuilder.newBuilder()
                    .add(Cell.withText(dataRow[0]).withAllBorders())
                    .add(Cell.withText(dataRow[1] + " €").withAllBorders().setHorizontalAlignment(RIGHT))
                    .add(Cell.withText(dataRow[2] + " €").withAllBorders().setHorizontalAlignment(RIGHT))
                    .add(Cell.withText(total + " €").withAllBorders().setHorizontalAlignment(RIGHT))
                    .setBackgroundColor(i % 2 == 0 ? BLUE_LIGHT_1 : BLUE_LIGHT_2)
                    .build());
        }

        // Add a final row
        tableBuilder.addRow(RowBuilder.newBuilder()
                .add(Cell.withText("This spans over 3 cells and shows the grand total in the next cell:")
                        .span(3)
                        .setBorderWidthTop(1)
                        .withTextColor(WHITE)
                        .withAllBorders()
                        .setHorizontalAlignment(RIGHT)
                        .setBackgroundColor(BLUE_DARK)
                        .withFontSize(6)
                        .withFont(HELVETICA_OBLIQUE))
                .add(Cell.withText(grandTotal + " €").setBackgroundColor(LIGHT_GRAY)
                    .withFont(HELVETICA_BOLD_OBLIQUE)
                    .setHorizontalAlignment(RIGHT)
                    .withAllBorders())
                .build());

        createDocumentWithTable(tableBuilder.build(), "target/sampleExcelLike.pdf");
    }

    private void createDocumentWithTable(Table table, String fileToSaveTo) throws IOException {
        final PDDocument document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        final PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Define the starting point
        final float startY = page.getMediaBox().getHeight() - 50;
        final int startX = 50;

        // Draw!
        TableDrawer.TableDrawerBuilder.newBuilder()
                .withContentStream(contentStream)
                .withTable(table)
                .startX(startX)
                .startY(startY)
                .build()
                .draw();
        contentStream.close();

        document.save(fileToSaveTo);
        document.close();
    }

    @Test
    public void createTableWithDifferentFontsInCells() throws IOException {
        // Define the table structure first
        TableBuilder tableBuilder = TableBuilder.newBuilder()
                .addColumnOfWidth(300)
                .addColumnOfWidth(120)
                .addColumnOfWidth(70)
                .setFontSize(8)
                .setFont(HELVETICA);

        // Header ...
        tableBuilder.addRow(RowBuilder.newBuilder()
                .add(Cell.withText("This is right aligned without a border").setHorizontalAlignment(RIGHT))
                .add(Cell.withText("And this is another cell"))
                .add(Cell.withText("Sum").setBackgroundColor(Color.ORANGE))
                .setBackgroundColor(Color.BLUE)
                .build());

        // ... and some cells
        for (int i = 0; i < 10; i++) {
            tableBuilder.addRow(RowBuilder.newBuilder()
                    .add(Cell.withText(i).withAllBorders().withFont(PDType1Font.COURIER_BOLD))
                    .add(Cell.withText(i * i).withAllBorders().withFontSize(22))
                    .add(Cell.withText(i + (i * i)).withAllBorders().withFont(PDType1Font.TIMES_ITALIC))
                    .setBackgroundColor(i % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE)
                    .build());
        }

        createDocumentWithTable(tableBuilder.build(), "target/sampleDifferentFontsInCells.pdf");
    }

    @Test
    public void createRingManagerDocument() throws Exception {
        final PDDocument document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        final float startY = page.getMediaBox().getHeight() - 150;
        final int startX = 56;

        final PDPageContentStream contentStream = new PDPageContentStream(document, page);
        Table table = getRingManagerTable();

        (new TableDrawer(contentStream, table, startX, startY)).draw();

        contentStream.setFont(HELVETICA, 8.0f);
        contentStream.beginText();

        contentStream.newLineAtOffset(startX, startY - (table.getHeight() + 22));
        contentStream.showText("Dieser Kampf muss der WB nicht entsprechen, da als Sparringskampf angesetzt.");
        contentStream.endText();

        contentStream.close();

        document.save("target/ringmanager.pdf");
        document.close();
    }

    private Table getRingManagerTable() {
        final TableBuilder tableBuilder = new TableBuilder()
                .addColumn(new Column(26))
                .addColumn(new Column(70))
                .addColumn(new Column(390))
                .setFontSize(9)
                .setBorderColor(Color.GRAY)
                .setFont(HELVETICA);

        float borderWidthOuter = 1.5f;
        float borderWidthInner = 1.0f;

        tableBuilder.addRow(RowBuilder.newBuilder().add(
                Cell.withText("1.")
                        .setBorderWidthTop(borderWidthOuter)
                        .setBorderWidthLeft(borderWidthOuter)
                        .setBorderWidthRight(borderWidthInner))
                .add(
                        Cell.withText("WK DBV(s)")
                                .setBorderWidthTop(borderWidthOuter)
                                .setBorderWidthRight(borderWidthInner)
                                .setBorderWidthLeft(borderWidthInner))
                .add(
                        Cell.withText("Rote Ecke:")
                                .setBorderWidthTop(borderWidthOuter)
                                .setBorderWidthRight(borderWidthOuter)).build());

        tableBuilder.addRow(RowBuilder.newBuilder().add(
                Cell.withText("").setBorderWidthLeft(borderWidthOuter))
                .add(
                        Cell.withText("Jugend")
                                .setBorderWidthRight(borderWidthInner)
                                .setBorderWidthLeft(borderWidthInner)).add(
                        Cell.withText("Thomas Test, m, FC St. Pauli, 01.01.1998, Jugend, 67,5 kg, 12K (8S, 4N, 0U)")
                                .setBorderWidthBottom(borderWidthInner)
                                .setBorderWidthRight(borderWidthOuter)).build());

        tableBuilder.addRow(RowBuilder.newBuilder().add(
                Cell.withText("").setBorderWidthLeft(borderWidthOuter)).add(
                Cell.withText("3x3")
                        .setBorderWidthRight(borderWidthInner)
                        .setBorderWidthLeft(borderWidthInner)).add(
                Cell.withText("Blaue Ecke:").setBorderWidthRight(borderWidthOuter)).build());

        tableBuilder.addRow(RowBuilder.newBuilder().add(
                Cell.withText("")
                        .setBorderWidthLeft(borderWidthOuter)
                        .setBorderWidthBottom(borderWidthOuter)).add(
                Cell.withText("10 Uz, KS")
                        .setBorderWidthRight(borderWidthInner)
                        .setBorderWidthLeft(borderWidthInner)
                        .setBorderWidthBottom(borderWidthOuter)).add(
                Cell.withText("Bernd Beispiel, m, Wedeler TSV, 02.01.1999, Jugend, 68,2 kg, 9K (7S, 2N, 0U)")
                        .setBorderWidthBottom(borderWidthOuter)
                        .setBorderWidthRight(borderWidthOuter)).build());

        return tableBuilder.build();
    }



}
