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
import com.aquent.rambo.easytable.structure.cell.RoboImageAndTextsCell;
import com.aquent.rambo.easytable.structure.cell.RoboImageCell;
import com.aquent.rambo.easytable.structure.cell.TextCell;

public class RoboImageAndTextsTest {

    @Test
    public void testRoboImage() throws Exception {

        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(50, 225);

        final byte[] bytes1 = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("blueReplyCommentIcon.png"));
        final PDImageXObject image1 = PDImageXObject.createFromByteArray(new PDDocument(), bytes1, "test1");

        final byte[] bytes3 = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("user_no_image.png"));
        final PDImageXObject image3 = PDImageXObject.createFromByteArray(new PDDocument(), bytes3, "noImage");
        
        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder().text("first").build())
                        .add(TextCell.builder().text("second").horizontalAlignment(RIGHT).build())
                        .build());

        tableBuilder.addRow(
                Row.builder()
                        .add(RoboImageCell.builder().image(image3).imageText("10").borderWidth(1).build())
                        .add(RoboImageAndTextsCell.builder().image(image1).maxHeight(10).text1("Vikram Tambe").text2("Arjun Tambe").borderWidth(1).build())
                        .build());
        
        tableBuilder.addRow(
                Row.builder()
                        .add(RoboImageCell.builder().image(image3).imageText("10").borderWidth(1).build())
                        .add(RoboImageAndTextsCell.builder().image(image1).maxHeight(10).text1("Madhavan Varadachari").text2("Veranika Kantrouskaya").borderWidth(1).build())
                        .build());
        
        tableBuilder.addRow(
                Row.builder()
                        .add(RoboImageCell.builder().image(image3).imageText("10").borderWidth(1).build())
                        .add(RoboImageAndTextsCell.builder().image(image1).maxHeight(10).text1("Aruna Tambe").text2("Eric Frederiksen").borderWidth(1).build())
                        .build());
        
        TestUtils.createAndSaveDocumentWithTables("roboimagesandtexts.pdf", tableBuilder.build());
    }

}
