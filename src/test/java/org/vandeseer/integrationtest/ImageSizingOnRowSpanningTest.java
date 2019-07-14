package org.vandeseer.integrationtest;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.ImageCell;
import org.vandeseer.easytable.structure.cell.TextCell;

// Test for ISSUE-40 https://github.com/vandeseer/easytable/issues/40
public class ImageSizingOnRowSpanningTest {

    @Test
    public void testImageSizingOnRowSpanning() throws Exception {

        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 40);

        final PDImageXObject glider = TestUtils.createGliderImage();

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

        TestUtils.createAndSaveDocumentWithTable("imageCellSizingOnRowSpanning.pdf", tableBuilder.build());
    }

}
