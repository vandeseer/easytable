package org.vandeseer.integrationtest;

import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.ArabicTextCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.io.IOException;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;

public class ArabicTextCellTest {

    @Test
    public void testArabicTextCell() throws IOException {
        TestUtils.createAndSaveDocumentWithTables("arabicTextCell.pdf",
                createSimpleTable()
        );
    }

    // Uses latin but illustrates how to use arabic fonts (also see #77)
    private static Table createSimpleTable() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 100)
                .fontSize(8)
                .font(HELVETICA);

        final String text = "The text is correctly displayed (with right align) " +
                "and correctly ordered within each line, but lines must be read from bottom to top";

        tableBuilder
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text(text).build())
                        .add(ArabicTextCell.builder().borderWidth(1).text(text).build())
                        .build());

        return tableBuilder.build();
    }

}
