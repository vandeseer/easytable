package org.vandeseer.integrationtest;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.ImageCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;
import java.io.IOException;

import static java.awt.Color.*;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.*;
import static org.vandeseer.TestUtils.createGliderImage;
import static org.vandeseer.TestUtils.createTuxImage;
import static org.vandeseer.easytable.settings.HorizontalAlignment.*;
import static org.vandeseer.easytable.settings.VerticalAlignment.MIDDLE;

public class ExcelLikeExampleTest {

    private final static Color BLUE_DARK = new Color(76, 129, 190);
    private final static Color BLUE_LIGHT_1 = new Color(186, 206, 230);
    private final static Color BLUE_LIGHT_2 = new Color(218, 230, 242);

    private final static Color ORANGE = new Color(255, 180, 0);
    private final static Color GRAY_LIGHT_1 = new Color(245, 245, 245);
    private final static Color GRAY_LIGHT_2 = new Color(240, 240, 240);
    private final static Color GRAY_LIGHT_3 = new Color(216, 216, 216);

    @Test
    public void createDocumentWithExcelLikeTables() throws IOException {
        TestUtils.createAndSaveDocumentWithTables("excelLike.pdf",
                createSimpleExampleTable(),
                createComplexExampleTable()
        );
    }

    private Table createSimpleExampleTable() {
        
        // Some data
        final Object[][] data = {
                {"Whisky", 134.4, 145.98},
                {"Beer", 768.2, 677.9},
                {"Gin", 456.45, 612.0},
                {"Vodka", 302.71, 465.2}
        };

        // Define the table structure first
        final TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 50, 50, 50)
                .fontSize(8)
                .font(HELVETICA)
                .borderColor(Color.WHITE);

