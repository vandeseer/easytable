package org.vandeseer.pdfbox.easytable.Cell;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.pdfbox.easytable.PdfUtil;

import java.awt.*;
import java.util.Optional;

@Getter
@SuperBuilder
public class CellText extends CellBaseData {

    @NonNull
    private String text;

    private Color textColor;
    private PDFont font;
    private Integer fontSize;


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

        if (super.getRow().getTable().isWordBreak()) {
            final int size = PdfUtil.getOptimalTextBreak(text, getFont(), getFontSize(),
                    getColumn().getWidth() - getPaddingRight() - getPaddingRight()).size();
            textHeight = size * PdfUtil.getFontHeight(getFont(), getFontSize()) + size * 2f;
        } else {
            textHeight = PdfUtil.getFontHeight(getFont(), getFontSize());
        }

        return textHeight + getHeightWithoutFontSize();
    }


}
