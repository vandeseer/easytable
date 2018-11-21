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

    @Test
    public void createSampleDocumentWithWordWrapping_Issue20_2() throws Exception {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(70, 260, 70, 70)
                .fontSize(12)
                .font(HELVETICA)
                .borderColor(Color.WHITE)
                .wordBreak(true);

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().borderWidth(1).text("Stückzahl").build())
                .add(CellText.builder().borderWidth(1).horizontalAlignment(CENTER).text("Artikel Bez.").build())
                .add(CellText.builder().borderWidth(1).horizontalAlignment(CENTER).text("Einzelpreis").build())
                .add(CellText.builder().borderWidth(1).horizontalAlignment(CENTER).text("Gesamt").build())
                .backgroundColor(Color.LIGHT_GRAY)
                .build());

        for (int i = 0; i < 10; i++) {
            int price = 200;
            tableBuilder.addRow(Row.builder()
                    .add(CellText.builder().text(String.valueOf(i + 1)).horizontalAlignment(RIGHT).build())
                    .add(CellText.builder().text("Thermaltake Desktop Tenor VB 2000 SNS").build())
                    .add(CellText.builder().text(price + " €").horizontalAlignment(RIGHT).build())
                    .add(CellText.builder().text((i + 1) * price + " €").horizontalAlignment(RIGHT).build())
                    .build());
        }

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text(String.valueOf(3)).horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text("This breaks because it is too long Thermaltake Desktop Tenor VB 2000 SNS").build())
                .add(CellText.builder().text(120 + " €").horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text(3 * 120 + " €").horizontalAlignment(RIGHT).build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text(String.valueOf(3)).horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text("Fubar Article").build())
                .add(CellText.builder().text(234 + " €").horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text(3 * 234 + " €").horizontalAlignment(RIGHT).build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("This is a text that spans over four columns. Hooray! Also it breaks" +
                        "because it is too long. This is a text that spans over four columns. Hooray! " +
                        "Also it breaks.").span(4).build())
                .backgroundColor(Color.LIGHT_GRAY)
                .build());

        for (int i = 0; i < 5; i++) {
            int price = 429;
            tableBuilder.addRow(Row.builder()
                    .add(CellText.builder().text(String.valueOf(i + 1)).horizontalAlignment(RIGHT).build())
                    .add(CellText.builder().text("Some other fancy product").build())
                    .add(CellText.builder().text(price + " €").horizontalAlignment(RIGHT).build())
                    .add(CellText.builder().text((i + 1) * price + " €").horizontalAlignment(RIGHT).build())
                    .build());
        }

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("Gesamt Seite 1:").horizontalAlignment(RIGHT).span(3).borderWidth(1).build())
                .add(CellText.builder().text("1 600 €").horizontalAlignment(RIGHT).borderWidth(1).build())
                .backgroundColor(Color.LIGHT_GRAY)
                .build());

        TestUtils.createAndSaveDocumentWithTable(tableBuilder.build(), "wordWrapping_issue20_2.pdf");
    }

}
