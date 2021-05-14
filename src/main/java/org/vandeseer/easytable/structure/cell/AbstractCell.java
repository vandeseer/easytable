package org.vandeseer.easytable.structure.cell;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.cell.AbstractCellDrawer;
import org.vandeseer.easytable.settings.BorderStyleInterface;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.Settings;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Column;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.TableNotYetBuiltException;

import java.awt.*;

@Getter
@SuperBuilder(toBuilder = true)
public abstract class AbstractCell {

    private static final float DEFAULT_MIN_HEIGHT = 10f;

    @Builder.Default
    private final int colSpan = 1;

    @Builder.Default
    private final int rowSpan = 1;

    @Setter
    protected AbstractCellDrawer drawer;

    @Setter
    private Row row;

    @Setter
    private Column column;

    @Setter
    private float width;

    @Builder.Default
    private float minHeight = DEFAULT_MIN_HEIGHT;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    protected Settings settings;

    public float getPaddingBottom() {
        return settings.getPaddingBottom();
    }

    public float getPaddingTop() {
        return settings.getPaddingTop();
    }

    public float getPaddingLeft() {
        return settings.getPaddingLeft();
    }

    public float getPaddingRight() {
        return settings.getPaddingRight();
    }

    public float getHorizontalPadding() {
        return settings.getPaddingLeft() + settings.getPaddingRight();
    }

    public float getVerticalPadding() {
        return settings.getPaddingTop() + settings.getPaddingBottom();
    }

    public float getBorderWidthTop() {
        return hasBorderTop()
                ? settings.getBorderWidthTop()
                : 0;
    }

    public boolean hasBorderTop() {
        return settings.getBorderWidthTop() != null && settings.getBorderWidthTop() > 0;
    }

    public float getBorderWidthBottom() {
        return hasBorderBottom()
                ? settings.getBorderWidthBottom()
                : 0;
    }

    public boolean hasBorderBottom() {
        return settings.getBorderWidthBottom() != null && settings.getBorderWidthBottom() > 0;
    }

    public float getBorderWidthLeft() {
        return hasBorderLeft()
                ? settings.getBorderWidthLeft()
                : 0;
    }

    public boolean hasBorderLeft() {
        return settings.getBorderWidthLeft() != null && settings.getBorderWidthLeft() > 0;
    }

    public float getBorderWidthRight() {
        return hasBorderRight()
                ? settings.getBorderWidthRight()
                : 0;
    }

    public boolean hasBorderRight() {
        return settings.getBorderWidthRight() != null && settings.getBorderWidthRight() > 0;
    }

    public Color getBorderColorTop() {
        return settings.getBorderColorTop();
    }

    public Color getBorderColorBottom() {
        return settings.getBorderColorBottom();
    }

    public Color getBorderColorLeft() {
        return settings.getBorderColorLeft();
    }

    public Color getBorderColorRight() {
        return settings.getBorderColorRight();
    }

    public BorderStyleInterface getBorderStyleTop() {
        return settings.getBorderStyleTop();
    }

    public BorderStyleInterface getBorderStyleBottom() {
        return settings.getBorderStyleBottom();
    }

    public BorderStyleInterface getBorderStyleLeft() {
        return settings.getBorderStyleLeft();
    }

    public BorderStyleInterface getBorderStyleRight() {
        return settings.getBorderStyleRight();
    }

    public boolean hasBackgroundColor() {
        return settings.getBackgroundColor() != null;
    }

    public Color getBackgroundColor() {
        return settings.getBackgroundColor();
    }

    public boolean isWordBreak() {
        return settings.isWordBreak();
    }

    public float getHeight() {
        assertIsRendered();

        return getRowSpan() > 1
                ? calculateHeightForRowSpan()
                : getMinHeight();
    }

    public Drawer getDrawer() {
        return this.drawer != null
                ? this.drawer.withCell(this)
                : createDefaultDrawer();
    }

    protected abstract Drawer createDefaultDrawer();

