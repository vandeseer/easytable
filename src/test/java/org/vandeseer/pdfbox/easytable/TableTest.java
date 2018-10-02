package org.vandeseer.pdfbox.easytable;

import org.junit.Test;
import org.vandeseer.pdfbox.easytable.Row.RowBuilder;
import org.vandeseer.pdfbox.easytable.Table.TableBuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TableTest {

    @Test
    public void getNumberOfColumns_tableBuilderWithThreeColumns() {
        TableBuilder tableBuilder = new TableBuilder()
                .addColumn(new Column(12))
                .addColumn(new Column(34))
                .addColumn(new Column(56));
        Table table = tableBuilder.build();

        assertThat(table.getNumberOfColumns(), equalTo(3));
    }

    @Test
    public void getWidth_tableBuilderWithTwoColumns() {
        TableBuilder tableBuilder = new TableBuilder()
                .addColumn(new Column(20))
                .addColumn(new Column(40));
        Table table = tableBuilder.build();

        assertThat(table.getWidth(), equalTo(60f));
    }

    @Test
    public void getRows_tableBuilderWithOneRow() {
        TableBuilder tableBuilder = new TableBuilder();
        tableBuilder.addColumn(new Column(12))
                    .addColumn(new Column(34));
        Row row = new RowBuilder()
                .add(Cell.withText("11"))
                .add(Cell.withText("12")).build();
        tableBuilder.addRow(row);
        Table table = tableBuilder.build();

        assertThat(table.getRows().size(), equalTo(1));
    }

    @Test
    public void getHeight_twoRowsWithDifferentPaddings() {
        TableBuilder tableBuilder = new TableBuilder();
        tableBuilder.addColumn(new Column(12))
                    .addColumn(new Column(34));
        Row row = new RowBuilder()
                .add(Cell.withText("11").setPaddingTop(35).setPaddingBottom(15))
                .add(Cell.withText("12").setPaddingTop(15).setPaddingBottom(25)).build();
        tableBuilder.addRow(row);
        Table table = tableBuilder
                .setFontSize(12) // this will have a font height withText 13.872
                .build();

        // highest cell (60) + font height
        assertThat(table.getHeight(), equalTo(63.872f));
    }

}
