package org.vandeseer.integrationtest;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.ImageCell;
import org.vandeseer.easytable.structure.cell.ImageCell.ImageCellBuilder;
import org.vandeseer.easytable.structure.cell.TextCell;
import org.vandeseer.easytable.structure.cell.TextCell.TextCellBuilder;

import java.awt.*;
import java.io.IOException;

import static java.awt.Color.*;
import static junit.framework.TestCase.assertTrue;
import static org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName.*;
import static org.vandeseer.TestUtils.*;
import static org.vandeseer.easytable.settings.HorizontalAlignment.*;
import static org.vandeseer.easytable.settings.VerticalAlignment.MIDDLE;
import static org.vandeseer.easytable.settings.VerticalAlignment.TOP;

public class ExcelLikeExampleTest {

    private final static Color BLUE_DARK = new Color(76, 129, 190);
    private final static Color BLUE_LIGHT_1 = new Color(186, 206, 230);
    private final static Color BLUE_LIGHT_2 = new Color(218, 230, 242);

    private final static Color GRAY_LIGHT_1 = new Color(245, 245, 245);
    private final static Color GRAY_LIGHT_2 = new Color(240, 240, 240);
    private final static Color GRAY_LIGHT_3 = new Color(216, 216, 216);

    private final static Object[][] DATA = new Object[][]{
            {"Whisky", 134.0, 145.0},
            {"Beer",   768.0, 677.0},
            {"Gin",    456.2, 612.0},
            {"Vodka",  302.3, 467.0}
    };

    private static final String FILE_NAME = "excelLike.pdf";

    @Test
    public void createDocumentWithExcelLikeTables() throws IOException {
        TestUtils.createAndSaveDocumentWithTables(FILE_NAME,
                createSimpleExampleTable(),
                createComplexExampleTable()
        );

        CompareResult compareResult = new PdfComparator<>(getExpectedPdfFor(FILE_NAME), getActualPdfFor(FILE_NAME)).compare();
        assertTrue(compareResult.isEqual());
    }

    private Table createSimpleExampleTable() {
        
        final TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 50, 50, 50)
                .fontSize(8)
                .font(new PDType1Font(HELVETICA))
                .borderColor(Color.WHITE);

