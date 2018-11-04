package org.vandeseer.pdfbox.easytable.cell;

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

    @Builder.Default
    private Color borderColor;

    @Builder.Default
    private final int span = 1;

    @Builder.Default
    private final HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;

    @Builder.Default
    private final VerticalAlignment verticalAlignment = VerticalAlignment.MIDDLE;

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

    public float getHorizontalPadding() {
        return getPaddingLeft() + getPaddingRight();
    }

    public float getVerticalPadding() {
        return getPaddingTop() + getPaddingBottom();
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

    public boolean hasBackgroundColor() {
        return backgroundColor != null;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getBorderColor() {
        final Optional<Color> optBorderColor = Optional.ofNullable(borderColor);
        return optBorderColor.orElse(getRow().getBorderColor());
    }

    public abstract float getHeight();

    // This is used for customizations of the Lombok generated (Super)Builder
    public abstract static class CellBaseDataBuilder<C extends CellBaseData, B extends CellBaseData.CellBaseDataBuilder<C, B>> {

        public B allBorders(final float borderWidth) {
            return this.borderWidthTop(borderWidth).borderWidthBottom(borderWidth).borderWidthLeft(borderWidth).borderWidthRight(borderWidth);
        }

    }

}
