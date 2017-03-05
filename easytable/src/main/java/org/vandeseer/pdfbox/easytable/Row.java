package org.vandeseer.pdfbox.easytable;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
        final Optional<Cell> highestCell = cells
                .stream()
                .max((cell1, cell2) -> Float.compare(cell1.getHeightWithoutFontSize(), cell2.getHeightWithoutFontSize()));
        return highestCell.orElseThrow(IllegalStateException::new).getHeightWithoutFontSize();
    }

    public static class RowBuilder {
        private final List<Cell> cells = new LinkedList<>();
        private Optional<Color> backgroundColor = Optional.empty();

        public RowBuilder add(final Cell cell) {
            cells.add(cell);
            return this;
        }

        public RowBuilder setBackgroundColor(Color backgroundColor) {
            this.backgroundColor = Optional.ofNullable(backgroundColor);
            return this;
        }

        public Row build() {
            cells.stream().forEach(cell -> {
                if (!cell.hasBackgroundColor()) {
                    backgroundColor.ifPresent(cell::setBackgroundColor);
                }
            });
            return new Row(cells);
        }
    }

}
