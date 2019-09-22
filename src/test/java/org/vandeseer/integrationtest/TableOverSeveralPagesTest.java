package org.vandeseer.integrationtest;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.Test;
import org.vandeseer.easytable.RepeatedHeaderTableDrawer;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;
import java.io.IOException;

public class TableOverSeveralPagesTest {

    @Test
    public void createTwoPageTable() throws IOException {

        final PDDocument document = new PDDocument();

        TableDrawer.builder()
            .table(createTable())
            .startX(50)
            .startY(100F)
            .endY(50F) // note: if not set, table is drawn over the end of the page
            .build()
            .draw(() -> document, () -> new PDPage(PDRectangle.A4), 50f);

        document.save("target/severalPagesTable1.pdf");
        document.close();

    }

    @Test
    public void createTwoTwoPageTable() throws IOException {

        final PDDocument document = new PDDocument();

        TableDrawer.builder()
                .table(createTable())
                .startX(50)
                .startY(100F)
                .endY(50F) // note: if not set, table is drawn over the end of the page
                .build()
                .draw(() -> document, () -> new PDPage(PDRectangle.A4), 50f);

        TableDrawer.builder()
                .table(createTable())
                .startX(50)
                .startY(100F)
                .endY(50F) // note: if not set, table is drawn over the end of the page
                .build()
                .draw(() -> document, () -> new PDPage(PDRectangle.A4), 50f);

        document.save("target/severalPagesTable2.pdf");
        document.close();

    }

    @Test
    public void createTwoPageTableWithRepeatedHeader() throws IOException {

        final PDDocument document = new PDDocument();

        RepeatedHeaderTableDrawer.builder()
            .table(createTable())
            .startX(50)
            .startY(100F)
            .endY(50F) // note: if not set, table is drawn over the end of the page
            .build()
            .draw(() -> document, () -> new PDPage(PDRectangle.A4), 50f);

        document.save("target/severalPagesTableRepeatedHeader.pdf");
        document.close();

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

        TextCell dummyCell = TextCell.builder()
                .text("dummy")
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
                            .add(dummyCell)
                            .add(dummyCell)
                            .build());
        }

        return tableBuilder.build();
    }

}