    public float calculateHeightForRowSpan() {
        Row currentRow = row;

        float result = currentRow.getHeight();
        for (int i = 1; i < getRowSpan(); i++) {
            result += currentRow.getNext().getHeight();
            currentRow = currentRow.getNext();
        }

        return result;
    }

    protected void assertIsRendered() {
        if (column == null || row == null) {
            throw new TableNotYetBuiltException();
        }
    }

    public boolean isHorizontallyAligned(HorizontalAlignment alignment) {
        return getSettings().getHorizontalAlignment() == alignment;
    }

    public boolean isVerticallyAligned(VerticalAlignment alignment) {
        return getSettings().getVerticalAlignment() == alignment;
    }

    // This is used for customizations of the Lombok generated (Super)Builder
    public abstract static class AbstractCellBuilder<C extends AbstractCell, B extends AbstractCell.AbstractCellBuilder<C, B>> {

        protected Settings settings = Settings.builder().build();

        // We don't want to expose settings directly!
        private void settings(Settings settings) {}

        public B borderWidth(final float borderWidth) {
            settings.setBorderWidthTop(borderWidth);
            settings.setBorderWidthBottom(borderWidth);
            settings.setBorderWidthLeft(borderWidth);
            settings.setBorderWidthRight(borderWidth);
            return this.self();
        }

        public B borderWidthTop(final float borderWidth) {
            settings.setBorderWidthTop(borderWidth);
            return this.self();
        }

        public B borderWidthBottom(final float borderWidth) {
            settings.setBorderWidthBottom(borderWidth);
            return this.self();
        }

        public B borderWidthLeft(final float borderWidth) {
            settings.setBorderWidthLeft(borderWidth);
            return this.self();
        }

        public B borderWidthRight(final float borderWidth) {
            settings.setBorderWidthRight(borderWidth);
            return this.self();
        }

        public B borderStyleTop(final BorderStyleInterface style) {
            settings.setBorderStyleTop(style);
            return this.self();
        }

        public B borderStyleBottom(final BorderStyleInterface style) {
            settings.setBorderStyleBottom(style);
            return this.self();
        }

        public B borderStyleLeft(final BorderStyleInterface style) {
            settings.setBorderStyleLeft(style);
            return this.self();
        }

        public B borderStyleRight(final BorderStyleInterface style) {
            settings.setBorderStyleRight(style);
            return this.self();
        }

        public B borderStyle(final BorderStyleInterface style) {
            return this.borderStyleLeft(style)
                    .borderStyleRight(style)
                    .borderStyleBottom(style)
                    .borderStyleTop(style);
        }

        public B padding(final float padding) {
            return this.paddingTop(padding)
                    .paddingBottom(padding)
                    .paddingLeft(padding)
                    .paddingRight(padding);
        }

        public B paddingTop(final float padding) {
            settings.setPaddingTop(padding);
            return this.self();
        }

        public B paddingBottom(final float padding) {
            settings.setPaddingBottom(padding);
            return this.self();
        }

        public B paddingLeft(final float padding) {
            settings.setPaddingLeft(padding);
            return this.self();
        }

        public B paddingRight(final float padding) {
            settings.setPaddingRight(padding);
            return this.self();
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

        public B borderColorTop(final Color borderColor) {
            settings.setBorderColorTop(borderColor);
            return this.self();
        }

        public B borderColorBottom(final Color borderColor) {
            settings.setBorderColorBottom(borderColor);
            return this.self();
        }

        public B borderColorLeft(final Color borderColor) {
            settings.setBorderColorLeft(borderColor);
            return this.self();
        }

        public B borderColorRight(final Color borderColor) {
            settings.setBorderColorRight(borderColor);
            return this.self();
        }

        public B borderColor(final Color borderColor) {
            this.borderColorBottom(borderColor)
                    .borderColorTop(borderColor)
                    .borderColorLeft(borderColor)
                    .borderColorRight(borderColor);
            return this.self();
        }

        public B wordBreak(final Boolean wordBreak) {
            settings.setWordBreak(wordBreak);
            return this.self();
        }

    }

}
