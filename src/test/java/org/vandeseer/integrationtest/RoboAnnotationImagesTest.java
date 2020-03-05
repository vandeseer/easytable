package org.vandeseer.integrationtest;

import static org.vandeseer.easytable.settings.HorizontalAlignment.RIGHT;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.json.JSONObject;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.AnnotationComment;
import org.vandeseer.easytable.structure.cell.RoboAnnotationImageCell;
import org.vandeseer.easytable.structure.cell.RoboImageCell;
import org.vandeseer.easytable.structure.cell.TextCell;

public class RoboAnnotationImagesTest {

    @Test
    public void testRoboImage() throws Exception {

        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(50, 50);

        final byte[] bytes1 = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("pic1.jpg"));
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
                        .add(RoboImageCell.builder().image(image3).imageText("3").borderWidth(0).build())
                        .add(TextCell.builder().text("third").build())
                        .build());
        
        tableBuilder.addRow(
                Row.builder()
                        .add(RoboImageCell.builder().image(image3).imageText("40").borderWidth(0).build())
                        .add(TextCell.builder().text("four").build())
                        .build());

        final Table.TableBuilder tableBuilder2 = Table.builder()
                .addColumnsOfWidth(500);

        final byte[] bytes4 = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("highlight_background.png"));
        final PDImageXObject highlightedAreaImage = PDImageXObject.createFromByteArray(new PDDocument(), bytes4, "highback");
        
        List<AnnotationComment> annotationsComments = new ArrayList<>();
        AnnotationComment annotationComment1 = new AnnotationComment();
        annotationComment1.setMarkupTypeId(1);
        annotationComment1.setMarkUpJson(new JSONObject("{\"xPosition\":278.58231707317077,\"yPosition\":1036.5853658536587}"));
        annotationComment1.setCommentNumber("7");
        
       
        
        
        AnnotationComment annotationComment2 = new AnnotationComment();
        annotationComment2.setMarkupTypeId(3);
        annotationComment2.setHighlightedAreaImage(highlightedAreaImage);
        annotationComment2.setMarkUpJson(new JSONObject("{\"xPosition\":1249.578101503337,\"yPosition\":864.9923113957504,\"width\":312.8834355828221,\"height\":187.73008124228636}"));
        annotationComment2.setCommentNumber("8");
        
      
        AnnotationComment annotationComment3 = new AnnotationComment();
        annotationComment3.setMarkupTypeId(2);
        annotationComment3.setHighlightedAreaImage(highlightedAreaImage);
        annotationComment3.setMarkUpJson(new JSONObject("{\"xPosition\":1819.8551433842358,\"yPosition\":810.4802224694229,\"width\":168.4451219512195,\"height\":204.72555044220715}"));
        annotationComment3.setCommentNumber("14");
        
        AnnotationComment annotationComment4 = new AnnotationComment();
        annotationComment4.setMarkupTypeId(4);
        annotationComment4.setHighlightedAreaImage(highlightedAreaImage);
        annotationComment4.setMarkUpJson(new JSONObject("{\"xPosition\":1648.8185975609756,\"yPosition\":197.59908536585365}"));
        annotationComment4.setCommentNumber("9");
        annotationComment4.setStampLabelText("Off Brand");
        annotationComment4.setStampLabelColor("#000000");
        annotationComment4.setStampBackgroundColor("#FFFF00");
        
        AnnotationComment annotationComment5 = new AnnotationComment();
        annotationComment5.setMarkupTypeId(4);
        annotationComment5.setHighlightedAreaImage(highlightedAreaImage);
        annotationComment5.setMarkUpJson(new JSONObject("{\"xPosition\":813.0716463414634,\"yPosition\":1201.7911585365855}"));
        annotationComment5.setCommentNumber("10");
        annotationComment5.setStampLabelText("Remove");
        annotationComment5.setStampLabelColor("#000000");
        annotationComment5.setStampBackgroundColor("#FF0000");
        
        AnnotationComment annotationComment6 = new AnnotationComment();
        annotationComment6.setMarkupTypeId(4);
        annotationComment6.setHighlightedAreaImage(highlightedAreaImage);
        annotationComment6.setMarkUpJson(new JSONObject("{\"xPosition\":955.0716463414634,\"yPosition\":1401.7911585365855}"));
        annotationComment6.setCommentNumber("11");
        annotationComment6.setStampLabelText("For Position Only");
        annotationComment6.setStampLabelColor("#000000");
        annotationComment6.setStampBackgroundColor("#FFCC99");
        
        AnnotationComment annotationComment7 = new AnnotationComment();
        annotationComment7.setMarkupTypeId(2);
        annotationComment7.setMarkUpJson(new JSONObject("{\"xPosition\":2182.6602191459842,\"yPosition\":382.88875905478875,\"width\":103.65853658536585,\"height\":181.40238959614825}"));
        annotationComment7.setCommentNumber("2");
        
        
        annotationsComments.add(annotationComment1);
        annotationsComments.add(annotationComment2);
        annotationsComments.add(annotationComment3);
        annotationsComments.add(annotationComment4);
        annotationsComments.add(annotationComment5);
        annotationsComments.add(annotationComment6);
        annotationsComments.add(annotationComment7);
        
        tableBuilder2.addRow(
                Row.builder()
                        .add(RoboAnnotationImageCell.builder().image(image1).imageFooterText("1").originalHeight(2170).originalWidth(2270).annotationsComments(annotationsComments).borderWidth(0).build())
                        .build());
        
        TestUtils.createAndSaveDocumentWithTables("roboannotationimages.pdf", tableBuilder.build(), tableBuilder2.build());
    }

}
