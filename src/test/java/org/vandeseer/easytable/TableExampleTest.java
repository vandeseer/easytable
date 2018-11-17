package org.vandeseer.easytable;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.CellText;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;
import static org.vandeseer.easytable.settings.HorizontalAlignment.RIGHT;
import static org.vandeseer.easytable.settings.VerticalAlignment.TOP;

public class TableExampleTest {

    @Test
    public void tableExample() throws IOException {
        
        // this example tests the Readme.md example
        
        // Use the following static imports:
        //        import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;
        //        import static org.vandeseer.easytable.settings.HorizontalAlignment.RIGHT;
        //        import static org.vandeseer.easytable.settings.VerticalAlignment.TOP;
        
        // Output file name
        String fileToSaveTo = "target/readme_example.pdf";
        
        // Some data
        final Object[][] data = {
                {"Whisky", 134.4, 145.98},
                {"Beer", 768.2, 677.9},
                {"Gin", 456.45, 612.0},
                {"Vodka", 302.71, 465.2}
        };
        
        // Some colors
        Color BLUE_DARK = new Color(76, 129, 190);
        Color BLUE_LIGHT_1 = new Color(186, 206, 230);
        Color BLUE_LIGHT_2 = new Color(218, 230, 242);

        // Define the table structure first
        final TableBuilder tableBuilder = Table.builder()
                .addColumnOfWidth(100)
                .addColumnOfWidth(50)
                .addColumnOfWidth(50)
                .addColumnOfWidth(50)
                .fontSize(8)
                .font(PDType1Font.HELVETICA)
                .borderColor(Color.WHITE);

        // Add the header row ...
        final Row headerRow = Row.builder()
                .add(CellText.builder().text("Product").borderWidth(1).build())
                .add(CellText.builder().text("2018").horizontalAlignment(CENTER).borderWidth(1).build())
                .add(CellText.builder().text("2019").horizontalAlignment(CENTER).borderWidth(1).build())
                .add(CellText.builder().text("Total").horizontalAlignment(CENTER).borderWidth(1).build())
                .backgroundColor(BLUE_DARK)
                .textColor(Color.WHITE)
                .font(PDType1Font.HELVETICA_BOLD)
                .fontSize(9)
                .build();

        tableBuilder.addRow(headerRow);

        // ... and some data rows
        double grandTotal = 0;
        for (int i = 0; i < data.length; i++) {
            final Object[] dataRow = data[i];
            final double total = (double) dataRow[1] + (double) dataRow[2];
            grandTotal += total;

            tableBuilder.addRow(Row.builder()
                    .add(CellText.builder().text(String.valueOf(dataRow[0])).borderWidth(1).build())
                    .add(CellText.builder().text(dataRow[1] + " €").horizontalAlignment(RIGHT).borderWidth(1).build())
                    .add(CellText.builder().text(dataRow[2] + " €").horizontalAlignment(RIGHT).borderWidth(1).build())
                    .add(CellText.builder().text(total + " €").horizontalAlignment(RIGHT).borderWidth(1).build())
                    .backgroundColor(i % 2 == 0 ? BLUE_LIGHT_1 : BLUE_LIGHT_2)
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
                        .textColor(Color.WHITE)
                        .horizontalAlignment(RIGHT)
                        .backgroundColor(BLUE_DARK)
                        .fontSize(6)
                        .font(PDType1Font.HELVETICA_OBLIQUE)
                        .borderWidth(1)
                        .build())
                .add(CellText.builder().text(grandTotal + " €").backgroundColor(Color.LIGHT_GRAY)
                        .font(PDType1Font.HELVETICA_BOLD_OBLIQUE)
                        .horizontalAlignment(RIGHT)
                        .verticalAlignment(TOP)
                        .borderWidth(1)
                        .build())
                .build());
                
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
                 .table(tableBuilder.build())
                 .tableStartX(startX)
                 .tableStartY(startY)
                 .build()
                 .draw();
         contentStream.close();

         document.save(fileToSaveTo);
         document.close();
    }
}
