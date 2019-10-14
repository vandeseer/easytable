package org.vandeseer.easytable.structure.cell;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.cell.TextCellDrawer;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Column;
import org.vandeseer.easytable.util.PdfUtil;

import java.util.Comparator;
import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
public class TextCell extends AbstractTextCell {

    private Float textHeight;

    protected Drawer createDefaultDrawer() {
        return new TextCellDrawer(this);
    }

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

    public boolean isHorizontallyAligned(HorizontalAlignment alignment) {
        return getSettings().getHorizontalAlignment() == alignment;
    }

    public boolean isVerticallyAligned(VerticalAlignment middle) {
        return getSettings().getVerticalAlignment() == middle;
    }

}
