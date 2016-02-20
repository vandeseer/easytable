package org.vandeseer.pdfbox.easytable;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.junit.Test;
import org.vandeseer.pdfbox.easytable.Row.RowBuilder;
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
    
    (new TableDrawer(contentStream, getTable(), 30, startY)).draw();
    contentStream.close();

    document.save("target/stefans.pdf");
    document.close();
  }

  @Test
  public void createRingManagerDocument() throws Exception {
    final PDDocument document = new PDDocument();
    final PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
    document.addPage(page);
    
    final float startY = page.findMediaBox().getHeight() - 150;
    final int startX = 56;

    // We have to draw the image first according to:
    // http://stackoverflow.com/questions/8521290/cant-add-an-image-to-a-pdf-using-pdfbox
    // Can we also draw svg/eps?
    InputStream in = new FileInputStream(new File("/home/stefan/tmp/rmlogo.jpg"));
    PDJpeg img = new PDJpeg(document, in);
    
    
    final PDPageContentStream contentStream = new PDPageContentStream(document, page);
    contentStream.drawImage(img, startX, startY);
    
    // printText
    Table table = getRingManagerTable();
    drawHeading(contentStream, startX, startY, table.getWidth());
    
    (new TableDrawer(contentStream, table, startX, startY)).draw();
    
    contentStream.setFont(PDType1Font.HELVETICA, 8.0f);
    contentStream.beginText();
    // Is table.getHeight() correct?
    System.out.println(table.getHeight());
    contentStream.moveTextPositionByAmount(startX, startY - (table.getHeight() + 22));
    contentStream.drawString("Dieser Kampf muss der WB nicht entsprechen, da als Sparringskampf angesetzt.");
    contentStream.endText();
    
    contentStream.close();

    document.save("target/ringmanager.pdf");
    document.close();
  }
  
  private void drawHeading(PDPageContentStream contentStream, float startX, float startY, float width) throws IOException {
    // drawLogo
    
    // drawLine
    contentStream.drawLine(startX, startY, startX + width, startY);
    
    // Wiegen -- Arzt -- Beginn
    // drawLine
    // zentriert: ring-manager.com
    // drawLine
    
    // Fett Kampfansetzungen
    // Für wichtige Hinweise siehe Ende der Kampfansetzungen
    
  }

  private Table getTable() {
    return new TableBuilder()
      .addColumn(new Column(200))
      .addColumn(new Column(400))
      .setFontSize(4)
      .setFont(PDType1Font.HELVETICA)
      .addRow(
        new RowBuilder()
            .add(new Cell("11")
                .setBackgroundColor(Color.YELLOW)
                .setHorizontalAlignment(Cell.HorizontalAlignment.RIGHT))
            .add(new Cell("12"))
            .build())
      .addRow(
        new RowBuilder()
            .add(new Cell("super").setHorizontalAlignment(Cell.HorizontalAlignment.RIGHT))
            .add(new Cell("geil").setBackgroundColor(Color.GRAY))
            .build())
      .build();
  }
  
  private Table getRingManagerTable() {
    final TableBuilder tableBuilder = new TableBuilder()
        .addColumn(new Column(26))
        .addColumn(new Column(70))
        .addColumn(new Column(390))
        .setFontSize(9)
        .setFont(PDType1Font.HELVETICA);

    float borderWidthOuter = 1.5f;
    float borderWidthInner = 1.0f;
    
    tableBuilder.addRow(new RowBuilder().add(
        new Cell("1.")
        .setBorderWidthTop(borderWidthOuter)
        .setBorderWidthLeft(borderWidthOuter)
        .setBorderWidthRight(borderWidthInner))
        .add(
        new Cell("WK DBV(s)")
        .setBorderWidthTop(borderWidthOuter)
        .setBorderWidthRight(borderWidthInner)
        .setBorderWidthLeft(borderWidthInner))
        .add(
        new Cell("Rote Ecke:")
        .setBorderWidthTop(borderWidthOuter)
        .setBorderWidthRight(borderWidthOuter)).build());
    
    tableBuilder.addRow(new RowBuilder().add(
        new Cell("").setBorderWidthLeft(borderWidthOuter))
        .add(
        new Cell("Jugend")
        .setBorderWidthRight(borderWidthInner)
        .setBorderWidthLeft(borderWidthInner)).add(
    new Cell("Thomas Test, m, FC St. Pauli, 01.01.1998, Jugend, 67,5 kg, 12K (8S, 4N, 0U)")
        .setBorderWidthBottom(borderWidthInner)
        .setBorderWidthRight(borderWidthOuter)).build());
    
    tableBuilder.addRow(new RowBuilder().add(
    new Cell("").setBorderWidthLeft(borderWidthOuter)).add(
    new Cell("3x3")
        .setBorderWidthRight(borderWidthInner)
        .setBorderWidthLeft(borderWidthInner)).add(
    new Cell("Blaue Ecke:").setBorderWidthRight(borderWidthOuter)).build());

    tableBuilder.addRow(new RowBuilder().add(
        new Cell("")
        .setBorderWidthLeft(borderWidthOuter)
        .setBorderWidthBottom(borderWidthOuter)).add(
            new Cell("10 Uz, KS")
        .setBorderWidthRight(borderWidthInner)
        .setBorderWidthLeft(borderWidthInner)
        .setBorderWidthBottom(borderWidthOuter)).add(
    new Cell("Bernd Beispiel, m, Wedeler TSV, 02.01.1999, Jugend, 68,2 kg, 9K (7S, 2N, 0U)")
        .setBorderWidthBottom(borderWidthOuter)
        .setBorderWidthRight(borderWidthOuter)).build());

    return tableBuilder.build();
  }

}
