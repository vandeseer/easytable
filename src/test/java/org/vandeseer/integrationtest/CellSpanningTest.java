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
import static org.vandeseer.easytable.settings.VerticalAlignment.BOTTOM;
import static org.vandeseer.easytable.settings.VerticalAlignment.MIDDLE;

public class CellSpanningTest {

    @Test
    public void createDocumentWithTables() throws Exception {
        TestUtils.createAndSaveDocumentWithTables("cellSpanning.pdf",
                createTableWithCellColSpanning(),
                createTableWithCellRowSpanning(),
                createTableWithTwoCellRowSpannings(),
                createTableWithSeveralRowSpannings(),
                createTableWithDifferentAlignmentsInSpannedCells(),
                createTableWithSeveralRowAndCellSpannings()
        );
    }

    private Table createTableWithCellColSpanning() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(300, 120, 70)
                .fontSize(8)
                .font(HELVETICA);


        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("Pur").colSpan(2).backgroundColor(Color.YELLOW).horizontalAlignment(CENTER).borderWidth(1).build())
                .add(CellText.builder().text("Booz").build())
                .backgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("Hey").horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text("Ho!").build())
                .add(CellText.builder().text("Fu.").backgroundColor(Color.ORANGE).build())
                .backgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("Bar").horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text("Baz").colSpan(2).backgroundColor(Color.CYAN).horizontalAlignment(CENTER).borderWidth(1).build())
                .backgroundColor(Color.GREEN)
                .build());

        return tableBuilder.build();
    }

    private Table createTableWithCellRowSpanning() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(300, 120, 70)
                .fontSize(8)
                .font(HELVETICA);


        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("Pur").backgroundColor(Color.YELLOW).horizontalAlignment(CENTER).borderWidth(1).build())
                .add(CellText.builder().text("Pur").backgroundColor(Color.YELLOW).horizontalAlignment(CENTER).borderWidth(1).build())
                .add(CellText.builder().text("Booz").build())
                .backgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("Hey").horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text("Fu.").rowSpan(2).backgroundColor(Color.ORANGE).borderWidth(1).build())
                .add(CellText.builder().text("Ho!").build())
                .backgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("Bar").horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text("Bar").horizontalAlignment(RIGHT).build())
                .backgroundColor(Color.GREEN)
                .build());

        return tableBuilder.build();
    }

    private Table createTableWithTwoCellRowSpannings() throws Exception {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(300, 120, 70)
                .fontSize(8)
                .font(HELVETICA);


        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("This is\nrow span 2").rowSpan(2).backgroundColor(Color.YELLOW).horizontalAlignment(CENTER).borderWidth(1).build())
                .add(CellText.builder().text("Two").backgroundColor(Color.YELLOW).horizontalAlignment(CENTER).borderWidth(1).build())
                .add(CellText.builder().text("Three").borderWidth(1).build())
                .backgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("This is\nrow span 3").rowSpan(3).backgroundColor(Color.ORANGE).borderWidth(1).build())
                .add(CellText.builder().text("Three").borderWidth(1).build())
                .backgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("One").horizontalAlignment(RIGHT).borderWidth(1).build())
                .add(CellText.builder().text("Three").horizontalAlignment(RIGHT).borderWidth(1).build())
                .backgroundColor(Color.GREEN)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("One").horizontalAlignment(RIGHT).borderWidth(1).build())
                .add(CellText.builder().text("Three").horizontalAlignment(RIGHT).borderWidth(1).build())
                .backgroundColor(Color.GREEN)
                .build());

        return tableBuilder.build();
    }

    private Table createTableWithSeveralRowSpannings() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(300, 120, 70)
                .fontSize(8)
                .font(HELVETICA);

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("This is\nrow span 4").rowSpan(4).borderWidth(1).verticalAlignment(MIDDLE).horizontalAlignment(CENTER).build())
                .add(CellText.builder().text("Pur").backgroundColor(Color.YELLOW).borderWidth(1).horizontalAlignment(CENTER).build())
                .add(CellText.builder().text("Booz").borderWidth(1).build())
                .backgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("This is\nrow span 3").rowSpan(3).borderWidth(1).backgroundColor(Color.ORANGE).build())
                .add(CellText.builder().text("Ho!").borderWidth(1).build())
                .backgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("Three").borderWidth(1).build())
                .backgroundColor(Color.GREEN)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("Three").borderWidth(1).build())
                .backgroundColor(Color.GREEN)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("One").borderWidth(1).horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text("Two").borderWidth(1).horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text("Three").borderWidth(1).horizontalAlignment(RIGHT).build())
                .backgroundColor(Color.CYAN)
                .build());

        return tableBuilder.build();
    }

    private Table createTableWithDifferentAlignmentsInSpannedCells() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(300, 120, 70)
                .fontSize(8)
                .font(HELVETICA);

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("This is\nrow span 4").rowSpan(4).borderWidth(1).verticalAlignment(MIDDLE).horizontalAlignment(CENTER).build())
                .add(CellText.builder().text("Pur").backgroundColor(Color.YELLOW).borderWidth(1).horizontalAlignment(CENTER).build())
                .add(CellText.builder().text("Booz").borderWidth(1).build())
                .backgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("This is\nrow span 3, bottom aligned").rowSpan(3).borderWidth(1)
                        .backgroundColor(Color.ORANGE).verticalAlignment(BOTTOM).paddingBottom(2).build())
                .add(CellText.builder().text("Ho!").borderWidth(1).build())
                .backgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("Three").borderWidth(1).build())
                .backgroundColor(Color.GREEN)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("Three").borderWidth(1).build())
                .backgroundColor(Color.GREEN)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("One").borderWidth(1).horizontalAlignment(RIGHT).build())
                .add(CellText.builder().text("Two").borderWidth(1).horizontalAlignment(CENTER).build())
                .add(CellText.builder().text("Three").borderWidth(1).horizontalAlignment(CENTER).build())
                .backgroundColor(Color.CYAN)
                .build());

        return tableBuilder.build();
    }

    private Table createTableWithSeveralRowAndCellSpannings() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(200, 120, 70, 100)
                .fontSize(8)
                .font(HELVETICA);

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().borderWidth(1).text("One").build())
                .add(CellText.builder().borderWidth(1).text("Two").build())
                .add(CellText.builder().borderWidth(1).text("Three").build())
                .add(CellText.builder().borderWidth(1).text("Four").build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().borderWidth(1).text("One").build())
                .add(CellText.builder().borderWidth(1).text("Spanning 2x2").colSpan(2).rowSpan(2).build())
                .add(CellText.builder().borderWidth(1).text("Four").build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().borderWidth(1).text("One").build())
                .add(CellText.builder().borderWidth(1).text("Four").build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().borderWidth(1).text("One").build())
                .add(CellText.builder().borderWidth(1).text("Two").build())
                .add(CellText.builder().borderWidth(1).text("Three").build())
                .add(CellText.builder().borderWidth(1).text("Four").build())
                .build());

        return tableBuilder.build();
    }

}
