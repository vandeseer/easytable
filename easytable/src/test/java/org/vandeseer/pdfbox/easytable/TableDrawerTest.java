package org.vandeseer.pdfbox.easytable;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Before;
import org.junit.Test;

public class TableDrawerTest {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void test() throws COSVisitorException, IOException {
    PDDocument document = new PDDocument();
    PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
    page.setRotation(90);

    document.addPage(page);

    // -------------------------
    Table table = new Table()
      .addColumn(new Column(200))
      .addColumn(new Column(400))
      .setFontSize(4)
      .setFont(PDType1Font.HELVETICA);

    List<Cell> cellsRow1 = new ArrayList<Cell>();
    cellsRow1.add(new Cell("11").setBackgroundColor(Color.YELLOW).setHorizontalAlignment(Cell.HorizontalAlignment.RIGHT));
    cellsRow1.add(new Cell("12"));
    table.addRow(new Row(cellsRow1));

    List<Cell> cellsRow2 = new ArrayList<Cell>();
    cellsRow2.add(new Cell("super").setHorizontalAlignment(Cell.HorizontalAlignment.RIGHT));
    cellsRow2.add(new Cell("geil").setBackgroundColor(Color.GRAY));
    table.addRow(new Row(cellsRow2));
    
    List<Cell> cellsRow3 = new ArrayList<Cell>();
    cellsRow3.add(new Cell("ein etwas längerer text").setHorizontalAlignment(Cell.HorizontalAlignment.RIGHT));
    cellsRow3.add(new Cell("ebenso hier! mit umläuten").setBackgroundColor(Color.GRAY));
    table.addRow(new Row(cellsRow3));
    
    List<Cell> cellsRow4 = new ArrayList<Cell>();
    cellsRow4.add(new Cell("nummero 4").setHorizontalAlignment(Cell.HorizontalAlignment.RIGHT));
    cellsRow4.add(new Cell("dieses ist die 4").setBackgroundColor(Color.GRAY));
    table.addRow(new Row(cellsRow4));
    
    List<Cell> cellsRow5 = new ArrayList<Cell>();
    cellsRow5.add(new Cell("nummero 5"));
    cellsRow5.add(new Cell("dieses ist die 5").setBackgroundColor(Color.RED));
    table.addRow(new Row(cellsRow5));
    
    PDPageContentStream contentStream = new PDPageContentStream(document, page);
    contentStream.concatenate2CTM(0, 1, -1, 0, page.findMediaBox().getWidth(), 0);
    float startY = page.findMediaBox().getWidth() - 30;
    (new TableDrawer(contentStream, table, 30, startY)).draw();
    // -------------------------

    document.save("target/stefans.pdf");
    document.close();
  }

}
