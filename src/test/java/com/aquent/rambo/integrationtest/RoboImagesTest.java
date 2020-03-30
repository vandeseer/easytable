package com.aquent.rambo.integrationtest;

import static com.aquent.rambo.easytable.settings.HorizontalAlignment.RIGHT;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.Test;

import com.aquent.rambo.TestUtils;
import com.aquent.rambo.easytable.structure.Row;
import com.aquent.rambo.easytable.structure.Table;
import com.aquent.rambo.easytable.structure.cell.ImageCell;
import com.aquent.rambo.easytable.structure.cell.RoboImageCell;
import com.aquent.rambo.easytable.structure.cell.TextCell;

public class RoboImagesTest {

    @Test
    public void testRoboImage() throws Exception {

        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(50, 50);

        final byte[] bytes1 = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("pic1.jpg"));
        final PDImageXObject image1 = PDImageXObject.createFromByteArray(new PDDocument(), bytes1, "test1");

        final byte[] bytes2 = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("pic2.jpg"));
        final PDImageXObject image2 = PDImageXObject.createFromByteArray(new PDDocument(), bytes2, "test2");

        final byte[] bytes3 = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("user_no_image.png"));
        final PDImageXObject image3 = PDImageXObject.createFromByteArray(new PDDocument(), bytes3, "noImage");
        
        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder().text("first").build())
                        .add(TextCell.builder().text("second").horizontalAlignment(RIGHT).build())
                        .build());


        tableBuilder.addRow(
                Row.builder()
                        .add(RoboImageCell.builder().image(image1).imageText("10").borderWidth(0).build())
                        .add(ImageCell.builder().image(image2).borderWidth(0).build())
                        .build());
        
        tableBuilder.addRow(
                Row.builder()
                        .add(RoboImageCell.builder().image(image3).imageText("3").borderWidth(0).build())
                        .add(TextCell.builder().text("third").build())
                        .build());
        
        tableBuilder.addRow(
                Row.builder()
                        .add(RoboImageCell.builder().image(image3).imageText("40").borderWidth(0).build())
                        .add(TextCell.builder().text("four").build())
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

        
        final Table.TableBuilder tableBuilder2 = Table.builder()
                .addColumnsOfWidth(500);

       
        tableBuilder2.addRow(
                Row.builder()
                        .add(ImageCell.builder().image(image1).borderWidth(0).build())
                        .build());
        
        TestUtils.createAndSaveDocumentWithTables("roboimages.pdf", tableBuilder.build(), tableBuilder2.build());
    }

}
