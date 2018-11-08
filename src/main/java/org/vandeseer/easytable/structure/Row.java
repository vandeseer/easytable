package org.vandeseer.easytable.structure;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.FontSettings;
import org.vandeseer.easytable.structure.cell.CellBaseData;

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

    @Getter
    @Setter(AccessLevel.NONE)
    private FontSettings fontSettings;

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

    public float getHeight() {
        return getCells().stream().map(CellBaseData::getHeight).max(naturalOrder()).orElseThrow(RuntimeException::new);
    }


    public static class RowBuilder {

        private List<CellBaseData> cells = new LinkedList<>();
        private Optional<Color> borderColor = Optional.empty();
        private FontSettings fontSettings = FontSettings.builder().build();

        public RowBuilder add(final CellBaseData cell) {
            cells.add(cell);
            return this;
        }

        public Row.RowBuilder font(final PDFont font) {
            fontSettings.setFont(font);
            return this;
        }

        public Row.RowBuilder fontSize(final Integer fontSize) {
            fontSettings.setFontSize(fontSize);
            return this;
        }

        public Row build() {
            Optional.ofNullable(backgroundColor).ifPresent(rowBackColor -> cells.stream()
                    .filter(cell -> !cell.hasBackgroundColor())
                    .forEach(cell -> cell.setBackgroundColor(rowBackColor)));

            final Row row = new Row(cells);
            row.fontSettings = fontSettings;

            borderColor.ifPresent(row::setBorderColor);
            Optional.ofNullable(textColor).ifPresent(row::setTextColor);

            return row;
        }

    }

}
