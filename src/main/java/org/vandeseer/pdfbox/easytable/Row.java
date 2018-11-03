package org.vandeseer.pdfbox.easytable;

import lombok.Getter;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.pdfbox.easytable.cell.CellBaseData;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;

public class Row {

    private Table table;
    private final List<CellBaseData> cells;
    private Color borderColor;
    private Color textColor;

    private PDFont font;
    private Integer fontSize;

    private Row(final List<CellBaseData> cells) {
        super();
        this.cells = cells;
    }

    public Table getTable() {
        return table;
    }

    void setTable(final Table table) {
        this.table = table;
    }

    public List<CellBaseData> getCells() {
        return cells;
    }

    public Color getTextColor() {
        return Optional.ofNullable(textColor).orElse(table.getTextColor());
    }

    private void setTextColor(final Color textColor) {
        this.textColor = textColor;
    }


    public Color getBorderColor() {
        final Optional<Color> optBorderColor = Optional.ofNullable(borderColor);
        return optBorderColor.orElse(getTable().getBorderColor());
    }

    private void setBorderColor(final Color borderColor) {
        this.borderColor = borderColor;
    }

    public Integer getFontSize() {
        return Optional.ofNullable(fontSize).orElse(table.getFontSize());
    }

    private void setFontSize(final Integer fontSize) {
        this.fontSize = fontSize;
    }

    public PDFont getFont() {
        return Optional.ofNullable(font).orElse(table.getFont());
    }

    private void setFont(final PDFont font) {
        this.font = font;
    }

    public static class RowBuilder {
        private final List<CellBaseData> cells = new LinkedList<>();

        @Getter
        private Color backgroundColor;
        private Optional<Color> borderColor = Optional.empty();
        private Color textColor;
        private PDFont font;
        private Integer fontSize;

        public static RowBuilder newBuilder() {
            return new RowBuilder();
        }

        public RowBuilder add(final CellBaseData cell) {
            cells.add(cell);
            return this;
        }

        public RowBuilder setBackgroundColor(final Color backgroundColor) {
            this.backgroundColor = backgroundColor;
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
            this.fontSize = fontSize;
            return this;
        }

        public RowBuilder withTextColor(final Color color) {
            textColor = color;
            return this;
        }

        public Row build() {
            Optional.ofNullable(backgroundColor).ifPresent(rowBackColor -> cells.stream()
                    .filter(cell -> !cell.hasBackgroundColor())
                    .forEach(cell -> cell.setBackgroundColor(rowBackColor)));

            final Row row = new Row(cells);
            borderColor.ifPresent(row::setBorderColor);
            Optional.ofNullable(textColor).ifPresent(row::setTextColor);
            Optional.ofNullable(font).ifPresent(row::setFont);
            row.setFontSize(fontSize);
            return row;
        }

    }

    public float getHeight() {
        return getCells().stream().map(CellBaseData::getHeight).max(naturalOrder()).orElseThrow(RuntimeException::new);
    }

}
