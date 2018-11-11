package org.vandeseer.easytable;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.Test;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.CellImage;
import org.vandeseer.easytable.structure.cell.CellText;

import java.awt.*;
import java.io.IOException;

import static java.awt.Color.LIGHT_GRAY;
import static java.awt.Color.WHITE;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.*;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;
import static org.vandeseer.easytable.settings.HorizontalAlignment.LEFT;
import static org.vandeseer.easytable.settings.HorizontalAlignment.RIGHT;

// TODO test border color on row level
// TODO test the precedence of cell level settings of row level/table level settings
// TODO can we somehow put the "cursor" directly to where the content stream is right now?!

public class TableDrawerIntegrationTest {

    private static final Color BLUE_DARK = new Color(76, 129, 190);
    private static final Color BLUE_LIGHT_1 = new Color(186, 206, 230);
    private static final Color BLUE_LIGHT_2 = new Color(218, 230, 242);

    // TODO we should do a unit test first ;)
    @Test
    public void createSampleDocumentWithFontSettingOverriding() throws Exception {
        final TableBuilder tableBuilder = Table.builder()
                .addColumnOfWidth(100).addColumnOfWidth(100).addColumnOfWidth(100)
                .horizontalAlignment(CENTER)
                .fontSize(10).font(HELVETICA);

        tableBuilder.addRow(
                Row.builder()
                        .add(CellText.builder().text(RandomStringUtils.randomAlphabetic(23)).span(2).borderWidth(1).build())
                        .add(CellText.builder().text("Booz").build())
                        .font(COURIER_BOLD).fontSize(8)
                        .build());

        tableBuilder.addRow(
                Row.builder()
                        .add(CellText.builder().text("Pur").backgroundColor(Color.YELLOW).borderWidth(1).build())
                        .add(CellText.builder().text("Booz").build())
                        .add(CellText.builder().text("baz").build())
                        .font(COURIER_BOLD).fontSize(8).build());

        tableBuilder.addRow(
                Row.builder()
                        .add(CellText.builder().text("Pur").backgroundColor(Color.YELLOW).borderWidth(1).build())
                        .add(CellText.builder().text("Booz").build())
                        .add(CellText.builder().text("baz").font(HELVETICA_OBLIQUE).fontSize(5).build())
                        .build());

        createDocumentWithTable(tableBuilder.build(), "target/fontSettingsOverriding.pdf");
    }


