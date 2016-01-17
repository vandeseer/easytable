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

    Cell cell1 = new Cell("11").setBackgroundColor(Color.YELLOW).setHorizontalAlignment(Cell.HorizontalAlignment.RIGHT);
    Cell cell2 = new Cell("12");
    tableBuilder.addRow(cell1, cell2);
    
    Cell cellSuper = new Cell("super").setHorizontalAlignment(Cell.HorizontalAlignment.RIGHT);
    Cell cellGeil = new Cell("geil").setBackgroundColor(Color.GRAY);
    tableBuilder.addRow(cellSuper, cellGeil);
    
    Cell cell31 = new Cell("ein etwas längerer text").setHorizontalAlignment(Cell.HorizontalAlignment.RIGHT);
    Cell cell32 = new Cell("ebenso hier! mit umläuten").setBackgroundColor(Color.GRAY);
    tableBuilder.addRow(cell31, cell32);
    
    Cell cell41 = new Cell("nummero 4").setHorizontalAlignment(Cell.HorizontalAlignment.RIGHT);
    Cell cell42 = new Cell("dieses ist die 4").setBackgroundColor(Color.GRAY);
    tableBuilder.addRow(cell41, cell42);
    
    Cell cell51 = new Cell("nummero 5");
    Cell cell52 = new Cell("dieses ist die 5").setBackgroundColor(Color.RED);
    tableBuilder.addRow(cell51, cell52);
    
    return tableBuilder.build();
  }

}