        // Add the header row ...
        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text("Product").horizontalAlignment(LEFT).borderWidth(1).build())
                .add(TextCell.builder().text("2018").borderWidth(1).build())
                .add(TextCell.builder().text("2019").borderWidth(1).build())
                .add(TextCell.builder().text("Total").borderWidth(1).build())
                .backgroundColor(BLUE_DARK)
                .textColor(Color.WHITE)
                .font(new PDType1Font(HELVETICA_BOLD))
                .fontSize(9)
                .horizontalAlignment(CENTER)
                .build());

        // ... and some data rows
        double grandTotal = 0;
        for (int i = 0; i < DATA.length; i++) {
            final Object[] dataRow = DATA[i];
            final double total = (double) dataRow[1] + (double) dataRow[2];
            grandTotal += total;

            tableBuilder.addRow(Row.builder()
                    .add(TextCell.builder().text(String.valueOf(dataRow[0])).horizontalAlignment(LEFT).borderWidth(1).build())
                    .add(TextCell.builder().text(dataRow[1] + " €").borderWidth(1).build())
                    .add(TextCell.builder().text(dataRow[2] + " €").borderWidth(1).build())
                    .add(TextCell.builder().text(total + " €").borderWidth(1).build())
                    .backgroundColor(i % 2 == 0 ? BLUE_LIGHT_1 : BLUE_LIGHT_2)
                    .horizontalAlignment(RIGHT)
                    .build());
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
                        .font(new PDType1Font(HELVETICA_OBLIQUE))
                        .borderWidth(1)
                        .build())
                .add(TextCell.builder().text(grandTotal + " €").backgroundColor(LIGHT_GRAY)
                        .font(new PDType1Font(HELVETICA_BOLD_OBLIQUE))
                        .verticalAlignment(TOP)
                        .borderWidth(1)
                        .build())
                .horizontalAlignment(RIGHT)
                .build());

        return tableBuilder.build();
    }


    private Table createComplexExampleTable() throws IOException {

        return Table.builder()
                .addColumnsOfWidth(50, 100, 40, 70, 120)
                .borderColor(WHITE)
                .textColor(DARK_GRAY)
                .fontSize(7)
                .font(new PDType1Font(HELVETICA))
                .addRow(createHeaderRow())
                .addRow(create1stDataRow())
                .addRow(create2ndDataRow())
                .addRow(create3rdDataRow())
                .addRow(create4thDataRow())
                .addRow(create5thDataRow())
                .addRow(create6thDataRow())
                .addRow(create7thDataRow())
                .addRow(create8thDataRow())
                .addRow(create9thDataRow())
                .addRow(create10thDataRow())
                .addRow(create11thDataRow())
                .addRow(create12thDataRow())
                .addRow(create13thDataRow())
                .addRow(create14thDataRow())
                .build();
    }

    private Row createHeaderRow() {
        return Row.builder()
            .add(TextCell.builder().borderWidth(1).padding(6).text("Left").build())
            .add(TextCell.builder().borderWidth(1).padding(6).text("Middle").colSpan(3).build())
            .add(TextCell.builder().borderWidth(1).padding(6).text("Right").build())
            .backgroundColor(GRAY)
            .textColor(WHITE)
            .font(new PDType1Font(HELVETICA_BOLD))
            .fontSize(8)
            .horizontalAlignment(CENTER)
            .build();
    }

    private Row create1stDataRow() throws IOException {
        return Row.builder()
            .add(createAndGetGliderTextCellBuilder().rowSpan(7).build())
            .add(createAndGetGliderImageCellBuilder().rowSpan(6).build())
            .add(TextCell.builder().borderWidth(1).text("Gray").colSpan(2).backgroundColor(GRAY_LIGHT_2).build())
            .add(createAndGetTorvaldsQuoteCellBuilder().rowSpan(14).build())
            .build();
    }

    private Row create2ndDataRow() {
        return Row.builder()
            .add(TextCell.builder()
                    .borderWidth(1)
                    .text("Darker Gray")
                    .colSpan(2)
                    .backgroundColor(GRAY_LIGHT_3)
                    .build())
            .build();
    }

    private Row create3rdDataRow() {
        return Row.builder()
            .add(TextCell.builder()
                    .borderWidth(1)
                    .text("Gray Again")
                    .colSpan(2)
                    .backgroundColor(GRAY_LIGHT_2)
                    .build())
            .build();
    }

    private Row create4thDataRow() {
        return Row.builder()
            .add(TextCell.builder()
                    .borderWidth(1)
                    .text("And Darker Gray")
                    .colSpan(2)
                    .backgroundColor(GRAY_LIGHT_3)
                    .build())
            .build();
    }

    private Row create5thDataRow() {
        return Row.builder()
            .add(TextCell.builder()
                    .borderWidth(1)
                    .text("Right!")
                    .rowSpan(2)
                    .backgroundColor(GRAY_LIGHT_2)
                    .build())
            .add(TextCell.builder()
                    .borderWidth(1)
                    .text("Aligned!")
                    .horizontalAlignment(RIGHT)
                    .backgroundColor(GRAY_LIGHT_2)
                    .build())
            .build();
    }

    private Row create6thDataRow() {
        return Row.builder()
            .add(TextCell.builder()
                    .borderWidth(1)
                    .text("Left.")
                    .backgroundColor(GRAY_LIGHT_3)
                    .build())
            .build();
    }

    private Row create7thDataRow() {
        return Row.builder()
            .add(TextCell.builder()
                    .borderWidth(1)
                    .text("Here some text.")
                    .backgroundColor(GRAY_LIGHT_2)
                    .colSpan(3)
                    .build())
            .build();
    }

    private Row create8thDataRow() throws IOException {
        return Row.builder()
                .add(createAndGetTuxTextCellBuilder().rowSpan(7).build())
                .add(createAndGetTuxImageCellBuilder().rowSpan(6).build())
                .add(TextCell.builder()
                        .borderWidth(1)
                        .text("Darker Gray Again.")
                        .colSpan(2)
                        .backgroundColor(GRAY_LIGHT_3)
                        .build())
                .build();
    }

    private Row create9thDataRow() {
        return Row.builder()
        .add(TextCell.builder().borderWidth(1).text("Bit Lighter.").colSpan(2).backgroundColor(GRAY_LIGHT_2).build())
        .build();
    }

    private Row create10thDataRow() {
        return Row.builder()
        .add(TextCell.builder().borderWidth(1).text("Well. Actually not.").colSpan(2).backgroundColor(GRAY_LIGHT_3).build())
        .build();
    }

    private Row create11thDataRow() {
        return Row.builder().add(TextCell.builder().borderWidth(1).text("Now.").colSpan(2).backgroundColor(GRAY_LIGHT_2).build())
        .build();
    }

    private Row create12thDataRow() {
        return Row.builder().add(TextCell.builder().borderWidth(1).text("Yeah.").rowSpan(2).backgroundColor(GRAY_LIGHT_3).build())
        .add(TextCell.builder().borderWidth(1).text("This and ...")
                .horizontalAlignment(RIGHT)
                .backgroundColor(GRAY_LIGHT_3)
                .build())
        .build();
    }

    private Row create13thDataRow() {
        return Row.builder()
        .add(TextCell.builder().borderWidth(1).text("... that: right aligned!")
                .backgroundColor(GRAY_LIGHT_2)
                .horizontalAlignment(RIGHT)
                .build())
        .build();
    }

    private Row create14thDataRow() {
        return Row.builder()
                .add(TextCell.builder().borderWidth(1).text("Here some text, too.").backgroundColor(GRAY_LIGHT_3).colSpan(3).build())
                .build();
    }

    private ImageCellBuilder createAndGetTuxImageCellBuilder() throws IOException {
        return ImageCell.builder()
                .verticalAlignment(MIDDLE)
                .horizontalAlignment(CENTER)
                .borderWidth(1)
                .image(createTuxImage())
                .scale(0.4f);
    }

    private TextCellBuilder createAndGetTuxTextCellBuilder() {
        return TextCell.builder().borderWidth(1).text("Tux")
                .verticalAlignment(MIDDLE)
                .horizontalAlignment(CENTER)
                .backgroundColor(GRAY_LIGHT_1);
    }

    private TextCellBuilder createAndGetTorvaldsQuoteCellBuilder() {
        return TextCell.builder().borderWidth(1)
                .text("\"I'm doing a (free) operating system (just a hobby, " +
                        "won't be big and professional like gnu) for 386(486) AT clones\" \n\n " +
                        "– Linus Torvalds")
                .verticalAlignment(MIDDLE)
                .horizontalAlignment(JUSTIFY)
                .padding(14)
                .font(new PDType1Font(HELVETICA_OBLIQUE))
                .backgroundColor(GRAY_LIGHT_1);
    }

    private ImageCellBuilder createAndGetGliderImageCellBuilder() throws IOException {
        return ImageCell.builder()
                .verticalAlignment(MIDDLE)
                .horizontalAlignment(CENTER)
                .borderWidth(1)
                .image(createGliderImage())
                .scale(0.4f);
    }

    private TextCellBuilder createAndGetGliderTextCellBuilder() {
        return TextCell.builder()
                .borderWidth(1)
                .text("Glider")
                .verticalAlignment(MIDDLE)
                .horizontalAlignment(CENTER)
                .backgroundColor(GRAY_LIGHT_1);
    }

}