    @Test
    public void createSampleDocumentWithCellSpanning() throws Exception {
        final TableBuilder tableBuilder = Table.builder()
                .addColumnOfWidth(300)
                .addColumnOfWidth(120)
                .addColumnOfWidth(70)
                .fontSize(8)
                .font(HELVETICA);


        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("Pur").span(2).backgroundColor(Color.YELLOW).horizontalAlignment(CENTER).borderWidth(1).build())
                .add(CellText.builder().text("Booz").build())
                .backgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("Hey").horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text("Ho!").build())
                .add(CellText.builder().text("Fu.").backgroundColor(Color.ORANGE).build())
                .backgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("Bar").horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text("Baz").span(2).backgroundColor(Color.CYAN).horizontalAlignment(CENTER).borderWidth(1).build())
                .backgroundColor(Color.GREEN)
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
        final TableBuilder tableBuilder = Table.builder() // TODO what about "Table.builder().columns(100,50,50,50). ..."? Would be easier! ;)
                .addColumnOfWidth(100)
                .addColumnOfWidth(50)
                .addColumnOfWidth(50)
                .addColumnOfWidth(50)
                .fontSize(8)
                .font(HELVETICA)
                .borderColor(Color.WHITE);

        // Add the header row ...
        final Row headerRow = Row.builder()
                .add(CellText.builder().text("Product").horizontalAlignment(LEFT).borderWidth(1).build())
                .add(CellText.builder().text("2018").borderWidth(1).build())
                .add(CellText.builder().text("2019").borderWidth(1).build())
                .add(CellText.builder().text("Total").borderWidth(1).build())
                .backgroundColor(TableDrawerIntegrationTest.BLUE_DARK)
                .textColor(Color.WHITE)
                .font(PDType1Font.HELVETICA_BOLD).fontSize(9)
                .horizontalAlignment(CENTER)
                .build();

        tableBuilder.addRow(headerRow);

        // ... and some data rows
        double grandTotal = 0;
        for (int i = 0; i < data.length; i++) {
            final Object[] dataRow = data[i];
            final double total = (double) dataRow[1] + (double) dataRow[2];
            grandTotal += total;

            tableBuilder.addRow(Row.builder()
                    .add(CellText.builder().text(String.valueOf(dataRow[0])).horizontalAlignment(LEFT).borderWidth(1).build())
                    .add(CellText.builder().text(dataRow[1] + " €").borderWidth(1).build())
                    .add(CellText.builder().text(dataRow[2] + " €").borderWidth(1).build())
                    .add(CellText.builder().text(total + " €").borderWidth(1).build())
                    .backgroundColor(i % 2 == 0 ? TableDrawerIntegrationTest.BLUE_LIGHT_1 : TableDrawerIntegrationTest.BLUE_LIGHT_2)
                    .horizontalAlignment(RIGHT)
                    .build())
            .wordBreak(true);
        }

        // Add a final row
        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("This spans over 3 cells, is right aligned and its text is so long that it even breaks. " +
                        "Also it shows the grand total in the next cell and furthermore vertical alignment is shown:")
                        .span(3)
                        .lineSpacing(1f)
                        .borderWidthTop(1)
                        .textColor(WHITE)
                        .backgroundColor(TableDrawerIntegrationTest.BLUE_DARK)
                        .fontSize(6)
                        .font(HELVETICA_OBLIQUE)
                        .borderWidth(1)
                        .build())
                .add(CellText.builder().text(grandTotal + " €").backgroundColor(LIGHT_GRAY)
                        .font(HELVETICA_BOLD_OBLIQUE)
                        .verticalAlignment(VerticalAlignment.TOP)
                        .borderWidth(1)
                        .build())
                .horizontalAlignment(RIGHT)
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
        TableDrawer.builder()
                .contentStream(contentStream)
                .table(table)
                .tableStartX(startX)
                .tableStartY(startY)
                .build()
                .draw();
        contentStream.close();

        document.save(fileToSaveTo);
        document.close();
    }

    @Test
    public void createTableWithDifferentFontsInCells() throws IOException {
        // Define the table structure first
        final TableBuilder tableBuilder = Table.builder()
                .addColumnOfWidth(300)
                .addColumnOfWidth(120)
                .addColumnOfWidth(70)
                .fontSize(8)
                .font(HELVETICA)
                .wordBreak(true);

        // Header ...
        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("This is top right aligned without a border")
                        .horizontalAlignment(RIGHT)
                        .verticalAlignment(VerticalAlignment.TOP)
                        .build())
                .add(CellText.builder().text("And this is another cell with a very long long long text that tells a nice" +
                        " and useless story, because Iam to lazy to get a lorem-ipsum and I have fun while typing" +
                        " a long text and a word that cannot be breaked yet aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa").build())
                .add(CellText.builder().text("This is bottom left aligned")
                        .backgroundColor(Color.ORANGE)
                        .verticalAlignment(VerticalAlignment.BOTTOM)
                        .build())
                .backgroundColor(Color.BLUE)
                .build());

        // ... and some cells
        for (int i = 0; i < 10; i++) {
            tableBuilder.addRow(Row.builder()
                    .add(CellText.builder().text(String.valueOf(i)).font(PDType1Font.COURIER_BOLD).borderWidth(1).build())
                    .add(CellText.builder().text(String.valueOf(i * i)).fontSize(22).borderWidth(1).build())
                    .add(CellText.builder().text(String.valueOf(i + (i * i))).font(PDType1Font.TIMES_ITALIC).borderWidth(1).build())
                    .backgroundColor(i % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE)
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

        TableDrawer.builder().contentStream(contentStream).table(table).tableStartX(startX).tableStartY(startY).build().draw();

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
        final TableBuilder tableBuilder = Table.builder()
                .addColumnOfWidth(26)
                .addColumnOfWidth(70)
                .addColumnOfWidth(390)
                .fontSize(9)
                .borderColor(Color.GRAY)
                .font(HELVETICA);

        final float borderWidthOuter = 1.5f;
        final float borderWidthInner = 1.0f;

        tableBuilder.addRow(Row.builder().add(
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

        tableBuilder.addRow(Row.builder().add(
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

        tableBuilder.addRow(Row.builder().add(
                CellText.builder().text("").borderWidthLeft(borderWidthOuter).build()).add(
                CellText.builder().text("3x3")
                        .borderWidthRight(borderWidthInner)
                        .borderWidthLeft(borderWidthInner)
                        .build()).add(
                CellText.builder().text("Blaue Ecke:").borderWidthRight(borderWidthOuter).build()).build());

        tableBuilder.addRow(Row.builder().add(
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

        final TableBuilder tableBuilder = Table.builder()
                .addColumnOfWidth(200)
                .addColumnOfWidth(200);

        final byte[] bytes1 = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("pic1.jpg"));
        final PDImageXObject image1 = PDImageXObject.createFromByteArray(document, bytes1, "test1");

        final byte[] bytes2 = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("pic2.jpg"));
        final PDImageXObject image2 = PDImageXObject.createFromByteArray(document, bytes2, "test2");

        tableBuilder.addRow(
                Row.builder()
                        .add(CellText.builder().text("first").build())
                        .add(CellText.builder().text("second").horizontalAlignment(RIGHT).build())
                        .build());


        tableBuilder.addRow(
                Row.builder()
                        .add(CellImage.builder().image(image1).borderWidth(1).build())
                        .add(CellImage.builder().image(image2).borderWidth(1).build())
                        .build());

        tableBuilder.addRow(
                Row.builder()
                        .add(CellText.builder()
                                .text("images from \"https://www.techrepublic.com/pictures/the-21-best-it-and-tech-memes-on-the-internet/5/\"")
                                .span(2)
                                .fontSize(6)
                                .borderWidth(1)
                                .build())
                        .build()
        );

        createDocumentWithTable(tableBuilder.build(), "target/images.pdf");
    }
    
    @Test
    public void createTwoPageTable() throws IOException {
        final PDDocument document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        final TableBuilder tableBuilder = Table.builder()
                .addColumnOfWidth(200)
                .addColumnOfWidth(200);
        
        CellText dummyHeaderCell = CellText.builder()
                .text("Header dummy")
                .backgroundColor(BLUE_DARK)
                .textColor(Color.WHITE)
                .borderWidth(1F)
                .build();
        
        CellText dummyCell = CellText.builder()
                .text("dummy")
                .borderWidth(1F)
                .build();
        
        tableBuilder.addRow(
                Row.builder()
                        .add(dummyHeaderCell)
                        .add(dummyHeaderCell)
                        .build());
        tableBuilder.addRow(
                Row.builder()
                        .add(dummyCell)
                        .add(dummyCell)
                        .build());
        tableBuilder.addRow(
                Row.builder()
                        .add(dummyCell)
                        .add(dummyCell)
                        .build());
        tableBuilder.addRow(
                Row.builder()
                        .add(dummyCell)
                        .add(dummyCell)
                        .build());
        Table completeTable = tableBuilder.build();
        
        // now define a starting point at the bottom
        final PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Define the starting point
        final float startY = 100F;
        final int startX = 50;
        
        TableDrawer drawer = TableDrawer.builder()
                .contentStream(contentStream)
                .table(completeTable)
                .tableStartX(startX)
                .tableStartY(startY)
                .tableEndY(50F) // note: if not set, table is drawn over the end of the page
                .build();

        PDPageContentStream currentContentStream = contentStream;
        // Draw!
        while(!drawer.draw()) {
            // could not draw on one page
            
            // close current content stream
            currentContentStream.close();
            
            // add a new page and content stream
            final PDPage additionalPage = new PDPage(PDRectangle.A4);
            document.addPage(additionalPage);
            final PDPageContentStream additionalContentStream = new PDPageContentStream(document, additionalPage);
            
            // define starting point on additional page
            final float pageStartY = page.getMediaBox().getHeight() - 50;
            
            drawer = drawer.toBuilder()
                    .contentStream(additionalContentStream)
                    .tableStartY(pageStartY)
                    .build();
            
            // change current content stream
            currentContentStream = additionalContentStream;
        }
        currentContentStream.close(); // close the last content stream

        document.save("target/twoPageTable.pdf");
        document.close();
    }

}
