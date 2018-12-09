package org.vandeseer.easytable.structure.cell;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.Settings;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Column;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.TableNotYetBuiltException;

import java.awt.*;

@SuperBuilder(toBuilder = true)
@Getter
public abstract class CellBaseData {

    @Setter
    private Row row;

    @Setter
    private Column column;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    protected Settings settings;

    @Builder.Default
    private final int span = 1;

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
        return settings.getBackgroundColor() != null;
    }

    public Color getBackgroundColor() {
        return settings.getBackgroundColor();
    }

    public Color getBorderColor() {
        return settings.getBorderColor();
    }

    public boolean isWordBreak(){
        return  settings.isWordBreak();
    }

    public abstract float getHeight();

    void assertIsRendered() {
        if (column == null || row == null) {
            throw new TableNotYetBuiltException();
        }
    }

    // This is used for customizations of the Lombok generated (Super)Builder
    public abstract static class CellBaseDataBuilder<C extends CellBaseData, B extends CellBaseData.CellBaseDataBuilder<C, B>> {

        protected Settings settings = Settings.builder().build();

        public B borderWidth(final float borderWidth) {
            return this.borderWidthTop(borderWidth).borderWidthBottom(borderWidth).borderWidthLeft(borderWidth).borderWidthRight(borderWidth);
        }

        public B padding(final float padding) {
            return this.paddingTop(padding).paddingBottom(padding).paddingLeft(padding).paddingRight(padding);
        }

        public B horizontalAlignment(final HorizontalAlignment alignment) {
            settings.setHorizontalAlignment(alignment);
            return this.self();
        }

        public B verticalAlignment(final VerticalAlignment alignment) {
            settings.setVerticalAlignment(alignment);
            return this.self();
        }

        public B backgroundColor(final Color backgroundColor) {
            settings.setBackgroundColor(backgroundColor);
            return this.self();
        }

        public B borderColor(final Color borderColor) {
            settings.setBorderColor(borderColor);
            return this.self();
        }

        public B wordBreak(final Boolean wordBreak) {
            settings.setWordBreak(wordBreak);
            return this.self();
        }

    }

}
