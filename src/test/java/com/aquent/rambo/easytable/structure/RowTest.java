package com.aquent.rambo.easytable.structure;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.aquent.rambo.easytable.structure.Row;
import com.aquent.rambo.easytable.structure.Table;
import com.aquent.rambo.easytable.structure.TableNotYetBuiltException;
import com.aquent.rambo.easytable.structure.cell.TextCell;

import static com.aquent.rambo.easytable.settings.HorizontalAlignment.CENTER;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.COURIER_BOLD;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class RowTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getHeightShouldThrowExceptionIfNotYetRendered() {
        final Row row = Row.builder()
                .add(TextCell.builder().text("This text should break because too long").colSpan(2).borderWidth(1).build())
                .add(TextCell.builder().text("Booz").build())
                .wordBreak(true)
                .font(COURIER_BOLD).fontSize(8)
                .build();

        exception.expect(TableNotYetBuiltException.class);
        row.getHeight();
    }

    @Test
    public void getHeightShouldReturnValueIfTableIsBuilt() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(10, 10, 10)
                .horizontalAlignment(CENTER)
                .fontSize(10).font(HELVETICA)
                .wordBreak(false);

        final Row row = Row.builder()
                .add(TextCell.builder().text("iVgebALheQlBkxtDyNDrhKv").colSpan(2).borderWidth(1).build())
                .add(TextCell.builder().text("Booz").build())
                .font(COURIER_BOLD).fontSize(8)
                .build();

        tableBuilder.addRow(row);
        tableBuilder.build();

        assertThat(row.getHeight(), greaterThan(0f));
    }

}