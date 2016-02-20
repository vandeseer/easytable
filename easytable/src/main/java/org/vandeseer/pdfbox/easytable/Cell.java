package org.vandeseer.pdfbox.easytable;

import java.awt.Color;

public class Cell {

  public enum HorizontalAlignment {
    LEFT, RIGHT
  }
  
  private Row row;
  
  private HorizontalAlignment alignment = HorizontalAlignment.LEFT;
  private final String text;
  private Color backgroundColor;
  
  private float paddingLeft = 4;
  private float paddingRight = 4;
  private float paddingTop = 2;
  private float paddingBottom = 4;
  
  private float borderWidthTop = 0;
  private float borderWidthLeft= 0;
  private float borderWidthRight = 0;
  private float borderWidthBottom = 0;
  
  public Cell(final String textContent) {
    this.text = textContent;
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

  public Cell setBorderWidthTop(float borderWidthTop) {
    this.borderWidthTop = borderWidthTop;
    return this;
  }

  public float getBorderWidthLeft() {
    return borderWidthLeft;
  }

  public Cell setBorderWidthLeft(float borderWidthLeft) {
    this.borderWidthLeft = borderWidthLeft;
    return this;
  }

  public float getBorderWidthRight() {
    return borderWidthRight;
  }

  public Cell setBorderWidthRight(float borderWidthRight) {
    this.borderWidthRight = borderWidthRight;
    return this;
  }

  public float getBorderWidthBottom() {
    return borderWidthBottom;
  }

  public Cell setBorderWidthBottom(float borderWidthBottom) {
    this.borderWidthBottom = borderWidthBottom;
    return this;
  }

  public float getHeightWithoutFontSize() {
    return this.paddingBottom + this.paddingTop;
  }

}
