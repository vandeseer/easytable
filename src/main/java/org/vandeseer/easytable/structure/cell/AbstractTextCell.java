package org.vandeseer.easytable.structure.cell;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.structure.Column;
import org.vandeseer.easytable.util.PdfUtil;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
public abstract class AbstractTextCell extends AbstractCell {

    protected Color textColor;

    @Builder.Default
    protected float lineSpacing = 1f;

    public PDFont getFont() {
        return settings.getFont();
    }

    public Integer getFontSize() {
        return settings.getFontSize();
    }

    public Color getTextColor() {
        return settings.getTextColor();
    }

    private Float textHeight;

    public abstract String getText();

    @Override
    public float getMinHeight() {
        return (getVerticalPadding() + getTextHeight()) > super.getMinHeight()
                ? (getVerticalPadding() + getTextHeight())
                : super.getMinHeight();
    }

    /**
     * Calculates the cell's text's height in respect when horizontally printed/rendered.
     *
     * @return the height of the cell's text taking into account spacing and line breaks
     */
    public float getTextHeight() {

        if (this.textHeight != null) {
            return this.textHeight;
        }

        this.textHeight = PdfUtil.getFontHeight(getFont(), getFontSize());

        if (settings.isWordBreak()) {

            final int size = PdfUtil.getOptimalTextBreakLines(getText(), getFont(), getFontSize(), getMaxWidth()).size();

            final float heightOfTextLines = size * this.textHeight;
            final float heightOfLineSpacing = (size - 1) * this.textHeight * getLineSpacing();

            this.textHeight = heightOfTextLines + heightOfLineSpacing;
        }

        return this.textHeight;
    }

    public float getWidthOfText() {
        assertIsRendered();

        final float notBrokenTextWidth = PdfUtil.getStringWidth(getText(), getFont(), getFontSize());

        if (settings.isWordBreak()) {

            final float maxWidth = getMaxWidthOfText() - getHorizontalPadding();
            List<String> textLines = PdfUtil.getOptimalTextBreakLines(getText(), getFont(), getFontSize(), maxWidth);

            return textLines.stream()
                    .map(line -> PdfUtil.getStringWidth(line, getFont(), getFontSize()))
                    .max(Comparator.naturalOrder())
                    .orElse(notBrokenTextWidth);

        }

        return notBrokenTextWidth;
    }

    private float getMaxWidthOfText() {
        float columnsWidth = getColumn().getWidth();

        // We have to take column spanning into account
        if (getColSpan() > 1) {
            Column currentColumn = getColumn();
            for (int i = 1; i < getColSpan(); i++) {
                columnsWidth += currentColumn.getNext().getWidth();
                currentColumn = currentColumn.getNext();
            }
        }
        return columnsWidth;
    }

    public float getMaxWidth() {
        return getMaxWidthOfText() - getHorizontalPadding();
    }

    // Adaption for Lombok
    public abstract static class AbstractTextCellBuilder<C extends AbstractTextCell, B extends AbstractTextCell.AbstractTextCellBuilder<C, B>> extends AbstractCellBuilder<C, B> {

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
