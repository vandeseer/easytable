package org.vandeseer.easytable.structure.cell;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.FontSettings;
import org.vandeseer.easytable.structure.Column;
import org.vandeseer.easytable.util.PdfUtil;

import java.awt.*;
import java.util.Comparator;
import java.util.Optional;

@Getter
@SuperBuilder
public class CellText extends CellBaseData {

    @NonNull
    private String text;

    private Color textColor;

    private float lineSpacing = 1f;

    @Getter
    @Setter(AccessLevel.NONE)
    private FontSettings fontSettings;

    //region Custom Getter

    public PDFont getFont() {
        return fontSettings.getFont();
    }

    public Integer getFontSize() {
        return fontSettings.getFontSize();
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

    public abstract static class CellTextBuilder<C extends CellText, B extends CellText.CellTextBuilder<C, B>> extends CellBaseDataBuilder<C, B> {

        protected FontSettings fontSettings = FontSettings.builder().build();

        public B font(PDFont font) {
            fontSettings.setFont(font);
            return this.self();
        }

        public B fontSize(Integer fontSize) {
            fontSettings.setFontSize(fontSize);
            return this.self();
        }

    }

}
