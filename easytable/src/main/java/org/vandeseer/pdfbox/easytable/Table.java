package org.vandeseer.pdfbox.easytable;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class Table {

  private List<Row> rows = new ArrayList<Row>();
  private List<Column> columns = new ArrayList<Column>();
  private PDFont font = PDType1Font.HELVETICA;
  private int fontSize = 12;
  private int numberOfColumns = 0;
  private float width = 0;
  private float borderWidth = 0.2f;

  public void addRow(Row row) {
    if (row.getCells().size() != numberOfColumns) {
      throw new IllegalArgumentException(
          "Number of row cells does not match with number of table columns");
    }
    rows.add(row);
  };

  public Table addColumn(Column column) {
    numberOfColumns++;
    columns.add(column);
    width += column.getWidth();
    return this;
  };

  public Table setFont(PDFont font) {
    this.font = font;
    return this;
  }

  public float getWidth() {
    return width;
  };

  public PDFont getFont() {
    return font;
  }

  public int getNumberOfColumns() {
    return numberOfColumns;
  }

  public int getFontSize() {
    return fontSize;
  }

  public Table setFontSize(int fontSize) {
    this.fontSize = fontSize;
    return this;
  }

  public List<Column> getColumns() {
    return columns;
  }

  public List<Row> getRows() {
    return rows;
  }

  public float getBorderWidth() {
    return borderWidth;
  }

  public float getRowHeight() {
    // TODO in later version we may not have equal sizes of every row!
    return rows.get(0) != null ? rows.get(0).getVerticalPadding() + fontSize : fontSize;
  }

  public float getFontHeight() {
    return font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
  }
  
  public float getHeight() {
    // TODO in later version we may not have equal sizes of every row!
    return rows.size() * getRowHeight(); 
  }

}
