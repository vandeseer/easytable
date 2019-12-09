package org.vandeseer.easytable.structure.cell;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.cell.AbstractCellDrawer;
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

    public static final float DEFAULT_MIN_HEIGHT = 10f;

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

        public B borderWidth(final float borderWidth) {
            return this.borderWidthTop(borderWidth)
                    .borderWidthBottom(borderWidth)
                    .borderWidthLeft(borderWidth)
                    .borderWidthRight(borderWidth);
        }

        public B padding(final float padding) {
            return this.paddingTop(padding)
                    .paddingBottom(padding)
                    .paddingLeft(padding)
                    .paddingRight(padding);
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
