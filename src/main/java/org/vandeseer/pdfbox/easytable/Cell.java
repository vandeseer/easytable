package org.vandeseer.pdfbox.easytable;

import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.util.Optional;

public class Cell {

    public enum HorizontalAlignment {
        LEFT, CENTER, RIGHT
    }

    private Row row;

    private HorizontalAlignment alignment = HorizontalAlignment.LEFT;
    private final String text;
    private Color backgroundColor;
    private Color textColor;

    private PDFont font;
    private Integer fontSize;

    private float paddingLeft = 4;
    private float paddingRight = 4;
    private float paddingTop = 2;
    private float paddingBottom = 4;

    private float borderWidthTop = 0;
    private float borderWidthLeft = 0;
    private float borderWidthRight = 0;
    private float borderWidthBottom = 0;

    private Color borderColor;

    private int span = 1;

    private Cell(final Object object) {
        text = String.valueOf(object);
    }

    public static Cell withText(final Object object) {
        return new Cell(object);
    }

    public Cell span(final int span) {
        if (span <= 1) {
            throw new IllegalArgumentException("span has to be bigger than 1");
        }

        this.span = span;
        return this;
    }

    public int getSpan() {
        return span;
    }

    public Cell withAllBorders() {
        final int borderWith = 1;
        return withAllBorders(borderWith);
    }

    public Cell withAllBorders(final int borderWith) {
        return setBorderWidthBottom(borderWith)
                .setBorderWidthLeft(borderWith)
                .setBorderWidthRight(borderWith)
                .setBorderWidthTop(borderWith);
    }

    public Cell withFont(final PDFont font) {
        this.font = font;
        return this;
    }

    public Cell withFontSize(final int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public PDFont getFont() {
        return Optional.ofNullable(font).orElse(row.getFont());
    }

    public Integer getFontSize() {
        return Optional.ofNullable(fontSize).orElse(row.getFontSize());
    }

    public Row getRow() {
        return row;
    }

    public void setRow(final Row row) {
        this.row = row;
    }

    public String getText() {
        return text;
    }

    public boolean hasBackgroundColor() {
        return backgroundColor != null;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Cell setBackgroundColor(final Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public boolean hasText() {
        return text != null;
    }

    public float getPaddingLeft() {
        return paddingLeft;
    }

    public Cell setPaddingLeft(final float paddingLeft) {
        this.paddingLeft = paddingLeft;
        return this;
    }

    public float getPaddingRight() {
        return paddingRight;
    }

    public Cell setPaddingRight(final float paddingRight) {
        this.paddingRight = paddingRight;
        return this;
    }

    public float getPaddingTop() {
        return paddingTop;
    }

    public Cell setPaddingTop(final float paddingTop) {
        this.paddingTop = paddingTop;
        return this;
    }

    public float getPaddingBottom() {
        return paddingBottom;
    }

    public Cell setPaddingBottom(final float paddingBottom) {
        this.paddingBottom = paddingBottom;
        return this;
    }

    public HorizontalAlignment getHorizontalAlignment() {
        return alignment;
    }

    public Cell setHorizontalAlignment(final HorizontalAlignment alignment) {
        this.alignment = alignment;
        return this;
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

    public float getBorderWidthTop() {
        return borderWidthTop;
    }

    public Cell setBorderWidthTop(final float borderWidthTop) {
        this.borderWidthTop = borderWidthTop;
        return this;
    }

    public float getBorderWidthLeft() {
        return borderWidthLeft;
    }

    public Cell setBorderWidthLeft(final float borderWidthLeft) {
        this.borderWidthLeft = borderWidthLeft;
        return this;
    }

    public float getBorderWidthRight() {
        return borderWidthRight;
    }

    public Cell setBorderWidthRight(final float borderWidthRight) {
        this.borderWidthRight = borderWidthRight;
        return this;
    }

    public float getBorderWidthBottom() {
        return borderWidthBottom;
    }

    public Cell setBorderWidthBottom(final float borderWidthBottom) {
        this.borderWidthBottom = borderWidthBottom;
        return this;
    }

    public float getHeightWithoutFontSize() {
        return paddingBottom + paddingTop;
    }

    public Color getBorderColor() {
        final Optional<Color> optBorderColor = Optional.ofNullable(borderColor);
        return optBorderColor.orElse(getRow().getBorderColor());
    }

    public Cell setBorderColor(final Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public Color getParentBorderColor() {
        return getRow().getBorderColor();
    }

    public Color getTextColor() {
        return Optional.ofNullable(textColor).orElse(row.getTextColor());
    }

    public void setTextColor(final Color textColor) {
        this.textColor = textColor;
    }

    public Cell withTextColor(final Color color) {
        textColor = color;
        return this;
    }

    public float getWidthWithoutWordbreak() throws Exception {
        return PdfUtil.getStringWidth(text, getFont(), getFontSize());
    }


}
