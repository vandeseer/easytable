package org.vandeseer.easytable.structure;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.Settings;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.naturalOrder;

@Builder
@Getter
@Setter(AccessLevel.PACKAGE)
public class Row {

    private Table table;

    private List<AbstractCell> cells;

    @Setter(AccessLevel.NONE)
    private Settings settings;

    private float height;

    private Row next;


    private Row(final List<AbstractCell> cells) {
        super();
        this.cells = cells;
    }

    boolean hasNext() {
        return next != null;
    }

    public float getHeight() {
        if (table == null) {
            throw new TableNotYetBuiltException();
        }

        final float maxCellHeight = getCells().stream()
                .filter(cell -> cell.getRowSpan() == 1)
                .map(AbstractCell::getHeight)
                .max(naturalOrder())
                .orElseThrow(RuntimeException::new);

        return Math.max(height, maxCellHeight);
    }


    public static class RowBuilder {

        private List<AbstractCell> cells = new ArrayList<>();
        private Settings settings = Settings.builder().build();

        private RowBuilder() {

        }

        public RowBuilder add(final AbstractCell cell) {
            cells.add(cell);
            return this;
        }

        public Row.RowBuilder font(final PDFont font) {
            settings.setFont(font);
            return this;
        }

        public Row.RowBuilder fontSize(final Integer fontSize) {
            settings.setFontSize(fontSize);
            return this;
        }

        public Row.RowBuilder textColor(final Color textColor) {
            settings.setTextColor(textColor);
            return this;
        }

        public Row.RowBuilder backgroundColor(final Color backgroundColor) {
            settings.setBackgroundColor(backgroundColor);
            return this;
        }

        public Row.RowBuilder borderColor(final Color borderColor) {
            settings.setBorderColor(borderColor);
            return this;
        }

        public Row.RowBuilder horizontalAlignment(HorizontalAlignment alignment) {
            settings.setHorizontalAlignment(alignment);
            return this;
        }

        public Row.RowBuilder verticalAlignment(VerticalAlignment alignment) {
            settings.setVerticalAlignment(alignment);
            return this;
        }

        public Row.RowBuilder wordBreak(Boolean wordBreak) {
            settings.setWordBreak(wordBreak);
            return this;
        }

        public Row build() {
            final Row row = new Row(cells);
            row.settings = settings;
            row.height = height;

            return row;
        }

    }

}
