package org.vandeseer.integrationtest;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.ExtendedTextCell;

import java.awt.*;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
import static org.vandeseer.TestUtils.getActualPdfFor;
import static org.vandeseer.TestUtils.getExpectedPdfFor;

public class ExtendedTextCellTest
{
    private static final String FILE_NAME = "ExtendedTextCell.pdf";

    @Test
    public void createDocumentWithExtendedCells() throws IOException
    {
        TestUtils.createAndSaveDocumentWithTables(FILE_NAME,
                createSuperExampleTable(8),
                createSubExampleTable(8),
                createSuperExampleTable(18),
                createSubExampleTable(18)
        );

        CompareResult compareResult = new PdfComparator<>(getExpectedPdfFor(FILE_NAME), getActualPdfFor(FILE_NAME)).compare();
        assertTrue(compareResult.isEqual());
    }

    private Table createSuperExampleTable(int fontSize)
    {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 100, 100, 100)
                .fontSize(fontSize)
                .font(HELVETICA)
                .borderColor(Color.BLACK)
                .borderWidth(1f);

        tableBuilder.addRow(Row.builder()
                .add(ExtendedTextCell.builder()
                        .text("Left Super^1")
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("Right Super^2")
                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("Center Super^3")
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("Just Super^4")
                        .horizontalAlignment(HorizontalAlignment.JUSTIFY)
                        .build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(ExtendedTextCell.builder()
                        .text("Left^1 Super")
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("Right^2 Super")
                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("Center^3 Super")
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("Just^4 Super")
                        .horizontalAlignment(HorizontalAlignment.JUSTIFY)
                        .build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(ExtendedTextCell.builder()
                        .text("^1Left Super")
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("^2Right Super")
                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("^3Center Super")
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("^4Just Super")
                        .horizontalAlignment(HorizontalAlignment.JUSTIFY)
                        .build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(ExtendedTextCell.builder()
                        .text("I'm Far Too Long Left Super^1")
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("I'm Far Too Long Right Super^2")
                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("I'm Far Too Long Center Super^3")
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("I'm Far Too Long Just Super^4")
                        .horizontalAlignment(HorizontalAlignment.JUSTIFY)
                        .build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(ExtendedTextCell.builder()
                        .text("I'm Far Too Long Left^1 Super")
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("I'm Far Too Long Right^2 Super")
                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("I'm Far Too Long Center^3 Super")
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("I'm Far Too Long Just^4 Super")
                        .horizontalAlignment(HorizontalAlignment.JUSTIFY)
                        .build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(ExtendedTextCell.builder()
                        .text("^1I'm Far Too Long Left Super")
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("^2I'm Far Too Long Right Super")
                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("^3I'm Far Too Long Center Super")
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("^4I'm Far Too Long Just Super")
                        .horizontalAlignment(HorizontalAlignment.JUSTIFY)
                        .build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(ExtendedTextCell.builder()
                        .text("^1")
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("^2")
                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("^3")
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("^4")
                        .horizontalAlignment(HorizontalAlignment.JUSTIFY)
                        .build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(ExtendedTextCell.builder()
                        .text("^1 ^2 ^3 ^4")
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("^5 ^6 ^7 ^8")
                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("^9 ^1^0 ^1^1 ^1^2")
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("^1^3 ^1^4 ^1^5 ^1^6")
                        .horizontalAlignment(HorizontalAlignment.JUSTIFY)
                        .build())
                .build());

        return (tableBuilder.build());
    }

    private Table createSubExampleTable(int fontSize)
    {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 100, 100, 100)
                .fontSize(fontSize)
                .font(HELVETICA)
                .borderColor(Color.BLACK)
                .borderWidth(1f);

        tableBuilder.addRow(Row.builder()
                .add(ExtendedTextCell.builder()
                        .text("Left Sub|1")
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("Right Sub|2")
                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("Center Sub|3")
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("Just Sub|4")
                        .horizontalAlignment(HorizontalAlignment.JUSTIFY)
                        .build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(ExtendedTextCell.builder()
                        .text("Left|1 Sub")
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("Right|2 Sub")
                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("Center|3 Sub")
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("Just|4 Sub")
                        .horizontalAlignment(HorizontalAlignment.JUSTIFY)
                        .build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(ExtendedTextCell.builder()
                        .text("|1Left Sub")
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("|2Right Sub")
                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("|3Center Sub")
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("|4Just Sub")
                        .horizontalAlignment(HorizontalAlignment.JUSTIFY)
                        .build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(ExtendedTextCell.builder()
                        .text("I'm Far Too Long Left Sub|1")
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("I'm Far Too Long Right Sub|2")
                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("I'm Far Too Long Center Sub|3")
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("I'm Far Too Long Just Sub|4")
                        .horizontalAlignment(HorizontalAlignment.JUSTIFY)
                        .build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(ExtendedTextCell.builder()
                        .text("I'm Far Too Long Left|1 Sub")
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("I'm Far Too Long Right|2 Sub")
                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("I'm Far Too Long Center|3 Sub")
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("I'm Far Too Long Just|4 Sub")
                        .horizontalAlignment(HorizontalAlignment.JUSTIFY)
                        .build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(ExtendedTextCell.builder()
                        .text("|1I'm Far Too Long Left Sub")
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("|2I'm Far Too Long Right Sub")
                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("|3I'm Far Too Long Center Sub")
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("|4I'm Far Too Long Just Sub")
                        .horizontalAlignment(HorizontalAlignment.JUSTIFY)
                        .build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(ExtendedTextCell.builder()
                        .text("|1")
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("|2")
                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("|3")
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("|4")
                        .horizontalAlignment(HorizontalAlignment.JUSTIFY)
                        .build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(ExtendedTextCell.builder()
                        .text("|1 |2 |3 |4")
                        .horizontalAlignment(HorizontalAlignment.LEFT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("|5 |6 |7 |8")
                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("|9 |1|0 |1|1 |1|2")
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .build())
                .add(ExtendedTextCell.builder()
                        .text("|1|3 |1|4 |1|5 |1|6")
                        .horizontalAlignment(HorizontalAlignment.JUSTIFY)
                        .build())
                .build());

        return (tableBuilder.build());
    }
}
