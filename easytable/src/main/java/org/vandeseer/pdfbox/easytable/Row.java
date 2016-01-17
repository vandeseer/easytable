package org.vandeseer.pdfbox.easytable;

import java.util.ArrayList;
import java.util.List;

public class Row {

  private Table table;
  private List<Cell> cells = new ArrayList<Cell>();

  public Row(final List<Cell> cells) {
    super();
    this.cells = cells;
    for (final Cell cell : cells) {
      cell.setRow(this);
    }
  }

  public Table getTable() {
    return table;
  }

  public void setTable(Table table) {
    this.table = table;
  }

  public List<Cell> getCells() {
    return this.cells;
  }

  public float getVerticalPadding() {
    // we silently assume that each cell has the same padding!
    return cells.get(0).getPaddingTop() + cells.get(0).getPaddingBottom();
  }

}
