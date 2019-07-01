package org.vandeseer.easytable.structure.cell;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.drawing.CellTextDrawer;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.structure.Column;
import org.vandeseer.easytable.util.PdfUtil;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
public class CellText extends AbstractCell {

    @NonNull
    private String text;

    private Color textColor;

    @Builder.Default
    private float lineSpacing = 1f;

    //region Custom Getter

    public PDFont getFont() {
        return settings.getFont();
    }

    public Integer getFontSize() {
        return settings.getFontSize();
    }

    public Color getTextColor() {
        return settings.getTextColor();
    }

    //endregion

    @Override
    protected Drawer createDefaultDrawer() {
        return new CellTextDrawer(this);
    }

    @Override
    public float getHeight() {
        assertIsRendered();

        return getRowSpan() > 1
                ? calculateHeightForRowSpan()
                : getTextHeight() + getVerticalPadding();
    }

    public float getTextHeight() {
        final float fontHeight = PdfUtil.getFontHeight(getFont(), getFontSize());

        if (settings.isWordBreak()) {

            final int size = PdfUtil.getOptimalTextBreakLines(text, getFont(), getFontSize(), getWidthOfText()).size();

            final float heightOfTextLines = size * fontHeight;
            final float heightOfLineSpacing = (size - 1) * fontHeight * getLineSpacing();

            return heightOfTextLines + heightOfLineSpacing;

        }

        return fontHeight;
    }

    public float getWidthOfText() {
        assertIsRendered();

        final float notBrokenTextWidth = PdfUtil.getStringWidth(text, getFont(), getFontSize());

        if (settings.isWordBreak()) {

            float columnsWidth = getColumn().getWidth();

            // We have to take column spanning into account
            if (getColSpan() > 1) {
                Column currentColumn = getColumn();
                for (int i = 1; i < getColSpan(); i++) {
                    columnsWidth += currentColumn.getNext().getWidth();
                    currentColumn = currentColumn.getNext();
                }
            }

            final float maxWidth = columnsWidth - getHorizontalPadding();
            List<String> textLines = PdfUtil.getOptimalTextBreakLines(text, getFont(), getFontSize(), maxWidth);

            return textLines.stream()
                    .map(line -> PdfUtil.getStringWidth(line, getFont(), getFontSize()))
                    .max(Comparator.naturalOrder())
                    .orElse(notBrokenTextWidth);

        } else {
            return notBrokenTextWidth;
        }

    }

    public abstract static class CellTextBuilder<C extends CellText, B extends CellText.CellTextBuilder<C, B>> extends AbstractCellBuilder<C, B> {

        public B font(final PDFont font) {
            settings.setFont(font);
            return this.self();
        }

        public B fontSize(final Integer fontSize) {
            settings.setFontSize(fontSize);
            return this.self();
        }

        public B textColor(final Color textColor) {
            settings.setTextColor(textColor);
            return this.self();
        }

    }

}
