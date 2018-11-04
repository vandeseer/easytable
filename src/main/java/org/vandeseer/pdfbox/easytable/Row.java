package org.vandeseer.pdfbox.easytable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.pdfbox.easytable.cell.CellBaseData;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;

@Builder
@Setter(AccessLevel.PRIVATE)
public class Row {

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private Table table;

    @Getter
    private List<CellBaseData> cells;

    private Color textColor;
    private Color borderColor;
    private Color backgroundColor;

    private PDFont font;
    private Integer fontSize;

    private Row(final List<CellBaseData> cells) {
        super();
        this.cells = cells;
    }

    public Color getTextColor() {
        return Optional.ofNullable(textColor).orElse(table.getTextColor());
    }

    public Color getBorderColor() {
        final Optional<Color> optBorderColor = Optional.ofNullable(borderColor);
        return optBorderColor.orElse(getTable().getBorderColor());
    }

    public Integer getFontSize() {
        return Optional.ofNullable(fontSize).orElse(table.getFontSize());
    }

    public PDFont getFont() {
        return Optional.ofNullable(font).orElse(table.getFont());
    }

    public float getHeight() {
        return getCells().stream().map(CellBaseData::getHeight).max(naturalOrder()).orElseThrow(RuntimeException::new);
    }


    public static class RowBuilder {
        private List<CellBaseData> cells = new LinkedList<>();

        private Optional<Color> borderColor = Optional.empty();

        public RowBuilder add(final CellBaseData cell) {
            cells.add(cell);
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

}
