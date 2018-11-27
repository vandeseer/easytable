package org.vandeseer.integrationtest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellText;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.COURIER_BOLD;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;

public class RowHeightTest {

    @Test
    public void testHeightBeforeAndAfterBuild() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 100, 100)
                .horizontalAlignment(CENTER)
                .fontSize(10).font(HELVETICA);

        Row row = Row.builder()
                .add(CellText.builder().text(RandomStringUtils.randomAlphabetic(23)).span(2).borderWidth(1).build())
                .add(CellText.builder().text("Booz").build())
                .font(COURIER_BOLD).fontSize(8)
                .build();

        tableBuilder.addRow(row);

        float heightBefore = row.getHeight();

        tableBuilder.build();

        float heightAfter = row.getHeight();

        assertThat(heightAfter, equalTo(heightBefore));
    }

}
