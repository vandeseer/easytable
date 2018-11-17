package org.vandeseer.integrationtest;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.CellText;

import java.awt.*;
import java.io.IOException;

import static java.awt.Color.LIGHT_GRAY;
import static java.awt.Color.WHITE;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.*;
import static org.vandeseer.easytable.settings.HorizontalAlignment.*;

public class ExcelLikeExampleTest {

    private final static Color BLUE_DARK = new Color(76, 129, 190);
    private final static Color BLUE_LIGHT_1 = new Color(186, 206, 230);
    private final static Color BLUE_LIGHT_2 = new Color(218, 230, 242);

    private final static String OUTPUT_FILE_NAME = "sampleExcelLike.pdf";

    @Test
    public void tableExample() throws IOException {
        
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
                .backgroundColor(BLUE_DARK)
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
                    .backgroundColor(i % 2 == 0 ? BLUE_LIGHT_1 : BLUE_LIGHT_2)
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
                        .backgroundColor(BLUE_DARK)
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

        TestUtils.createAndSaveDocumentWithTable(tableBuilder.build(), OUTPUT_FILE_NAME);
    }

}
