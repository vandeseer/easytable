package org.vandeseer.pdfbox.easytable;

import java.awt.Color;
import java.awt.Font;

public class Cell {

  public enum HorizontalAlignment {
    LEFT, RIGHT
  }
  
  private HorizontalAlignment alignment = HorizontalAlignment.LEFT;
  private String text;
  private Color backgroundColor;
  private float paddingLeft = 3;
  private float paddingRight = 3;
  private float paddingTop = 3;
  private float paddingBottom = 3;

  public Cell(String textContent) {
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

  public Cell setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;
    return this;
  }

  public boolean hasText() {
    return text != null;
  }

  public float getPaddingLeft() {
    return paddingLeft;
  }

  public Cell setPaddingLeft(float paddingLeft) {
    this.paddingLeft = paddingLeft;
    return this;
  }

  public float getPaddingRight() {
    return paddingRight;
  }

  public Cell setPaddingRight(float paddingRight) {
    this.paddingRight = paddingRight;
    return this;
  }

  public float getPaddingTop() {
    return paddingTop;
  }

  public Cell setPaddingTop(float paddingTop) {
    this.paddingTop = paddingTop;
    return this;
  }

  public float getPaddingBottom() {
    return paddingBottom;
  }

  public Cell setPaddingBottom(float paddingBottom) {
    this.paddingBottom = paddingBottom;
    return this;
  }

  public HorizontalAlignment getHorizontalAlignment() {
    return alignment;
  }

  public Cell setHorizontalAlignment(HorizontalAlignment alignment) {
    this.alignment = alignment;
    return this;
  }

}