        // Add the header row ...
        final Row headerRow = Row.builder()
                .add(TextCell.builder().text("Product").horizontalAlignment(LEFT).borderWidth(1).build())
                .add(TextCell.builder().text("2018").borderWidth(1).build())
                .add(TextCell.builder().text("2019").borderWidth(1).build())
                .add(TextCell.builder().text("Total").borderWidth(1).build())
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
                    .add(TextCell.builder().text(String.valueOf(dataRow[0])).horizontalAlignment(LEFT).borderWidth(1).build())
                    .add(TextCell.builder().text(dataRow[1] + " €").borderWidth(1).build())
                    .add(TextCell.builder().text(dataRow[2] + " €").borderWidth(1).build())
                    .add(TextCell.builder().text(total + " €").borderWidth(1).build())
                    .backgroundColor(i % 2 == 0 ? BLUE_LIGHT_1 : BLUE_LIGHT_2)
                    .horizontalAlignment(RIGHT)
                    .build())
                    .wordBreak(true);
        }

        // Add a final row
        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text("This spans over 3 cells, is right aligned and its text is so long that it even breaks. " +
                        "Also it shows the grand total in the next cell and furthermore vertical alignment is shown:")
                        .colSpan(3)
                        .lineSpacing(1f)
                        .borderWidthTop(1)
                        .textColor(WHITE)
                        .backgroundColor(BLUE_DARK)
                        .fontSize(6)
                        .font(HELVETICA_OBLIQUE)
                        .borderWidth(1)
                        .build())
                .add(TextCell.builder().text(grandTotal + " €").backgroundColor(LIGHT_GRAY)
                        .font(HELVETICA_BOLD_OBLIQUE)
                        .verticalAlignment(VerticalAlignment.TOP)
                        .borderWidth(1)
                        .build())
                .horizontalAlignment(RIGHT)
                .build());

        return tableBuilder.build();
    }

    private Table createComplexExampleTable() throws IOException {

        final TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(50, 100, 40, 70, 120)
                .borderColor(WHITE)
                .textColor(DARK_GRAY)
                .fontSize(7)
                .font(HELVETICA);

        // Header row
        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().borderWidth(1).padding(6).text("Left").build())
                .add(TextCell.builder().borderWidth(1).padding(6).text("Middle").colSpan(3).build())
                .add(TextCell.builder().borderWidth(1).padding(6).text("Right").build())
                .backgroundColor(ORANGE)
                .textColor(WHITE)
                .font(HELVETICA_BOLD)
                .fontSize(8)
                .horizontalAlignment(CENTER)
                .build()
        );

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().borderWidth(1).text("Glider")
                        .verticalAlignment(MIDDLE)
                        .horizontalAlignment(CENTER)
                        .backgroundColor(GRAY_LIGHT_1)
                        .rowSpan(7)
                        .build())
                .add(ImageCell.builder()
                        .verticalAlignment(MIDDLE)
                        .horizontalAlignment(CENTER)
                        .borderWidth(1)
                        .image(createGliderImage())
                        .scale(0.4f)
                        .rowSpan(6)
                        .build())
                .add(TextCell.builder().borderWidth(1).text("Gray").colSpan(2).backgroundColor(GRAY_LIGHT_2).build())
                .add(TextCell.builder().borderWidth(1)
                        .text("\"I'm doing a (free) operating system (just a hobby, " +
                                "won't be big and professional like gnu) for 386(486) AT clones\" \n\n " +
                                "– Linus Torvalds")
                        .rowSpan(14)
                        .verticalAlignment(MIDDLE)
                        .horizontalAlignment(JUSTIFY)
                        .padding(14)
                        .font(HELVETICA_OBLIQUE)
                        .backgroundColor(GRAY_LIGHT_1)
                        .build())
                .build()
        );

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().borderWidth(1).text("Darker Gray").colSpan(2).backgroundColor(GRAY_LIGHT_3).build())
                .build()
        );

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().borderWidth(1).text("Gray Again").colSpan(2).backgroundColor(GRAY_LIGHT_2).build())
                .build()
        );

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().borderWidth(1).text("And Darker Gray").colSpan(2).backgroundColor(GRAY_LIGHT_3).build())
                .build()
        );

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().borderWidth(1).text("Right!").rowSpan(2).backgroundColor(GRAY_LIGHT_2).build())
                .add(TextCell.builder().borderWidth(1).text("Aligned!").horizontalAlignment(RIGHT).backgroundColor(GRAY_LIGHT_2).build())
                .build()
        );

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().borderWidth(1).text("Left.").backgroundColor(GRAY_LIGHT_3).build())
                .build()
        );

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().borderWidth(1).text("Here some text.").backgroundColor(GRAY_LIGHT_2).colSpan(3).build())
                .build()
        );

        // Second part

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().borderWidth(1).text("Tux")
                        .verticalAlignment(MIDDLE)
                        .horizontalAlignment(CENTER)
                        .backgroundColor(GRAY_LIGHT_1)
                        .rowSpan(7)
                        .build())
                .add(ImageCell.builder()
                        .verticalAlignment(MIDDLE)
                        .horizontalAlignment(CENTER)
                        .borderWidth(1)
                        .image(createTuxImage())
                        .scale(0.4f)
                        .rowSpan(6)
                        .build())
                .add(TextCell.builder().borderWidth(1).text("Darker Gray Again.").colSpan(2).backgroundColor(GRAY_LIGHT_3).build())
                .build()
        );

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().borderWidth(1).text("Bit Lighter.").colSpan(2).backgroundColor(GRAY_LIGHT_2).build())
                .build()
        );

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().borderWidth(1).text("Well. Actually not.").colSpan(2).backgroundColor(GRAY_LIGHT_3).build())
                .build()
        );

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().borderWidth(1).text("Now.").colSpan(2).backgroundColor(GRAY_LIGHT_2).build())
                .build()
        );

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().borderWidth(1).text("Yeah.").rowSpan(2).backgroundColor(GRAY_LIGHT_3).build())
                .add(TextCell.builder().borderWidth(1).text("This and ...")
                        .horizontalAlignment(RIGHT)
                        .backgroundColor(GRAY_LIGHT_3)
                        .build())
                .build()
        );

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().borderWidth(1).text("... that: right aligned!")
                        .backgroundColor(GRAY_LIGHT_2)
                        .horizontalAlignment(RIGHT)
                        .build())
                .build()
        );

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().borderWidth(1).text("Here some text, too.").backgroundColor(GRAY_LIGHT_3).colSpan(3).build())
                .build()
        );

        return tableBuilder.build();
    }

}
