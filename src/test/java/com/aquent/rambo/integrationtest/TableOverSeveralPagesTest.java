package com.aquent.rambo.integrationtest;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.Test;

import com.aquent.rambo.TestUtils;
import com.aquent.rambo.easytable.RepeatedHeaderTableDrawer;
import com.aquent.rambo.easytable.TableDrawer;
import com.aquent.rambo.easytable.structure.Row;
import com.aquent.rambo.easytable.structure.Table;
import com.aquent.rambo.easytable.structure.cell.TextCell;

import java.awt.*;
import java.io.IOException;

public class TableOverSeveralPagesTest {

    @Test
    public void drawMultipageTable() throws IOException {

        try (final PDDocument document = new PDDocument()) {
            drawMultipageTableOn(document);
            document.save(TestUtils.TARGET_FOLDER + "/severalPagesTable1.pdf");
        }

    }

    @Test
    public void drawSeveralMultipageTableOnSameDocument() throws IOException {

        try (final PDDocument document = new PDDocument()) {
            drawMultipageTableOn(document);
            drawMultipageTableOn(document);

            document.save(TestUtils.TARGET_FOLDER + "/severalPagesTable2.pdf");
        }

    }

    @Test
    public void createTwoPageTableWithRepeatedHeader() throws IOException {

        try (final PDDocument document = new PDDocument()) {

            RepeatedHeaderTableDrawer.builder()
                    .table(createTable())
                    .startX(50)
                    .startY(100F)
                    .endY(50F) // note: if not set, table is drawn over the end of the page
                    .build()
                    .draw(() -> document, () -> new PDPage(PDRectangle.A4), 50f);

            document.save(TestUtils.TARGET_FOLDER + "/severalPagesTableRepeatedHeader.pdf");
        }

    }

    private void drawMultipageTableOn(PDDocument document) throws IOException {
        TableDrawer.builder()
                .table(createTable())
                .startX(50)
                .startY(100F)
                .endY(50F) // note: if not set, table is drawn over the end of the page
                .build()
                .draw(() -> document, () -> new PDPage(PDRectangle.A4), 50f);
    }

    private Table createTable() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnOfWidth(200)
                .addColumnOfWidth(200);

        TextCell dummyHeaderCell = TextCell.builder()
                .text("Header dummy")
                .backgroundColor(Color.BLUE)
                .textColor(Color.WHITE)
                .borderWidth(1F)
                .build();

        tableBuilder.addRow(
                Row.builder()
                        .add(dummyHeaderCell)
                        .add(dummyHeaderCell)
                        .build());

        for (int i = 0; i < 50; i++) {
            tableBuilder.addRow(
                    Row.builder()
                            .add(TextCell.builder()
                                    .text("dummy " + i)
                                    .borderWidth(1F)
                                    .build())
                            .add(TextCell.builder()
                                    .text("dummy " + i)
                                    .borderWidth(1F)
                                    .build())
                            .build());
        }

        return tableBuilder.build();
    }

}
