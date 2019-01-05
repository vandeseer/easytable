package org.vandeseer.easytable.structure;

import lombok.*;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.Settings;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.cell.CellBaseData;

import java.awt.*;
import java.util.List;
import java.util.*;

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

    // TODO this data structure/handling is shit :D
    private final Set<Point> rowSpanCells;
    private final Map<Point, Integer> rowSpanCellsColSpanMap;

    @Getter
    @Setter(AccessLevel.NONE)
    private Settings settings;

    private int numberOfColumns;
    private float width;

    @Builder.Default
    private float borderWidth = 0.2f;

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

    public boolean isRowSpanAt(int colIndex,int rowIndex) {
        return rowSpanCells.contains(new Point(colIndex, rowIndex));
    }

    public int getRowSpanCellFor(int colIndex, int rowIndex) {
        return rowSpanCellsColSpanMap.get(new Point(colIndex, rowIndex));
    }

    public static class TableBuilder {

        private List<Row> rows = new LinkedList<>();
        private List<Column> columns = new LinkedList<>();

        private Settings settings = Settings.builder()
                                                .font(DEFAULT_FONT)
                                                .fontSize(DEFAULT_FONT_SIZE)
                                                .textColor(DEFAULT_TEXT_COLOR)
                                                .borderColor(DEFAULT_BORDER_COLOR)
                                                .wordBreak(true)
                                                .build();

        // TODO Simplify those structures! ... Point? Really? ;)
        private Map<Integer, Long> rowSpanMap = new HashMap<>();
        private Set<Point> rowSpanCells = new HashSet<>();
        private Map<Point, Integer> rowSpanCellsColSpanMap = new HashMap<>();

        private TableBuilder() {

        }

        public TableBuilder addRow(final Row row) {
            final List<CellBaseData> cells = row.getCells();

            // Store how many cells can or better have to be omitted in the next rows
            // due to cells in this row that declare row spanning
            updateRowSpanInformation(cells);

            // Check for the sum of col spans
            int colSpanSum = cells.stream().mapToInt(CellBaseData::getColSpan).sum();
            int differenceOfCells = numberOfColumns - colSpanSum;

            if (differenceOfCells != 0 && isDifferenceOfLastRowRowSpanning(differenceOfCells)) {
                throw new IllegalArgumentException("Row " + rows.size() + ": " +
                        "Number of row cells does not match with number of table columns or row spanning setup");
            }

            if (!rows.isEmpty()) {
                rows.get(rows.size() - 1).setNext(row);
            }
            rows.add(row);

            return this;
        }

        // TODO too complex :-/ Simplify!
        private void updateRowSpanInformation(List<CellBaseData> cells) {
            for (int i = 0; i < cells.size(); i++) {
                CellBaseData cell = cells.get(i);
                if (cell.getRowSpan() > 1) {
                    int k = 0;
                    while (k < cells.size() && rowSpanCells.contains(new Point(rows.size(), i + k))) {
                        k++;
                    }

                    for (int j = 0; j < cell.getRowSpan(); j++) {
                        if (j < cell.getRowSpan() - 1) {
                            long increment = cell.getColSpan() > 1 ? cell.getColSpan() : 1;
                            rowSpanMap.merge(rows.size() + j, increment, (key, value) -> value + increment);
                        }
                        if (j >= 1) {
                            Point point = new Point(rows.size() + j, i + k);
                            rowSpanCells.add(point);
                            rowSpanCellsColSpanMap.put(point, cell.getColSpan());
                        }
                    }
                }
            }
        }

        private boolean isDifferenceOfLastRowRowSpanning(int difference) {
            return difference != rowSpanMap.get(rows.size() - 1);
        }

        public TableBuilder addColumnsOfWidth(final float ...columnWidths) {
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
            Table table = this.internalBuild();

            // TODO check for row spanning issues!
            // ...

            table.setWidth(width);
            table.setNumberOfColumns(numberOfColumns);

            // Set up the connections between table, row(s) and cell(s)
            for (Row row : rows) {
                row.setTable(table);

                // Fill up the settings of the row that are not set there directly
                row.getSettings().fillingMergeBy(table.getSettings());

                int columnNumber = 0;
                for (CellBaseData cell : row.getCells()) {

                    // Fill up the settings of the cell that are not set there directly
                    cell.getSettings().fillingMergeBy(row.getSettings());

                    cell.setRow(row);
                    Column column = table.getColumns().get(columnNumber);
                    cell.setColumn(column);

                    columnNumber += cell.getColSpan();
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
