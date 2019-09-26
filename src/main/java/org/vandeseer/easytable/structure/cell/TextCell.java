package org.vandeseer.easytable.structure.cell;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.cell.TextCellDrawer;
import org.vandeseer.easytable.drawing.cell.VerticalTextCellDrawer;
import org.vandeseer.easytable.settings.Orientation;
import org.vandeseer.easytable.structure.Column;
import org.vandeseer.easytable.util.PdfUtil;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
public class TextCell extends AbstractCell {

    @NonNull
    protected String text;
    protected Color textColor;
    @Builder.Default
    protected float lineSpacing = 1f;
    @Builder.Default
    protected Orientation textOrientation = Orientation.HORIZONTAL;
    private Float textHeight;

    public PDFont getFont() {
        return settings.getFont();
    }

    public Integer getFontSize() {
        return settings.getFontSize();
    }

    public Color getTextColor() {
        return settings.getTextColor();
    }

    protected Drawer createDefaultDrawer() {
        return textOrientation == Orientation.HORIZONTAL
                ? new TextCellDrawer(this)
                : new VerticalTextCellDrawer(this);
    }

    @Override
    public float getMinHeight() {
        // For vertical alignment we can't simply calculate the min width, the users should
        // just set it themselves ;)
        if (textOrientation == Orientation.VERTICAL) {
            // But in order not to run into https://github.com/vandeseer/easytable/issues/20
            // we assume a min height that is the width of three chars (which are turned by 90 degrees).
            float widthOfThreeChars = (getFont().getAverageFontWidth() * getFontSize() / 1000F) * 3;
            return (getVerticalPadding() + widthOfThreeChars) > super.getMinHeight()
                    ? (getVerticalPadding() + widthOfThreeChars)
                    : super.getMinHeight();
        }

        // In case we have regular horizontal alignment ...
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

            final int size = PdfUtil.getOptimalTextBreakLines(text, getFont(), getFontSize(), getWidthOfText()).size();

            final float heightOfTextLines = size * this.textHeight;
            final float heightOfLineSpacing = (size - 1) * this.textHeight * getLineSpacing();

            this.textHeight = heightOfTextLines + heightOfLineSpacing;
        }

        return this.textHeight;
    }

    public float getWidthOfText() {
        assertIsRendered();

        final float notBrokenTextWidth = PdfUtil.getStringWidth(text, getFont(), getFontSize());

        if (settings.isWordBreak()) {

            final float maxWidth = getMaxWidthOfText() - getHorizontalPadding();
            List<String> textLines = PdfUtil.getOptimalTextBreakLines(text, getFont(), getFontSize(), maxWidth);

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

    // Adaption for Lombok
    public abstract static class TextCellBuilder<C extends TextCell, B extends TextCell.TextCellBuilder<C, B>> extends AbstractCellBuilder<C, B> {

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
