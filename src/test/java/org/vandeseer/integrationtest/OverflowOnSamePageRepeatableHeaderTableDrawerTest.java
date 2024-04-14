package org.vandeseer.integrationtest;

import static junit.framework.TestCase.assertTrue;
import static org.vandeseer.TestUtils.getActualPdfFor;
import static org.vandeseer.TestUtils.getExpectedPdfFor;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.OverflowOnSamePageRepeatableHeaderTableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;

public class OverflowOnSamePageRepeatableHeaderTableDrawerTest {
	   private static final String OVERFLOW_ON_SAME_PAGE_REPEATABLE_HEADER_PDF = "overflowOnSamePageRepeatableHeader.pdf";

	    @Test
	    public void createTableWithOverflowOnSamePageAndRepeatableHeader() throws IOException {

	        try (final PDDocument document = new PDDocument()) {

	            OverflowOnSamePageRepeatableHeaderTableDrawer.builder()
	                    .table(createTable())
	                    .startX(50)
	                    .lanesPerPage(3)
	                    .numberOfRowsToRepeat(2)
	                    .spaceInBetween(25)
	                    .endY(50F) // note: if not set, table is drawn over the end of the page
	                    .build()
	                    .draw(() -> document, () -> new PDPage(PDRectangle.A4), 100f);

	            document.save(TestUtils.TARGET_FOLDER + "/" + OVERFLOW_ON_SAME_PAGE_REPEATABLE_HEADER_PDF);
	        }

	        final CompareResult compareResult = new PdfComparator<>(
	                getExpectedPdfFor(OVERFLOW_ON_SAME_PAGE_REPEATABLE_HEADER_PDF),
	                getActualPdfFor(OVERFLOW_ON_SAME_PAGE_REPEATABLE_HEADER_PDF)
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
	        
	        TextCell dummyHeaderCell2 = TextCell.builder()
	                .text("HD2")
	                .backgroundColor(Color.BLACK)
	                .textColor(Color.RED)
	                .borderWidth(1F)
	                .build();

	        tableBuilder.addRow(
	                Row.builder()
	                        .add(dummyHeaderCell)
	                        .add(dummyHeaderCell)
	                        .build());
	        
	        tableBuilder.addRow(
	                Row.builder()
	                        .add(dummyHeaderCell2)
	                        .add(dummyHeaderCell2)
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
