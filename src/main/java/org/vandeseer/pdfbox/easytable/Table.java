package org.vandeseer.pdfbox.easytable;

import lombok.*;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.vandeseer.pdfbox.easytable.cell.CellBaseData;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@Builder(buildMethodName = "internalBuild")
@Getter
@Setter(AccessLevel.PRIVATE)
public class Table {

    private final List<Row> rows;
    private final List<Column> columns;

    @Builder.Default
    private PDFont font = PDType1Font.HELVETICA;

    @Builder.Default
    private int fontSize = 12;

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

    float getAvailableCellWidthRespectingSpan(int columnIndex, int span) {
        float cellWidth = 0;
        for (int i = 0; i < span; i++) {
            cellWidth += getColumns().get(columnIndex + i).getWidth();
        }
        return cellWidth;
    }

    public static class TableBuilder {

        private List<Row> rows = new LinkedList<>();
        private List<Column> columns = new LinkedList<>();

        public TableBuilder addRow(final Row row) {
            if (row.getCells().stream().mapToInt(CellBaseData::getSpan).sum() != numberOfColumns) {
                throw new IllegalArgumentException(
                        "Number of row cells does not match with number of table columns");
            }
            rows.add(row);
            return this;
        }

        public TableBuilder addColumnOfWidth(final int width) {
            addColumn(new Column(width));
            return this;
        }

        private TableBuilder addColumn(final Column column) {
            numberOfColumns++;
            columns.add(column);
            width += column.getWidth();
            return this;
        }

        public Table build() {
            Table table = this.internalBuild();

            table.setWidth(width);
            table.setNumberOfColumns(numberOfColumns);

            // Set up the connections between table, row(s) and cell(s)
            for (Row row : rows) {
                row.setTable(table);

                for (int i = 0; i < row.getCells().size(); i++) {
                    row.getCells().get(i).setRow(row);
                    row.getCells().get(i).setColumn(table.getColumns().get(i));
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
