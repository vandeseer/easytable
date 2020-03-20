package org.vandeseer.easytable.structure;

import lombok.*;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.Settings;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Builder(buildMethodName = "internalBuild")
@Getter
@Setter(AccessLevel.PRIVATE)
public class Table {

    private static final PDFont DEFAULT_FONT = PDType1Font.HELVETICA;
    private static final int DEFAULT_FONT_SIZE = 12;
    private static final Color DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final Color DEFAULT_BORDER_COLOR = Color.BLACK;

    private static final HorizontalAlignment DEFAULT_HORIZONTAL_ALIGNMENT = HorizontalAlignment.LEFT;
    private static final VerticalAlignment DEFAULT_VERTICAL_ALIGNMENT = VerticalAlignment.MIDDLE;

    private final List<Row> rows;
    private final List<Column> columns;

    private final Set<Point> rowSpanCells;

    @Getter
    @Setter(AccessLevel.NONE)
    private Settings settings;

    private int numberOfColumns;
    private float width;

    @Builder.Default
    private float borderWidth = 0.2f;

    public float getHeight() {
        return rows.stream()
                .map(Row::getHeight)
                .reduce(0F, Float::sum);
    }

    public boolean isRowSpanAt(int rowIndex, int columnIndex) {
        return rowSpanCells.contains(new Point(rowIndex, columnIndex));
    }

    public static class TableBuilder {

        private List<Row> rows = new ArrayList<>();
        private List<Column> columns = new ArrayList<>();

        private Settings settings = Settings.builder()
                .font(DEFAULT_FONT)
                .fontSize(DEFAULT_FONT_SIZE)
                .textColor(DEFAULT_TEXT_COLOR)
                .borderColor(DEFAULT_BORDER_COLOR)
                .wordBreak(true)
                .build();

        private Set<Point> rowSpanCells = new HashSet<>();

        private TableBuilder() {

        }

        public TableBuilder addRow(final Row row) {
            // Store how many cells can or better have to be omitted in the next rows
            // due to cells in this row that declare row spanning
            updateRowSpanCellsSet(row.getCells());

            if (!rows.isEmpty()) {
                rows.get(rows.size() - 1).setNext(row);
            }
            rows.add(row);

            return this;
        }

        private float getAvailableCellWidthRespectingSpan(int columnIndex, int span) {
            float cellWidth = 0;
            for (int i = 0; i < span; i++) {
                cellWidth += columns.get(columnIndex + i).getWidth();
            }
            return cellWidth;
        }

        // This method is unfortunately a bit complex, but what it does is basically:
        // Put every cell coordinate in the set which needs to be skipped because it is
        // "contained" in another cell due to row spanning.
        // The coordinates are those of the table how it would look like without any spanning.
        private void updateRowSpanCellsSet(List<AbstractCell> cells) {
            int currentColumn = 0;

            for (AbstractCell cell : cells) {

                while (rowSpanCells.contains(new Point(rows.size(), currentColumn))) {
                    currentColumn++;
                }

                if (cell.getRowSpan() > 1) {

                    for (int rowsToSpan = 0; rowsToSpan < cell.getRowSpan(); rowsToSpan++) {

                        // Skip first row's cell, because that is a regular cell
                        if (rowsToSpan >= 1) {
                            for (int colSpan = 0; colSpan < cell.getColSpan(); colSpan++) {
                                rowSpanCells.add(new Point(rows.size() + rowsToSpan, currentColumn + colSpan));
                            }
                        }
                    }
                }

                currentColumn += cell.getColSpan();
            }
        }

        public TableBuilder addColumnsOfWidth(final float... columnWidths) {
            for (float columnWidth : columnWidths) {
                addColumnOfWidth(columnWidth);
            }
            return this;
        }

        public TableBuilder addColumnOfWidth(final float width) {
            final Column column = new Column(width);
            numberOfColumns++;
            columns.add(column);
            this.width += column.getWidth();
            return this;
        }

        public TableBuilder font(final PDFont font) {
            settings.setFont(font);
            return this;
        }

        public TableBuilder fontSize(final Integer fontSize) {
            settings.setFontSize(fontSize);
            return this;
        }

        public TableBuilder textColor(final Color textColor) {
            settings.setTextColor(textColor);
            return this;
        }

        public TableBuilder backgroundColor(final Color backgroundColor) {
            settings.setBackgroundColor(backgroundColor);
            return this;
        }

        public TableBuilder borderColor(final Color borderColor) {
            settings.setBorderColor(borderColor);
            return this;
        }

        public TableBuilder horizontalAlignment(HorizontalAlignment alignment) {
            settings.setHorizontalAlignment(alignment);
            return this;
        }

        public TableBuilder verticalAlignment(VerticalAlignment alignment) {
            settings.setVerticalAlignment(alignment);
            return this;
        }

        public TableBuilder wordBreak(Boolean wordBreak) {
            settings.setWordBreak(wordBreak);
            return this;
        }

        public Table build() {
            if (getNumberOfRegularCells() != getNumberOfSpannedCells()) {
                throw new TableSetupException("Number of table cells does not match with table setup. " +
                        "This could be due to row or col spanning not being correct.");
            }

            Table table = this.internalBuild();

            table.setWidth(width);
            table.setNumberOfColumns(numberOfColumns);

            // Set up the connections between table, row(s) and cell(s)
            for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
                Row row = rows.get(rowIndex);
                row.setTable(table);

                // Fill up the settings of the row that are not set there directly
                row.getSettings().fillingMergeBy(table.getSettings());

                int columnIndex = 0;
                for (AbstractCell cell : row.getCells()) {

                    // Fill up the settings of the cell that are not set there directly
                    cell.getSettings().fillingMergeBy(row.getSettings());

                    cell.setRow(row);

                    // We need to take into account row spanning ...
                    while (table.isRowSpanAt(rowIndex, columnIndex)) {
                        columnIndex++;
                    }
                    
                    if(columnIndex == table.getColumns().size()) {
                    	columnIndex = table.getColumns().size() - 1;
                    }

                    Column column = table.getColumns().get(columnIndex);
                    cell.setColumn(column);

                    cell.setWidth(getAvailableCellWidthRespectingSpan(columnIndex, cell.getColSpan()));

                    columnIndex += cell.getColSpan();
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

        private int getNumberOfRegularCells() {
            return columns.size() * rows.size();
        }

        private int getNumberOfSpannedCells() {
            return rows.stream()
                    .flatMapToInt(row -> row.getCells().stream().mapToInt(cell -> cell.getRowSpan() * cell.getColSpan()))
                    .sum();
        }

        private class TableSetupException extends RuntimeException {
            TableSetupException(String message) {
                super(message);
            }
        }
    }

}
