package org.vandeseer.pdfbox.easytable;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.Test;
import org.vandeseer.pdfbox.easytable.cell.CellImage;
import org.vandeseer.pdfbox.easytable.cell.CellText;
import org.vandeseer.pdfbox.easytable.Row.RowBuilder;
import org.vandeseer.pdfbox.easytable.Table.TableBuilder;

import java.awt.*;
import java.io.IOException;

import static java.awt.Color.LIGHT_GRAY;
import static java.awt.Color.WHITE;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.*;
import static org.vandeseer.pdfbox.easytable.cell.HorizontalAlignment.CENTER;
import static org.vandeseer.pdfbox.easytable.cell.HorizontalAlignment.RIGHT;
import static org.vandeseer.pdfbox.easytable.cell.VerticalAlignment.BOTTOM;
import static org.vandeseer.pdfbox.easytable.cell.VerticalAlignment.TOP;


public class TableDrawerIntegrationTest {

    private static final Color BLUE_DARK = new Color(76, 129, 190);
    private static final Color BLUE_LIGHT_1 = new Color(186, 206, 230);
    private static final Color BLUE_LIGHT_2 = new Color(218, 230, 242);

    @Test
    public void createSampleDocumentWithCellSpanning() throws Exception {
        final TableBuilder tableBuilder = TableBuilder.newBuilder()
                .addColumnOfWidth(300)
                .addColumnOfWidth(120)
                .addColumnOfWidth(70)
                .setFontSize(8)
                .setFont(HELVETICA);


        tableBuilder.addRow(RowBuilder.newBuilder()
                .add(CellText.builder().text("Pur").span(2).backgroundColor(Color.YELLOW).horizontalAlignment(CENTER).allBorders(1).build())
                .add(CellText.builder().text("Booz").build())
                .setBackgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(RowBuilder.newBuilder()
                .add(CellText.builder().text("Hey").horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text("Ho!").build())
                .add(CellText.builder().text("Fu.").backgroundColor(Color.ORANGE).build())
                .setBackgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(RowBuilder.newBuilder()
                .add(CellText.builder().text("Bar").horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text("Baz").span(2).backgroundColor(Color.CYAN).horizontalAlignment(CENTER).allBorders(1).build())
                .setBackgroundColor(Color.GREEN)
                .build());

        createDocumentWithTable(tableBuilder.build(), "target/sampleWithCellSpanning.pdf");
    }

    @Test
    public void createExcelLikeSampleDocument() throws Exception {
        // Some data
        final Object[][] data = {
                {"Whisky", 134.4, 145.98},
                {"Beer", 768.2, 677.9},
                {"Gin", 456.45, 612.0},
                {"Vodka", 302.71, 465.2}
        };

        // Define the table structure first
        final TableBuilder tableBuilder = TableBuilder.newBuilder()
                .addColumnOfWidth(100)
                .addColumnOfWidth(50)
                .addColumnOfWidth(50)
                .addColumnOfWidth(50)
                .setFontSize(8)
                .setFont(HELVETICA)
                .setBorderColor(Color.WHITE);

        // Add the header row ...
        final Row headerRow = RowBuilder.newBuilder()
                .add(CellText.builder().text("Product").allBorders(1).build())
                .add(CellText.builder().text("2018").horizontalAlignment(CENTER).allBorders(1).build())
                .add(CellText.builder().text("2019").horizontalAlignment(CENTER).allBorders(1).build())
                .add(CellText.builder().text("Total").horizontalAlignment(CENTER).allBorders(1).build())
                .setBackgroundColor(TableDrawerIntegrationTest.BLUE_DARK)
                .withTextColor(Color.WHITE)
                .setFont(PDType1Font.HELVETICA_BOLD)
                .setFontSize(9)
                .build();

        tableBuilder.addRow(headerRow);

        // ... and some data rows
        double grandTotal = 0;
        for (int i = 0; i < data.length; i++) {
            final Object[] dataRow = data[i];
            final double total = (double) dataRow[1] + (double) dataRow[2];
            grandTotal += total;

            tableBuilder.addRow(RowBuilder.newBuilder()
                    .add(CellText.builder().text(String.valueOf(dataRow[0])).allBorders(1).build())
                    .add(CellText.builder().text(dataRow[1] + " €").horizontalAlignment(RIGHT).allBorders(1).build())
                    .add(CellText.builder().text(dataRow[2] + " €").horizontalAlignment(RIGHT).allBorders(1).build())
                    .add(CellText.builder().text(total + " €").horizontalAlignment(RIGHT).allBorders(1).build())
                    .setBackgroundColor(i % 2 == 0 ? TableDrawerIntegrationTest.BLUE_LIGHT_1 : TableDrawerIntegrationTest.BLUE_LIGHT_2)
                    .build())
            .setWordBreaking();
        }

        // Add a final row
        tableBuilder.addRow(RowBuilder.newBuilder()
                .add(CellText.builder().text("This spans over 3 cells, is right aligned and its text is so long that it even breaks. " +
                        "Also it shows the grand total in the next cell and furthermore vertical alignment is shown:")
                        .span(3)
                        .lineSpacing(1f)
                        .borderWidthTop(1)
                        .textColor(WHITE)
                        .horizontalAlignment(RIGHT)
                        .backgroundColor(TableDrawerIntegrationTest.BLUE_DARK)
                        .fontSize(6)
                        .font(HELVETICA_OBLIQUE)
                        .allBorders(1)
                        .build())
                .add(CellText.builder().text(grandTotal + " €").backgroundColor(LIGHT_GRAY)
                        .font(HELVETICA_BOLD_OBLIQUE)
                        .horizontalAlignment(RIGHT)
                        .verticalAlignment(TOP)
                        .allBorders(1)
                        .build())
                .build());

        createDocumentWithTable(tableBuilder.build(), "target/sampleExcelLike.pdf");
    }

    private void createDocumentWithTable(final Table table, final String fileToSaveTo) throws IOException {
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
        final TableBuilder tableBuilder = TableBuilder.newBuilder()
                .addColumnOfWidth(300)
                .addColumnOfWidth(120)
                .addColumnOfWidth(70)
                .setFontSize(8)
                .setFont(HELVETICA)
                .setWordBreaking();

        // Header ...
        tableBuilder.addRow(RowBuilder.newBuilder()
                .add(CellText.builder().text("This is top right aligned without a border")
                        .horizontalAlignment(RIGHT)
                        .verticalAlignment(TOP)
                        .build())
                .add(CellText.builder().text("And this is another cell with a very long long long text that tells a nice" +
                        " and useless story, because Iam to lazy to get a lorem-ipsum and I have fun while typing" +
                        " a long text and a word that cannot be breaked yet aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa").build())
                .add(CellText.builder().text("This is bottom left aligned")
                        .backgroundColor(Color.ORANGE)
                        .verticalAlignment(BOTTOM)
                        .build())
                .setBackgroundColor(Color.BLUE)
                .build());

        // ... and some cells
        for (int i = 0; i < 10; i++) {
            tableBuilder.addRow(RowBuilder.newBuilder()
                    .add(CellText.builder().text(String.valueOf(i)).font(PDType1Font.COURIER_BOLD).allBorders(1).build())
                    .add(CellText.builder().text(String.valueOf(i * i)).fontSize(22).allBorders(1).build())
                    .add(CellText.builder().text(String.valueOf(i + (i * i))).font(PDType1Font.TIMES_ITALIC).allBorders(1).build())
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
        final Table table = getRingManagerTable();

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

        final float borderWidthOuter = 1.5f;
        final float borderWidthInner = 1.0f;

        tableBuilder.addRow(RowBuilder.newBuilder().add(
                CellText.builder().text("1.")
                        .borderWidthTop(borderWidthOuter)
                        .borderWidthLeft(borderWidthOuter)
                        .borderWidthRight(borderWidthInner)
                        .build())
                .add(
                        CellText.builder().text("WK DBV(s)")
                                .borderWidthTop(borderWidthOuter)
                                .borderWidthRight(borderWidthInner)
                                .borderWidthLeft(borderWidthInner)
                                .build())
                .add(
                        CellText.builder().text("Rote Ecke:")
                                .borderWidthTop(borderWidthOuter)
                                .borderWidthRight(borderWidthOuter)
                                .build()
                ).build());

        tableBuilder.addRow(RowBuilder.newBuilder().add(
                CellText.builder().text("").borderWidthLeft(borderWidthOuter).build())
                .add(
                        CellText.builder().text("Jugend")
                                .borderWidthRight(borderWidthInner)
                                .borderWidthLeft(borderWidthInner)
                                .build())
                .add(
                        CellText.builder().text("Thomas Test, m, FC St. Pauli, 01.01.1998, Jugend, 67,5 kg, 12K (8S, 4N, 0U)")
                                .borderWidthBottom(borderWidthInner)
                                .borderWidthRight(borderWidthOuter)
                                .build())
                .build());

        tableBuilder.addRow(RowBuilder.newBuilder().add(
                CellText.builder().text("").borderWidthLeft(borderWidthOuter).build()).add(
                CellText.builder().text("3x3")
                        .borderWidthRight(borderWidthInner)
                        .borderWidthLeft(borderWidthInner)
                        .build()).add(
                CellText.builder().text("Blaue Ecke:").borderWidthRight(borderWidthOuter).build()).build());

        tableBuilder.addRow(RowBuilder.newBuilder().add(
                CellText.builder().text("")
                        .borderWidthLeft(borderWidthOuter)
                        .borderWidthBottom(borderWidthOuter)
                        .build()).add(
                CellText.builder().text("10 Uz, KS")
                        .borderWidthRight(borderWidthInner)
                        .borderWidthLeft(borderWidthInner)
                        .borderWidthBottom(borderWidthOuter)
                        .build()).add(
                CellText.builder().text("Bernd Beispiel, m, Wedeler TSV, 02.01.1999, Jugend, 68,2 kg, 9K (7S, 2N, 0U)")
                        .borderWidthBottom(borderWidthOuter)
                        .borderWidthRight(borderWidthOuter)
                        .build()).build());

        return tableBuilder.build();
    }

    @Test
    public void testImage() throws Exception {
        final PDDocument document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        final float startY = page.getMediaBox().getHeight() - 150;
        final int startX = 56;

        final PDPageContentStream contentStream = new PDPageContentStream(document, page);
        final TableBuilder tableBuilder = new TableBuilder()
                .addColumn(new Column(200))
                .addColumn(new Column(200));

        final byte[] bytes1 = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("pic1.jpg"));
        final PDImageXObject image1 = PDImageXObject.createFromByteArray(document, bytes1, "test1");

        final byte[] bytes2 = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("pic2.jpg"));
        final PDImageXObject image2 = PDImageXObject.createFromByteArray(document, bytes2, "test2");

        tableBuilder.addRow(
                RowBuilder.newBuilder()
                        .add(CellText.builder().text("first").build())
                        .add(CellText.builder().text("second").horizontalAlignment(RIGHT).build())
                        .build());


        tableBuilder.addRow(
                RowBuilder.newBuilder()
                        .add(CellImage.builder().image(image1).allBorders(1).build())
                        .add(CellImage.builder().image(image2).allBorders(1).build())
                        .build());

        tableBuilder.addRow(
                RowBuilder.newBuilder()
                        .add(CellText.builder()
                                .text("images from \"https://www.techrepublic.com/pictures/the-21-best-it-and-tech-memes-on-the-internet/5/\"")
                                .span(2)
                                .fontSize(6)
                                .allBorders(1)
                                .build())
                        .build()
        );

        createDocumentWithTable(tableBuilder.build(), "target/images.pdf");
    }

}
