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

  public void setTable(final Table table) {
    this.table = table;
  }

  public List<Cell> getCells() {
    return this.cells;
  }

  public float getHeightWithoutFontHeight() {
    final Cell highestCell = cells
        .stream()
        .max((cell1, cell2) -> Float.compare(cell1.getHeightWithoutFontSize(), cell2.getHeightWithoutFontSize()))
        .get();
    return highestCell.getHeightWithoutFontSize();
  }

}
