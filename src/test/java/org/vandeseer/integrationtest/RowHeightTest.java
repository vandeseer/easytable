package org.vandeseer.integrationtest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.Ignore;
import org.junit.Test;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellImage;
import org.vandeseer.easytable.structure.cell.CellText;

import java.io.IOException;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.COURIER_BOLD;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;

/**
 * Tests that the height of {@link Row}s can be determined before the Table is built.
 */
public class RowHeightTest {

    @Test
    public void testHeightForTextWithBreak() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(10, 10, 10)
                .wordBreak(true)
                .horizontalAlignment(CENTER)
                .fontSize(10).font(HELVETICA);
        Row row = Row.builder()
                .add(CellText.builder().text(RandomStringUtils.randomAlphabetic(23)).span(2).borderWidth(1).build())
                .add(CellText.builder().text("Booz").build())
                .wordBreak(true)
                .font(COURIER_BOLD).fontSize(8)
                .build();

        tableBuilder.addRow(row);

        float heightBefore = row.getHeight();
        tableBuilder.build();
        float heightAfter = row.getHeight();

        assertThat(heightAfter, equalTo(heightBefore));
    }

    @Test
    public void testHeightForTextWithoutBreak() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(10, 10, 10)
                .horizontalAlignment(CENTER)
                .fontSize(10).font(HELVETICA);
        Row row = Row.builder()
                .add(CellText.builder().text(RandomStringUtils.randomAlphabetic(23)).span(2).borderWidth(1).build())
                .add(CellText.builder().text("Booz").build())
                .font(COURIER_BOLD).fontSize(8)
                .build();

        tableBuilder.addRow(row);

        float heightBefore = row.getHeight();
        tableBuilder.build();
        float heightAfter = row.getHeight();

        assertThat(heightAfter, equalTo(heightBefore));
    }

    @Test
    public void testHeightForImageCells() throws IOException {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 100)
                .horizontalAlignment(CENTER)
                .fontSize(10).font(HELVETICA);

        final byte[] bytes1 = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("pic1.jpg"));
        final PDImageXObject image1 = PDImageXObject.createFromByteArray(new PDDocument(), bytes1, "test1");

        final byte[] bytes2 = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("pic2.jpg"));
        final PDImageXObject image2 = PDImageXObject.createFromByteArray(new PDDocument(), bytes2, "test2");


        Row row = Row.builder()
                .add(CellImage.builder().image(image1).borderWidth(1).build())
                .add(CellImage.builder().image(image2).build())
                .build();

        tableBuilder.addRow(row);

        float heightBefore = row.getHeight();
        tableBuilder.build();
        float heightAfter = row.getHeight();

        assertThat(heightAfter, equalTo(heightBefore));
    }
}