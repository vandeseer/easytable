package org.vandeseer.pdfbox.easytable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.vandeseer.pdfbox.easytable.Table.TableBuilder;

public class TableTest {

  private TableBuilder tableBuilder;

  @Before
  public void setUp() throws Exception {
    tableBuilder = new TableBuilder()
    .addColumn(new Column(20))
    .addColumn(new Column(40));
  }

  @Test
  public void testAddTwoColumnsNoFontSpecifics() {
    Table table = tableBuilder.build();
    assertThat(table.getNumberOfColumns(), equalTo(2));
    assertThat(table.getWidth(), equalTo(60f));
  }
  
  @Test
  public void testFullTable() {
    tableBuilder.addRow(new Cell("11"), new Cell("12"));
    assertThat(tableBuilder.build().getRows().size(), equalTo(1));
  }

}
