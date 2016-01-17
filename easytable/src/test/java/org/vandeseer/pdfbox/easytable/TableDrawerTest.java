package org.vandeseer.pdfbox.easytable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.pdfbox.easytable.Table.TableBuilder;

public class TableDrawerTest {

  @Test
  public void createDocument() throws Exception {
    final PDDocument document = new PDDocument();
    final PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
    page.setRotation(90);
    document.addPage(page);
    final PDPageContentStream contentStream = new PDPageContentStream(document, page);
    contentStream.concatenate2CTM(0, 1, -1, 0, page.findMediaBox().getWidth(), 0);
    final float startY = page.findMediaBox().getWidth() - 30;
    
    (new TableDrawer(contentStream, getTable1(), 30, startY)).draw();
    contentStream.close();

    document.save("target/stefans.pdf");
    document.close();
  }


  public Table getTable1() {
    final TableBuilder tableBuilder = new TableBuilder()
      .addColumn(new Column(200))
      .addColumn(new Column(400))
      .setFontSize(4)
      .setFont(PDType1Font.HELVETICA);

    final List<Cell> cellsRow1 = new ArrayList<>();
    cellsRow1.add(new Cell("11").setBackgroundColor(Color.YELLOW).setHorizontalAlignment(Cell.HorizontalAlignment.RIGHT));
    cellsRow1.add(new Cell("12"));
    tableBuilder.addRow(new Row(cellsRow1));
    
    final List<Cell> cellsRow2 = new ArrayList<>();
    cellsRow2.add(new Cell("super").setHorizontalAlignment(Cell.HorizontalAlignment.RIGHT));
    cellsRow2.add(new Cell("geil").setBackgroundColor(Color.GRAY));
    tableBuilder.addRow(new Row(cellsRow2));
    
    final List<Cell> cellsRow3 = new ArrayList<>();
    cellsRow3.add(new Cell("ein etwas längerer text").setHorizontalAlignment(Cell.HorizontalAlignment.RIGHT));
    cellsRow3.add(new Cell("ebenso hier! mit umläuten").setBackgroundColor(Color.GRAY));
    tableBuilder.addRow(new Row(cellsRow3));
    
    final List<Cell> cellsRow4 = new ArrayList<>();
    cellsRow4.add(new Cell("nummero 4").setHorizontalAlignment(Cell.HorizontalAlignment.RIGHT));
    cellsRow4.add(new Cell("dieses ist die 4").setBackgroundColor(Color.GRAY));
    tableBuilder.addRow(new Row(cellsRow4));
    
    final List<Cell> cellsRow5 = new ArrayList<>();
    cellsRow5.add(new Cell("nummero 5"));
    cellsRow5.add(new Cell("dieses ist die 5").setBackgroundColor(Color.RED));
    tableBuilder.addRow(new Row(cellsRow5));
    
    return tableBuilder.build();
  }

}
