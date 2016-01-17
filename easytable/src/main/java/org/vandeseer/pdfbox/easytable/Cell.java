package org.vandeseer.pdfbox.easytable;

import java.awt.Color;

public class Cell {

  public enum HorizontalAlignment {
    LEFT, RIGHT
  }
  
  private HorizontalAlignment alignment = HorizontalAlignment.LEFT;
  private final String text;
  private Color backgroundColor;
  private float paddingLeft = 3;
  private float paddingRight = 3;
  private float paddingTop = 3;
  private float paddingBottom = 3;

  public Cell(final String textContent) {
    super();
    this.text = textContent;
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

}
