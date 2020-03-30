package com.aquent.rambo.regressiontest;

import org.junit.Before;
import org.junit.Test;

import com.aquent.rambo.TestUtils;
import com.aquent.rambo.easytable.settings.HorizontalAlignment;
import com.aquent.rambo.easytable.settings.VerticalAlignment;
import com.aquent.rambo.easytable.structure.Row;
import com.aquent.rambo.easytable.structure.Table;
import com.aquent.rambo.easytable.structure.cell.TextCell;

import java.awt.*;
import java.io.IOException;

import static com.aquent.rambo.easytable.settings.HorizontalAlignment.CENTER;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD;

public class Issue50RowSpanningTest {

    @Before
    public void before() {
        TestUtils.assertRegressionFolderExists();
    }

    @Test
    public void testIssue50() throws IOException {
        TestUtils.createAndSaveDocumentWithTable(TestUtils.TARGET_SUBFOLDER_REGRESSION + "/issue50.pdf", createTable());
    }

    private Table createTable() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(35, 92, 50, 50, 72, 240)
                .fontSize(8)
                .font(HELVETICA)
                .borderColor(Color.BLACK);

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text("XXXXXXXXX")
                        .colSpan(4)
                        .lineSpacing(1f)
                        .borderWidthTop(1)
                        .textColor(Color.BLACK)
                        .backgroundColor(Color.RED)
                        .fontSize(14)
                        .font(HELVETICA_BOLD)
                        .verticalAlignment(VerticalAlignment.MIDDLE)
                        .horizontalAlignment(CENTER)
                        .borderWidth(1)
                        .build())
                .add(TextCell.builder().text("YYY YYY")
                        .colSpan(2)
                        .textColor(Color.BLACK)
                        .backgroundColor(Color.RED)
                        .fontSize(11)
                        .font(HELVETICA_BOLD)
                        .verticalAlignment(VerticalAlignment.MIDDLE)
                        .horizontalAlignment(CENTER)
                        .borderWidth(1)
                        .build())
                .horizontalAlignment(HorizontalAlignment.LEFT)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text("#")
                        .rowSpan(3)
                        .lineSpacing(1f)
                        .borderWidthTop(1)
                        .textColor(Color.BLACK)
                        .backgroundColor(Color.GRAY)
                        .fontSize(8)
                        .font(HELVETICA_BOLD)
                        .verticalAlignment(VerticalAlignment.MIDDLE)
                        .horizontalAlignment(CENTER)
                        .borderWidth(1)
                        .build())
                .add(TextCell.builder().text("CCC")
                        .rowSpan(3)
                        .textColor(Color.BLACK)
                        .backgroundColor(Color.GRAY)
                        .fontSize(8)
                        .font(HELVETICA_BOLD)
                        .verticalAlignment(VerticalAlignment.MIDDLE)
                        .horizontalAlignment(CENTER)
                        .borderWidth(1)
                        .build())
                .add(TextCell.builder().text("QWERTY")
                        .colSpan(3)
                        .textColor(Color.BLACK)
                        .backgroundColor(Color.YELLOW)
                        .fontSize(10)
                        .font(HELVETICA_BOLD)
                        .verticalAlignment(VerticalAlignment.MIDDLE)
                        .horizontalAlignment(CENTER)
                        .borderWidth(1)
                        .build())
                .add(TextCell.builder().text("John Black")
                        .textColor(Color.BLACK)
                        .backgroundColor(Color.YELLOW)
                        .fontSize(8)
                        .verticalAlignment(VerticalAlignment.MIDDLE)
                        .horizontalAlignment(CENTER)
                        .borderWidth(1)
                        .build())
                .horizontalAlignment(HorizontalAlignment.LEFT)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text("QQQQ")
                        .colSpan(3)
                        .borderWidthTop(1)
                        .backgroundColor(Color.GRAY)
                        .fontSize(8)
                        .font(HELVETICA_BOLD)
                        .verticalAlignment(VerticalAlignment.MIDDLE)
                        .horizontalAlignment(CENTER)
                        .borderWidth(1)
                        .build())
                .add(TextCell.builder().text("ZZZZZ")
                        .rowSpan(2)
                        .borderWidthTop(1)
                        .backgroundColor(Color.GRAY)
                        .fontSize(8)
                        .font(HELVETICA_BOLD)
                        .verticalAlignment(VerticalAlignment.MIDDLE)
                        .horizontalAlignment(CENTER)
                        .borderWidth(1)
                        .build())
                .horizontalAlignment(HorizontalAlignment.LEFT)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text("X")
                        .lineSpacing(1f)
                        .borderWidthTop(1)
                        .textColor(Color.BLACK)
                        .backgroundColor(Color.GRAY)
                        .fontSize(8)
                        .font(HELVETICA)
                        .verticalAlignment(VerticalAlignment.MIDDLE)
                        .horizontalAlignment(CENTER)
                        .borderWidth(1)
                        .build())
                .add(TextCell.builder().text("Y")
                        .lineSpacing(1f)
                        .borderWidthTop(1)
                        .textColor(Color.BLACK)
                        .backgroundColor(Color.GRAY)
                        .fontSize(8)
                        .font(HELVETICA)
                        .verticalAlignment(VerticalAlignment.MIDDLE)
                        .horizontalAlignment(CENTER)
                        .borderWidth(1)
                        .build())
                .add(TextCell.builder().text("LLLLLLLLLL")
                        .borderWidthTop(1)
                        .backgroundColor(Color.GRAY)
                        .fontSize(8)
                        .font(HELVETICA)
                        .verticalAlignment(VerticalAlignment.MIDDLE)
                        .horizontalAlignment(CENTER)
                        .borderWidth(1)
                        .build())
                .horizontalAlignment(HorizontalAlignment.LEFT)
                .build());

        return tableBuilder.build();
    }

}
