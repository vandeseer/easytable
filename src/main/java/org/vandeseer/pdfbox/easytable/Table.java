package org.vandeseer.pdfbox.easytable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.vandeseer.pdfbox.easytable.cell.CellBaseData;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter(AccessLevel.PRIVATE)
public class Table {

    private final List<Row> rows;
    private final List<Column> columns;
    private PDFont font = PDType1Font.HELVETICA;
    private int fontSize = 12;
    private int numberOfColumns = 0;
    private float width = 0;
    private float borderWidth = 0.2f;
    private Color borderColor = Color.BLACK;
    private Color textColor = Color.BLACK;
    private boolean wordBreak = false;

    private Table(final List<Row> rows, final List<Column> columns) {
        this.rows = rows;
        this.columns = columns;
    }


    public float getHeight() {
        float height = 0;
        for (final Row row : rows) {
            height += (row.getHeight());
        }
        return height;
    }

    public static class TableBuilder {
        private final List<Row> rows = new LinkedList<>();
        private final List<Column> columns = new LinkedList<>();
        private int numberOfColumns = 0;
        private float width = 0;
        private final Table table = new Table(rows, columns);

        public static TableBuilder newBuilder() {
            return new TableBuilder();
        }

        public TableBuilder addRow(final Row row) {
            if (row.getCells().stream().mapToInt(CellBaseData::getSpan).sum() != numberOfColumns) {
                throw new IllegalArgumentException(
                        "Number of row cells does not match with number of table columns");
            }

            row.setTable(table);

            for (int i = 0; i < row.getCells().size(); i++) {
                row.getCells().get(i).setRow(row);
                row.getCells().get(i).setColumn(table.getColumns().get(i));
            }

            rows.add(row);
            return this;
        }

        public TableBuilder addColumnOfWidth(final int width) {
            addColumn(new Column(width));
            return this;
        }

        public TableBuilder addColumn(final Column column) {
            numberOfColumns++;
            columns.add(column);
            width += column.getWidth();
            return this;
        }

        public TableBuilder setFont(final PDFont font) {
            table.setFont(font);
            return this;
        }

        public TableBuilder setFontSize(final int fontSize) {
            table.setFontSize(fontSize);
            return this;
        }

        public TableBuilder setBorderWidth(final float borderWidth) {
            table.setBorderWidth(borderWidth);
            return this;
        }

        public TableBuilder setBorderColor(final Color color) {
            table.setBorderColor(color);
            return this;
        }

        public TableBuilder setTextColor(final Color color) {
            table.setTextColor(color);
            return this;
        }

        public TableBuilder setWordBreaking() {
            table.setWordBreak(true);
            return this;
        }

        public Table build() {
            table.setWidth(width);
            table.setNumberOfColumns(numberOfColumns);
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
