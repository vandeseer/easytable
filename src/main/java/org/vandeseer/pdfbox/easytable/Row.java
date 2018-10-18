package org.vandeseer.pdfbox.easytable;

import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;

public class Row {

    private Table table;
    private final List<Cell> cells;
    private Color borderColor;
    private Color textColor;

    private PDFont font;
    private Optional<Integer> fontSize = Optional.empty();

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

    void setTable(final Table table) {
        this.table = table;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public Color getTextColor() {
        return Optional.ofNullable(textColor).orElse(table.getTextColor());
    }

    private void setTextColor(final Color textColor) {
        this.textColor = textColor;
    }

    // TODO refactor! ;)
    int getFontHeight() {
        final Optional<Integer> cellWithMaxFontHeight = cells.stream()
                .map(Cell::getFontSize)
                .map(cellFontSize -> table.getFontSize())
                .max(naturalOrder());

        final Integer maxCellHeight = cellWithMaxFontHeight.orElseThrow(IllegalStateException::new);
        return fontSize.map(integer -> Math.max(maxCellHeight, integer)).orElse(maxCellHeight);
    }

    float getHeightWithoutFontHeight() {
        final Optional<Cell> highestCell = cells
                .stream() // TODO Can't we replace this with Comparator.comparing?
                .max((cell1, cell2) -> Float.compare(cell1.getHeightWithoutFontSize(), cell2.getHeightWithoutFontSize()));
        return highestCell.orElseThrow(IllegalStateException::new).getHeightWithoutFontSize();
    }

    public Color getBorderColor() {
        final Optional<Color> optBorderColor = Optional.ofNullable(borderColor);
        return optBorderColor.orElse(getTable().getBorderColor());
    }

    private void setBorderColor(final Color borderColor) {
        this.borderColor = borderColor;
    }

    public Integer getFontSize() {
        return fontSize.orElse(table.getFontSize());
    }

    private void setFontSize(final Optional<Integer> fontSize) {
        this.fontSize = fontSize;
    }

    public PDFont getFont() {
        return Optional.ofNullable(font).orElse(table.getFont());
    }

    private void setFont(final PDFont font) {
        this.font = font;
    }

    public static class RowBuilder {
        private final List<Cell> cells = new LinkedList<>();
        private Optional<Color> backgroundColor = Optional.empty();
        private Optional<Color> borderColor = Optional.empty();
        private Color textColor;
        private PDFont font;
        private Optional<Integer> fontSize = Optional.empty();

        public static RowBuilder newBuilder() {
            return new RowBuilder();
        }

        public RowBuilder add(final Cell cell) {
            cells.add(cell);
            return this;
        }

        public RowBuilder setBackgroundColor(final Color backgroundColor) {
            this.backgroundColor = Optional.ofNullable(backgroundColor);
            return this;
        }

        public RowBuilder setBorderColor(final Color borderColor) {
            this.borderColor = Optional.of(borderColor);
            return this;
        }

        public RowBuilder setFont(final PDFont font) {
            this.font = font;
            return this;
        }

        public RowBuilder setFontSize(final int fontSize) {
            this.fontSize = Optional.of(fontSize);
            return this;
        }

        public RowBuilder withTextColor(final Color color) {
            textColor = color;
            return this;
        }

        public Row build() {
            cells.forEach(cell -> {
                if (!cell.hasBackgroundColor()) {
                    backgroundColor.ifPresent(cell::setBackgroundColor);
                }
            });
            final Row row = new Row(cells);
            borderColor.ifPresent(row::setBorderColor);
            Optional.ofNullable(textColor).ifPresent(row::setTextColor);
            Optional.ofNullable(font).ifPresent(row::setFont);
            row.setFontSize(fontSize);
            return row;
        }

    }

}
