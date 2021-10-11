package org.vandeseer.integrationtest;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.OverflowOnSamePageTableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.vandeseer.TestUtils.getActualPdfFor;
import static org.vandeseer.TestUtils.getExpectedPdfFor;

public class OverflowOnSamePageTableDrawerTest {

    private static final String OVERFLOW_ON_SAME_PAGE_PDF = "overflowOnSamePage.pdf";

    @Test
    public void createTwoPageTableWithRepeatedHeader() throws IOException {

        try (final PDDocument document = new PDDocument()) {

            OverflowOnSamePageTableDrawer.builder()
                    .table(createTable())
                    .startX(50)
                    .lanesPerPage(3)
                    .spaceInBetween(25)
                    .endY(50F) // note: if not set, table is drawn over the end of the page
                    .build()
                    .draw(() -> document, () -> new PDPage(PDRectangle.A4), 50f);

            document.save(TestUtils.TARGET_FOLDER + "/" + OVERFLOW_ON_SAME_PAGE_PDF);
        }

        final CompareResult compareResult = new PdfComparator<>(
                getExpectedPdfFor(OVERFLOW_ON_SAME_PAGE_PDF),
                getActualPdfFor(OVERFLOW_ON_SAME_PAGE_PDF)
        ).compare();

        assertTrue(compareResult.isEqual());
    }

    private Table createTable() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnOfWidth(60)
                .addColumnOfWidth(60);

        TextCell dummyHeaderCell = TextCell.builder()
                .text("HD")
                .backgroundColor(Color.BLUE)
                .textColor(Color.WHITE)
                .borderWidth(1F)
                .build();

        tableBuilder.addRow(
                Row.builder()
                        .add(dummyHeaderCell)
                        .add(dummyHeaderCell)
                        .build());

        for (int i = 0; i < 200; i++) {
            tableBuilder.addRow(
                    Row.builder()
                            .add(TextCell.builder()
                                    .text("a " + i)
                                    .borderWidth(1F)
                                    .build())
                            .add(TextCell.builder()
                                    .text("b " + i)
                                    .borderWidth(1F)
                                    .build())
                            .build());
        }

        return tableBuilder.build();
    }

}
