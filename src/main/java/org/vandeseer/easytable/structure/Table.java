package org.vandeseer.easytable.structure;

import lombok.*;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.Settings;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.cell.CellBaseData;

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
    private static final Color DEFAULT_TEXT_COLOR = Color.BLACK;

    private static final HorizontalAlignment DEFAULT_HORIZONTAL_ALIGNMENT = HorizontalAlignment.LEFT;
    private static final VerticalAlignment DEFAULT_VERTICAL_ALIGNMENT = VerticalAlignment.MIDDLE;

    private final List<Row> rows;
    private final List<Column> columns;

    @Getter
    @Setter(AccessLevel.NONE)
    private Settings settings;

    private int numberOfColumns;
    private float width;

    @Builder.Default
    private float borderWidth = 0.2f;

    @Builder.Default
    private Color borderColor = Color.BLACK;

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
            cellWidth += getColumns().get(columnIndex + i).getWidth(); // TODO should we expand?
        }
        return cellWidth;
    }

    public static class TableBuilder {

        private List<Row> rows = new LinkedList<>();
        private List<Column> columns = new LinkedList<>();

        private Settings settings = Settings.builder()
                                                .font(DEFAULT_FONT)
                                                .fontSize(DEFAULT_FONT_SIZE)
                                                .textColor(DEFAULT_TEXT_COLOR)
                                                .build();

        private TableBuilder() {

        }

        public TableBuilder addRow(final Row row) {
            if (row.getCells().stream().mapToInt(CellBaseData::getSpan).sum() != numberOfColumns) {
                throw new IllegalArgumentException(
                        "Number of row cells does not match with number of table columns");
            }
            rows.add(row);
            return this;
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

        public TableBuilder horizontalAlignment(HorizontalAlignment alignment) {
            settings.setHorizontalAlignment(alignment);
            return this;
        }

        public TableBuilder verticalAlignment(VerticalAlignment alignment) {
            settings.setVerticalAlignment(alignment);
            return this;
        }

        public Table build() {
            Table table = this.internalBuild();

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

                    columnNumber += cell.getSpan();
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
