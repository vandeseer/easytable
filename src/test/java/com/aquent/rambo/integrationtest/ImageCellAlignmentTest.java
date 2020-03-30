package com.aquent.rambo.integrationtest;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;

import com.aquent.rambo.TestUtils;
import com.aquent.rambo.easytable.settings.HorizontalAlignment;
import com.aquent.rambo.easytable.settings.VerticalAlignment;
import com.aquent.rambo.easytable.structure.Row;
import com.aquent.rambo.easytable.structure.Table;
import com.aquent.rambo.easytable.structure.cell.ImageCell;
import com.aquent.rambo.easytable.structure.cell.TextCell;

import static com.aquent.rambo.easytable.settings.HorizontalAlignment.*;
import static com.aquent.rambo.easytable.settings.VerticalAlignment.*;

import java.io.IOException;

public class ImageCellAlignmentTest {

    @Test
    public void test() throws IOException {
        TestUtils.createAndSaveDocumentWithTables("imageCellAlignment.pdf",
                createTableWithImageCellThatHasImageAligned(TOP, LEFT),
                createTableWithImageCellThatHasImageAligned(TOP, RIGHT),
                createTableWithImageCellThatHasImageAligned(MIDDLE, CENTER),
                createTableWithImageCellThatHasImageAligned(MIDDLE, RIGHT),
                createTableWithImageCellThatHasImageAligned(BOTTOM, LEFT),
                createTableWithImageCellThatHasImageAligned(BOTTOM, CENTER)
        );
    }

    private Table createTableWithImageCellThatHasImageAligned(VerticalAlignment verticalAlignment, HorizontalAlignment horizontalAlignment) throws IOException {
        float userPageWidth = new PDPage().getMediaBox().getWidth() - (2.0F * 72.0f);
        float columnWidth = userPageWidth / 8.0F;

        Table.TableBuilder tableBuilder = Table.builder()
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
                        .colSpan(2)
                        .build());

        tableBuilder.addRow(rowBuilder.font(PDType1Font.HELVETICA)
                .fontSize(8)
                .horizontalAlignment(CENTER)
                .build());

        return tableBuilder.build();
    }
}


