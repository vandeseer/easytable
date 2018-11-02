package org.vandeseer.pdfbox.easytable.Cell;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.vandeseer.pdfbox.easytable.Column;
import org.vandeseer.pdfbox.easytable.Row;

import java.awt.*;
import java.util.Optional;

@SuperBuilder
@Getter
public abstract class CellBaseData {

    @Setter
    private Row row;

    @Setter
    private Column column;

    @Setter
    private Color backgroundColor;
    private Color borderColor;

    @Builder.Default
    private final int span = 1;

    @Builder.Default
    private final HorizontalAlignment alignment = HorizontalAlignment.LEFT;

    @Builder.Default
    private final float paddingLeft = 4;

    @Builder.Default
    private final float paddingRight = 4;

    @Builder.Default
    private final float paddingTop = 4;

    @Builder.Default
    private final float paddingBottom = 4;

    @Builder.Default
    private float borderWidthTop = 0;

    @Builder.Default
    private float borderWidthLeft = 0;

    @Builder.Default
    private float borderWidthRight = 0;

    @Builder.Default
    private float borderWidthBottom = 0;


    public CellBaseData withAllBorders(final float borderWith) {
        borderWidthBottom = borderWith;
        borderWidthLeft = borderWith;
        borderWidthRight = borderWith;
        borderWidthTop = borderWith;
        return this;
    }

    public CellBaseData withAllBorders() {
        final int borderWith = 1;
        return withAllBorders(borderWith);
    }


    public boolean hasBorderTop() {
        return getBorderWidthTop() > 0;
    }

    public boolean hasBorderBottom() {
        return getBorderWidthBottom() > 0;
    }

    public boolean hasBorderLeft() {
        return getBorderWidthLeft() > 0;
    }

    public boolean hasBorderRight() {
        return getBorderWidthRight() > 0;
    }

    public float getHeightWithoutFontSize() {
        return getPaddingBottom() + getPaddingTop();
    }

    public boolean hasBackgroundColor() {
        return backgroundColor != null;
    }

    public Color getBackgroundColor() {
        // TODO Get Row Color
        return Optional.ofNullable(backgroundColor).orElse(null);
    }

    public Color getBorderColor() {
        final Optional<Color> optBorderColor = Optional.ofNullable(borderColor);
        return optBorderColor.orElse(getRow().getBorderColor());
    }

    public abstract float getHeight();

}
