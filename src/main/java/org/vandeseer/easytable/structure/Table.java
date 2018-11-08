package org.vandeseer.easytable.structure;

import lombok.*;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.vandeseer.easytable.settings.FontSettings;
import org.vandeseer.easytable.structure.cell.CellBaseData;
import org.vandeseer.easytable.structure.cell.CellText;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@Builder(buildMethodName = "internalBuild")
@Getter
@Setter(AccessLevel.PRIVATE)
public class Table {

    private static final PDFont DEFAULT_FONT = PDType1Font.HELVETICA;
    private static final int DEFAULT_FONT_SIZE = 12;

    private final List<Row> rows;
    private final List<Column> columns;

    @Getter
    @Setter(AccessLevel.NONE)
    private FontSettings fontSettings;

    private int numberOfColumns = 0;
    private float width = 0;

    @Builder.Default
    private float borderWidth = 0.2f;

    @Builder.Default
    private Color borderColor = Color.BLACK;

    @Builder.Default
    private Color textColor = Color.BLACK;

    @Builder.Default
    private boolean wordBreak = false;


    public float getHeight() {
        float height = 0;
        for (final Row row : rows) {
            height += (row.getHeight());
        }
        return height;
    }

    public float getAvailableCellWidthRespectingSpan(int columnIndex, int span) {
        float cellWidth = 0;
        for (int i = 0; i < span; i++) {
            cellWidth += getColumns().get(columnIndex + i).getWidth();
        }
        return cellWidth;
    }

    public static class TableBuilder {

        private List<Row> rows = new LinkedList<>();
        private List<Column> columns = new LinkedList<>();

        private FontSettings fontSettings = FontSettings.builder()
                                                .font(DEFAULT_FONT)
                                                .fontSize(DEFAULT_FONT_SIZE)
                                                .build();

        public TableBuilder addRow(final Row row) {
            if (row.getCells().stream().mapToInt(CellBaseData::getSpan).sum() != numberOfColumns) {
                throw new IllegalArgumentException(
                        "Number of row cells does not match with number of table columns");
            }
            rows.add(row);
            return this;
        }

        public TableBuilder addColumnOfWidth(final int width) {
            final Column column = new Column(width);
            numberOfColumns++;
            columns.add(column);
            this.width += column.getWidth();
            return this;
        }

        public TableBuilder font(final PDFont font) {
            fontSettings.setFont(font);
            return this;
        }

        public TableBuilder fontSize(final Integer fontSize) {
            fontSettings.setFontSize(fontSize);
            return this;
        }

        public Table build() {
            Table table = this.internalBuild();

            table.setWidth(width);
            table.setNumberOfColumns(numberOfColumns);

            // Set up the connections between table, row(s) and cell(s)
            for (Row row : rows) {
                row.setTable(table);
                row.getFontSettings().fillingMergeBy(table.getFontSettings());

                for (int i = 0; i < row.getCells().size(); i++) {
                    CellBaseData cell = row.getCells().get(i);

                    cell.setRow(row);
                    cell.setColumn(table.getColumns().get(i));

                    if (cell instanceof CellText) {
                        ((CellText) cell).getFontSettings().fillingMergeBy(row.getFontSettings());
                    }
                }
            }

            // Set up the column connections (table connection, next column connection in case of spanning)
            for (int i = 0; i < table.getColumns().size(); i++) {
                final Column column = table.getColumns().get(i);
                column.setTable(table);
                if (i < table.getColumns().size() - 1) {
                    column.setNext(table.getColumns().get(i + 1));
                }
            }

            return table;
        }

    }

}
