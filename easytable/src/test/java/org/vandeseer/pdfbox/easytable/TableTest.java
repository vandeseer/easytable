package org.vandeseer.pdfbox.easytable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TableTest {

  private Table table;

  @Before
  public void setUp() throws Exception {
    table = new Table()
    .addColumn(new Column(20))
    .addColumn(new Column(40));
  }

  @Test
  public void testAddTwoColumnsNoFontSpecifics() {
    assertThat(table.getNumberOfColumns(), equalTo(2));
    assertThat(table.getWidth(), equalTo(60f));
  }
  
  @Test
  public void testFullTable() {
    List<Cell> cells = new ArrayList<Cell>();
    cells.add(new Cell("11"));
    cells.add(new Cell("12"));
    table.addRow(new Row(cells));
    
    assertThat(table.getRows().size(), equalTo(1));
  }

}
