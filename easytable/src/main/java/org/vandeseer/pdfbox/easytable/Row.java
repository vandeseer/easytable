package org.vandeseer.pdfbox.easytable;

import java.util.ArrayList;
import java.util.List;

public class Row {

    private Table table;
    private final List<Cell> cells;

    private Row(final List<Cell> cells) {
        super();
        this.cells = cells;
        for (final Cell cell : cells) {
            cell.setRow(this);
        }
    }

    public Table getTable() {
        return table;
    }

    public void setTable(final Table table) {
        this.table = table;
    }

    public List<Cell> getCells() {
        return this.cells;
    }

    float getHeightWithoutFontHeight() {
        final Cell highestCell = cells
                .stream()
                .max((cell1, cell2) -> Float.compare(cell1.getHeightWithoutFontSize(), cell2.getHeightWithoutFontSize()))
                .get();
        return highestCell.getHeightWithoutFontSize();
    }

    public static class RowBuilder {
        private List<Cell> cells = new ArrayList<>();

        public RowBuilder add(final Cell cell) {
            cells.add(cell);
            return this;
        }

        public Row build() {
            return new Row(cells);
        }
    }

}
