package org.vandeseer.integrationtest;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.RepeatedHeaderTableDrawer;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName.HELVETICA_BOLD;
import static org.vandeseer.TestUtils.getActualPdfFor;
import static org.vandeseer.TestUtils.getExpectedPdfFor;

public class TableOverSeveralPagesTest {

    private static final String SEVERAL_PAGES_TABLE_1_FILE_NAME = "severalPagesTable1.pdf";
    private static final String SEVERAL_PAGES_TABLE_2_FILE_NAME = "severalPagesTable2.pdf";

    private static final String SEVERAL_PAGES_REP_HEADER_TABLE_1_FILE_NAME = "severalPagesTableRepeatedHeader.pdf";
    private static final String SEVERAL_PAGES_REP_HEADER_TABLE_2_FILE_NAME = "severalPagesTableRepeatedHeaderMultipleRows.pdf";

    private static final Color DARK_BLUE = new Color(46, 77, 97);
    private static final Color CUSTOM_GRAY = new Color(136, 136, 136);

    @Test
    public void drawMultipageTable() throws IOException {

        try (final PDDocument document = new PDDocument()) {
            drawMultipageTableOn(document);
            document.save(TestUtils.TARGET_FOLDER + "/" + SEVERAL_PAGES_TABLE_1_FILE_NAME);
        }

        final CompareResult compareResult = new PdfComparator<>(
                                                    getExpectedPdfFor(SEVERAL_PAGES_TABLE_1_FILE_NAME),
                                                    getActualPdfFor(SEVERAL_PAGES_TABLE_1_FILE_NAME)
                                                ).compare();

        assertTrue(compareResult.isEqual());
    }

    @Test
    public void drawSeveralMultipageTableOnSameDocument() throws IOException {

        try (final PDDocument document = new PDDocument()) {
            drawMultipageTableOn(document);
            drawMultipageTableOn(document);

            document.save(TestUtils.TARGET_FOLDER + "/" + SEVERAL_PAGES_TABLE_2_FILE_NAME);
        }

        final CompareResult compareResult = new PdfComparator<>(
                getExpectedPdfFor(SEVERAL_PAGES_TABLE_2_FILE_NAME),
                getActualPdfFor(SEVERAL_PAGES_TABLE_2_FILE_NAME)
        ).compare();

        assertTrue(compareResult.isEqual());
    }

    @Test
    public void createTwoPageTableWithRepeatedHeader() throws IOException {

        try (final PDDocument document = new PDDocument()) {

            RepeatedHeaderTableDrawer.builder()
                    .table(createTable())
                    .startX(50)
                    .startY(100F)
                    .endY(50F) // note: if not set, table is drawn over the end of the page
                    .build()
                    .draw(() -> document, () -> new PDPage(PDRectangle.A4), 50f);

            document.save(TestUtils.TARGET_FOLDER + "/" + SEVERAL_PAGES_REP_HEADER_TABLE_1_FILE_NAME);
        }

        final CompareResult compareResult = new PdfComparator<>(
                getExpectedPdfFor(SEVERAL_PAGES_REP_HEADER_TABLE_1_FILE_NAME),
                getActualPdfFor(SEVERAL_PAGES_REP_HEADER_TABLE_1_FILE_NAME)
        ).compare();

        assertTrue(compareResult.isEqual());
    }

    @Test
    public void createTwoPageTableWithRepeatedHeaderOfThreeRows() throws IOException {

        try (final PDDocument document = new PDDocument()) {

            RepeatedHeaderTableDrawer.builder()
                    .table(createTableWithThreeHeaderRows())
                    .startX(50)
                    .startY(200F)
                    .endY(50F) // note: if not set, table is drawn over the end of the page
                    .numberOfRowsToRepeat(2)
                    .build()
                    .draw(() -> document, () -> new PDPage(PDRectangle.A4), 50f);

            document.save(TestUtils.TARGET_FOLDER + "/" + SEVERAL_PAGES_REP_HEADER_TABLE_2_FILE_NAME);
        }

        final CompareResult compareResult = new PdfComparator<>(
                getExpectedPdfFor(SEVERAL_PAGES_REP_HEADER_TABLE_2_FILE_NAME),
                getActualPdfFor(SEVERAL_PAGES_REP_HEADER_TABLE_2_FILE_NAME)
        ).compare();

        assertTrue(compareResult.isEqual());
    }

    private Table createTableWithThreeHeaderRows() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(200, 200);

        tableBuilder
                .addRow(Row.builder()
                        .add(createHeaderCell("Some"))
                        .add(createHeaderCell("Header"))
                        .build())
                .addRow(Row.builder()
                        .add(TextCell.builder().text("This is a longer text that could be used to describe some " +
                                "data of the header. It is only used as a placeholder here.")
                                .colSpan(2)
                                .fontSize(6)
                                .horizontalAlignment(HorizontalAlignment.CENTER)
                                .padding(12)
                                .borderColor(DARK_BLUE)
                                .borderWidthBottom(2f)
                                .build())
                        .build());

        for (int i = 0; i < 150; i++) {
            tableBuilder.addRow(
                    Row.builder()
                            .add(TextCell.builder()
                                    .text("Row " + i)
                                    .textColor(CUSTOM_GRAY)
                                    .borderColor(CUSTOM_GRAY)
                                    .borderWidthBottom(2f)
                                    .padding(12f)
                                    .build())
                            .add(TextCell.builder()
                                    .text("Value " + i)
                                    .textColor(CUSTOM_GRAY)
                                    .borderColor(CUSTOM_GRAY)
                                    .borderWidthBottom(2f)
                                    .padding(12f)
                                    .build())
                            .build());
        }

        return tableBuilder.build();
    }

    private TextCell createHeaderCell(String text) {
        return TextCell.builder()
                .font(new PDType1Font(HELVETICA_BOLD))
                .text(text.toUpperCase())
                .backgroundColor(DARK_BLUE)
                .padding(16f)
                .textColor(Color.WHITE)
                .borderColor(Color.WHITE)
                .borderWidth(2f)
                .build();
    }

    private void drawMultipageTableOn(PDDocument document) throws IOException {
        TableDrawer.builder()
                .table(createTable())
                .startX(50)
                .startY(100F)
                .endY(50F) // note: if not set, table is drawn over the end of the page
                .build()
                .draw(() -> document, () -> new PDPage(PDRectangle.A4), 50f);
    }

    private Table createTable() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnOfWidth(200)
                .addColumnOfWidth(200);

        TextCell dummyHeaderCell = TextCell.builder()
                .text("Header dummy")
                .backgroundColor(Color.BLUE)
                .textColor(Color.WHITE)
                .borderWidth(1F)
                .build();

        tableBuilder.addRow(
                Row.builder()
                        .add(dummyHeaderCell)
                        .add(dummyHeaderCell)
                        .build());

        for (int i = 0; i < 50; i++) {
            tableBuilder.addRow(
                    Row.builder()
                            .add(TextCell.builder()
                                    .text("dummy " + i)
                                    .borderWidth(1F)
                                    .build())
                            .add(TextCell.builder()
                                    .text("dummy " + i)
                                    .borderWidth(1F)
                                    .build())
                            .build());
        }

        return tableBuilder.build();
    }

}
