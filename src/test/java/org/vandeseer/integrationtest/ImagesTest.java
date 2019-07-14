package org.vandeseer.integrationtest;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.ImageCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import static org.vandeseer.easytable.settings.HorizontalAlignment.RIGHT;

public class ImagesTest {

    @Test
    public void testImage() throws Exception {

        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(200, 200);

        final byte[] bytes1 = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("pic1.jpg"));
        final PDImageXObject image1 = PDImageXObject.createFromByteArray(new PDDocument(), bytes1, "test1");

        final byte[] bytes2 = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("pic2.jpg"));
        final PDImageXObject image2 = PDImageXObject.createFromByteArray(new PDDocument(), bytes2, "test2");

        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder().text("first").build())
                        .add(TextCell.builder().text("second").horizontalAlignment(RIGHT).build())
                        .build());


        tableBuilder.addRow(
                Row.builder()
                        .add(ImageCell.builder().image(image1).borderWidth(1).build())
                        .add(ImageCell.builder().image(image2).borderWidth(1).build())
                        .build());

        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder()
                                .text("images from \"https://www.techrepublic.com/pictures/the-21-best-it-and-tech-memes-on-the-internet/5/\"")
                                .colSpan(2)
                                .fontSize(6)
                                .borderWidth(1)
                                .build())
                        .build()
        );

        TestUtils.createAndSaveDocumentWithTable("images.pdf", tableBuilder.build());
    }

}
