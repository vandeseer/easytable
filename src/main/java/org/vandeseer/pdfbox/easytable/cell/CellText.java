package org.vandeseer.pdfbox.easytable.cell;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.pdfbox.easytable.Column;
import org.vandeseer.pdfbox.easytable.util.PdfUtil;

import java.awt.*;
import java.util.Comparator;
import java.util.Optional;

@Getter
@SuperBuilder
public class CellText extends CellBaseData {

    @NonNull
    private String text;

    private Color textColor;
    private PDFont font;
    private Integer fontSize;

    private float lineSpacing = 1f;

    //region Custom Getter

    public PDFont getFont() {
        return Optional.ofNullable(font).orElse(getRow().getFont());
    }

    public Integer getFontSize() {
        return Optional.ofNullable(fontSize).orElse(getRow().getFontSize());
    }

    public Color getTextColor() {
        return Optional.ofNullable(textColor).orElse(getRow().getTextColor());
    }

    //endregion


    @Override
    public float getHeight() {
        final float textHeight;
        final float fontHeight = PdfUtil.getFontHeight(getFont(), getFontSize());

        if (getRow().getTable().isWordBreak()) {

            final int size = PdfUtil.getOptimalTextBreak(text, getFont(), getFontSize(),
                    getWidth() - getPaddingRight() - getPaddingRight()).size();

            final float heightOfTextLines = size * fontHeight;
            final float heightOfLineSpacing = (size - 1) * fontHeight * getLineSpacing();

            textHeight = heightOfTextLines + heightOfLineSpacing;

        } else {
            textHeight = fontHeight;
        }

        return textHeight + getPaddingBottom() + getPaddingTop();
    }

    // TODO @Override? (Do we need that for image cell alignment for instance?)
    public float getWidth() {
        final float textWidth;

        if (getRow().getTable().isWordBreak()) {

            float columnsWidth = getColumn().getWidth();

            // We have to take column spanning into account
            if (getSpan() > 1) {
                Column currentColumn = getColumn();
                for (int i = 1; i < getSpan(); i++) {
                    columnsWidth += currentColumn.getNext().getWidth();
                    currentColumn = currentColumn.getNext();
                }
            }

            final float maxWidth = columnsWidth - getHorizontalPadding();
            textWidth = PdfUtil.getOptimalTextBreak(text, getFont(), getFontSize(), maxWidth)
                    .stream()
                    .map(line -> PdfUtil.getStringWidth(line, getFont(), getFontSize()))
                    .max(Comparator.naturalOrder())
                    .orElseThrow(RuntimeException::new);

        } else {
            textWidth = PdfUtil.getStringWidth(text, getFont(), getFontSize());
        }

        return textWidth + getHorizontalPadding();
    }

}
