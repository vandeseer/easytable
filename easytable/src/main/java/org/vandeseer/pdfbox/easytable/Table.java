package org.vandeseer.pdfbox.easytable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class Table {

  private List<Row> rows = new ArrayList<>();
  private List<Column> columns = new ArrayList<>();
  private PDFont font = PDType1Font.HELVETICA;
  private int fontSize = 12;
  private int numberOfColumns = 0;
  private float width = 0;
  private float borderWidth = 0.2f;
  
  private Table(final List<Row> rows, final List<Column> columns) {
    this.rows = rows;
    this.columns = columns;
  };
 
  private void setFont(final PDFont font) {
    this.font = font;
  }
  
  private void setFontSize(final int fontSize) {
    this.fontSize = fontSize;
  }
  
  private void setNumberOfColumns(final int numberOfColumns) {
    this.numberOfColumns = numberOfColumns;
  }
  
  private void setWidth(final float width) {
    this.width = width;
  }
  
  private void setBorderWidth(final float borderWidth) {
    this.borderWidth = borderWidth;
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
  
  
  public static class TableBuilder {
    private final List<Row> rows = new ArrayList<Row>();
    private final List<Column> columns = new ArrayList<Column>();
    private int numberOfColumns = 0;
    private float width = 0;
    private Table table = new Table(rows, columns);
    
    public TableBuilder addRow(Cell... cells) {
      if (cells.length != numberOfColumns) {
        throw new IllegalArgumentException(
            "Number of row cells does not match with number of table columns");
      }
      Row row = new Row(Arrays.asList(cells));
      row.setTable(table);
      rows.add(row);
      return this;
    };
    
    public TableBuilder addColumn(final Column column) {
      numberOfColumns++;
      columns.add(column);
      width += column.getWidth();
      return this;
    };

    public TableBuilder setFont(final PDFont font) {
      this.table.setFont(font);
      return this;
    }
    
    public TableBuilder setFontSize(final int fontSize) {
      this.table.setFontSize(fontSize);
      return this;
    }
    
    public void setBorderWidth(final float borderWidth) {
      this.table.setBorderWidth(borderWidth);
    }
    
    public Table build() {
      table.setWidth(width);
      table.setNumberOfColumns(numberOfColumns);
      return table;
    }
    
  }

}
