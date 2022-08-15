package org.vandeseer.integrationtest.image;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.ImageCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.vandeseer.TestUtils.getActualPdfFor;
import static org.vandeseer.TestUtils.getExpectedPdfFor;
import static org.vandeseer.easytable.settings.HorizontalAlignment.*;
import static org.vandeseer.easytable.settings.VerticalAlignment.*;

public class ImageCellAlignmentTest {

    public static final String FILE_NAME = "imageCellAlignment.pdf";

    @Test
    public void test() throws IOException {
        TestUtils.createAndSaveDocumentWithTables(FILE_NAME,
                createTableWithImageCellThatHasImageAligned(TOP, LEFT),
                createTableWithImageCellThatHasImageAligned(TOP, RIGHT),
                createTableWithImageCellThatHasImageAligned(MIDDLE, CENTER),
                createTableWithImageCellThatHasImageAligned(MIDDLE, RIGHT),
                createTableWithImageCellThatHasImageAligned(BOTTOM, LEFT),
                createTableWithImageCellThatHasImageAligned(BOTTOM, CENTER)
        );

        CompareResult compareResult = new PdfComparator<>(getExpectedPdfFor(FILE_NAME), getActualPdfFor(FILE_NAME)).compare();
        assertTrue(compareResult.isEqual());
    }

    private Table createTableWithImageCellThatHasImageAligned(VerticalAlignment verticalAlignment, HorizontalAlignment horizontalAlignment) throws IOException {
        float userPageWidth = new PDPage().getMediaBox().getWidth() - (2.0F * 72.0f);
        float columnWidth = userPageWidth / 8.0F;

        Table.TableBuilder tableBuilder = Table.builder()
                .backwardsCompatibleFontHeight(true)
                .addColumnsOfWidth(columnWidth, columnWidth, columnWidth, columnWidth,
                        columnWidth, columnWidth, columnWidth, columnWidth)
                .fontSize(8)
                .font(PDType1Font.HELVETICA)
                .wordBreak(true);

        Row.RowBuilder rowBuilder = Row.builder()
                .add(TextCell.builder()
                        .borderWidth(1)
                        .padding(4)
                        .text("Cell 1")
                        .build())
                .add(TextCell.builder()
                        .borderWidth(1)
                        .padding(4)
                        .text("Cell 2")
                        .build())
                .add(TextCell.builder()
                        .borderWidth(1)
                        .padding(4)
                        .text("Cell 3")
                        .build())
                .add(TextCell.builder()
                        .colSpan(4)
                        .borderWidth(1)
                        .padding(4)
                        .colSpan(2)
                        .text("Cell 4\nMore text\nEven more text\nEven more text\n" +
                                "Even more text\nEven more text\nEven more text")
                        .build())
                .add(TextCell.builder()
                        .borderWidth(1)
                        .padding(4)
                        .text(verticalAlignment.toString() + ", " + horizontalAlignment.toString())
                        .build())
                .add(ImageCell.builder()
                        .verticalAlignment(verticalAlignment)
                        .horizontalAlignment(horizontalAlignment)
                        .borderWidth(1)
                        .padding(2)
                        .image(TestUtils.createSampleImage())
                        .scale(0.2f)
                        .colSpan(2)
                        .build());

        tableBuilder.addRow(rowBuilder.font(PDType1Font.HELVETICA)
                .fontSize(8)
                .horizontalAlignment(CENTER)
                .build());

        return tableBuilder.build();
    }
}


