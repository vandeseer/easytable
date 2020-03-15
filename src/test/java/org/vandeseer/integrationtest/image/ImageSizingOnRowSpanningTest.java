package org.vandeseer.integrationtest.image;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.ImageCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import static junit.framework.TestCase.assertTrue;
import static org.vandeseer.TestUtils.getActualPdfFor;
import static org.vandeseer.TestUtils.getExpectedPdfFor;

// Test for ISSUE-40 https://github.com/vandeseer/easytable/issues/40
//      and ISSUE-60 https://github.com/vandeseer/easytable/issues/64
public class ImageSizingOnRowSpanningTest {

    public static final String FILE_NAME = "imageCellSizingOnRowAndColSpanning.pdf";

    @Test
    public void testImageSizingOnRowAndColSpanning() throws Exception {
        final PDImageXObject glider = TestUtils.createGliderImage();

        TestUtils.createAndSaveDocumentWithTables(
                FILE_NAME,
                createRowSpanningTable(glider),
                createColSpanningTable(glider)
        );

        CompareResult compareResult = new PdfComparator<>(getExpectedPdfFor(FILE_NAME), getActualPdfFor(FILE_NAME)).compare();
        assertTrue(compareResult.isEqual());
    }

    private Table createRowSpanningTable(PDImageXObject glider) {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(50, 30, 80, 20);
        // First row
        tableBuilder.addRow(
                Row.builder()
                        .add(ImageCell.builder().colSpan(2).image(glider).borderWidth(1).build())
                        .add(ImageCell.builder().colSpan(2).image(glider).borderWidth(1).build())
                        .build());

        // Second row
        tableBuilder.addRow(
                Row.builder()
                        .add(ImageCell.builder().image(glider).borderWidth(1).build())
                        .add(ImageCell.builder().image(glider).borderWidth(1).build())
                        .add(ImageCell.builder().image(glider).borderWidth(1).build())
                        .add(ImageCell.builder().image(glider).borderWidth(1).build())
                        .build());

        // Third row
        tableBuilder.addRow(
                Row.builder()
                        .add(ImageCell.builder().colSpan(4).scale(2f).image(glider).borderWidth(1).build())
                        .build());

        return tableBuilder.build();
    }

    private Table createColSpanningTable(PDImageXObject glider) {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 100);
        // First row
        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder().rowSpan(2).text("Row span").borderWidth(1).build())
                        .add(ImageCell.builder().image(glider).borderWidth(1).build())
                        .build());

        // Second row
        tableBuilder.addRow(
                Row.builder()
                        .add(ImageCell.builder().image(glider).borderWidth(1).build())
                        .build());
        return tableBuilder.build();
    }

}
