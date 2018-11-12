package org.vandeseer.easytable.structure.cell;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.structure.Column;
import org.vandeseer.easytable.util.PdfUtil;

import java.awt.*;
import java.util.Comparator;
import java.util.Optional;

@Getter
@SuperBuilder(toBuilder = true)
public class CellText extends CellBaseData {

    @NonNull
    private String text;

    private Color textColor;

    private float lineSpacing = 1f;

    //region Custom Getter

    public PDFont getFont() {
        return settings.getFont();
    }

    public Integer getFontSize() {
        return settings.getFontSize();
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
                    getWidthOfTextAndHorizontalPadding() - getPaddingRight() - getPaddingRight()).size();

            final float heightOfTextLines = size * fontHeight;
            final float heightOfLineSpacing = (size - 1) * fontHeight * getLineSpacing();

            textHeight = heightOfTextLines + heightOfLineSpacing;

        } else {
            textHeight = fontHeight;
        }

        return textHeight + getPaddingBottom() + getPaddingTop();
    }

    public float getWidthOfTextAndHorizontalPadding() {
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

    public abstract static class CellTextBuilder<C extends CellText, B extends CellText.CellTextBuilder<C, B>> extends CellBaseDataBuilder<C, B> {

        public B font(PDFont font) {
            settings.setFont(font);
            return this.self();
        }

        public B fontSize(Integer fontSize) {
            settings.setFontSize(fontSize);
            return this.self();
        }

    }

}
