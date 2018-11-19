package org.vandeseer.integrationtest;

import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellText;

import java.awt.*;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;
import static org.vandeseer.easytable.settings.HorizontalAlignment.RIGHT;

public class WordWrappingTest {

    @Test
    public void createSampleDocumentWithWordWrapping_Issue20() throws Exception {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(8, 8, 8)
                .fontSize(12)
                .font(HELVETICA)
                .borderColor(Color.RED)
                .wordBreak(true);

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("a").borderWidth(1).padding(0f).horizontalAlignment(CENTER).build())
                .add(CellText.builder().text("B").borderWidth(1).padding(0f).build())
                .add(CellText.builder().text("#").borderWidth(1).padding(0f).build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("c").borderWidth(1).padding(0f).horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text("D").borderWidth(1).padding(0f).build())
                .add(CellText.builder().text("g").borderWidth(1).padding(0f).build())
                .build());

        TestUtils.createAndSaveDocumentWithTable(tableBuilder.build(), "wordWrapping_issue20.pdf");
    }

}
