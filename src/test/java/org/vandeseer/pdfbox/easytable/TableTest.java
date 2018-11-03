package org.vandeseer.pdfbox.easytable;

import org.junit.Test;
import org.vandeseer.pdfbox.easytable.cell.CellText;
import org.vandeseer.pdfbox.easytable.Row.RowBuilder;
import org.vandeseer.pdfbox.easytable.Table.TableBuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TableTest {

    @Test
    public void getNumberOfColumns_tableBuilderWithThreeColumns() {
        final TableBuilder tableBuilder = new TableBuilder()
                .addColumn(new Column(12))
                .addColumn(new Column(34))
                .addColumn(new Column(56));
        final Table table = tableBuilder.build();

        assertThat(table.getNumberOfColumns(), equalTo(3));
    }

    @Test
    public void getWidth_tableBuilderWithTwoColumns() {
        final TableBuilder tableBuilder = new TableBuilder()
                .addColumn(new Column(20))
                .addColumn(new Column(40));
        final Table table = tableBuilder.build();

        assertThat(table.getWidth(), equalTo(60f));
    }

    @Test
    public void getRows_tableBuilderWithOneRow() {
        final TableBuilder tableBuilder = new TableBuilder();
        tableBuilder.addColumn(new Column(12))
                .addColumn(new Column(34));
        final Row row = new RowBuilder()
                .add(CellText.builder().text("11").build())
                .add(CellText.builder().text("12").build())
                .build();
        tableBuilder.addRow(row);
        final Table table = tableBuilder.build();

        assertThat(table.getRows().size(), equalTo(1));
    }

    @Test
    public void getHeight_twoRowsWithDifferentPaddings() {
        final TableBuilder tableBuilder = new TableBuilder();
        tableBuilder.addColumn(new Column(12))
                .addColumn(new Column(34));
        final Row row = new RowBuilder()
                .add(CellText.builder().text("11").paddingTop(35).paddingBottom(15).build())
                .add(CellText.builder().text("12").paddingTop(15).paddingBottom(25).build())
                .build();
        tableBuilder.addRow(row);
        final Table table = tableBuilder
                .setFontSize(12) // this will have a font height withText 13.872
                .build();

        // highest cell (60) + font height
        assertThat(table.getHeight(), equalTo(58.616f));
    }

}
