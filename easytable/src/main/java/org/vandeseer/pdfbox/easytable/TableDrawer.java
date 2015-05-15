package org.vandeseer.pdfbox.easytable;

import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

public class TableDrawer {

  private float tableStartX;
  private float tableStartY;
  private PDPageContentStream contentStream;
  private Table table;

  public TableDrawer(PDDocument document, PDPage page, Table table) throws IOException {
    this.table = table;

    // Prepare the content stream and its use for landscape mode
    contentStream = new PDPageContentStream(document, page);
    contentStream.concatenate2CTM(0, 1, -1, 0, page.findMediaBox().getWidth(), 0);

    tableStartX = table.getMarginLeft();
    tableStartY = page.findMediaBox().getWidth() - table.getMarginTop() - table.getFontHeight();
  }

  public void draw() throws IOException {
    drawTableTexts();
    drawTableGridHLines(table.getRowHeight());

    contentStream.close();
  }

  private void drawTableTexts() throws IOException {
    float startX = tableStartX;
    float startY = tableStartY;

    Iterator<Row> rowIter = table.getRows().iterator();
    while (rowIter.hasNext()) {
      Row row = rowIter.next();
      float rowHeight = table.getFontSize() + row.getVerticalPadding();
      int columnCounter = 0;

      Iterator<Cell> cellIter = row.getCells().iterator();
      while (cellIter.hasNext()) {
        Cell cell = cellIter.next();

        // Handle the cell's background color
        float columnWidth = table.getColumns().get(columnCounter).getWidth();
        if (cell.hasBackgroundColor()) {
          drawCellBackground(cell, startX, startY, columnWidth, rowHeight);
        }

        // Handle the cell's text
        if (cell.hasText()) {
          drawCellText(cell, columnWidth, startX, startY);
        }

        startX += columnWidth;
        columnCounter++;
      }
      startX = table.getMarginLeft();
      startY -= rowHeight;
    }
  }

  private void drawCellBackground(Cell cell, float startX, float startY, float width, float height)
      throws IOException {
    contentStream.setNonStrokingColor(cell.getBackgroundColor());
    contentStream.fillRect(startX, startY, width, height);
    contentStream.closeSubPath();

    // Reset NonStroking Color to default value
    contentStream.setNonStrokingColor(Color.BLACK);
  }

  private void drawCellText(Cell cell, float columnWidth, float moveX, float moveY)
      throws IOException {
    contentStream.beginText();
    contentStream.setFont(table.getFont(), table.getFontSize());

    float xOffset = moveX + cell.getPaddingLeft();
    float yOffset = moveY + cell.getPaddingTop();

    if (cell.getHorizontalAlignment().equals(Cell.HorizontalAlignment.RIGHT)) {
      // For the calculation of text width, see:
      // http://stackoverflow.com/questions/24004539/right-alignment-text-in-pdfbox
      float textWidth = (table.getFont().getStringWidth(cell.getText()) / 1000f) * table.getFontSize();
      xOffset = moveX + (columnWidth - (textWidth + cell.getPaddingRight()));
    }

    contentStream.moveTextPositionByAmount(xOffset, yOffset);
    contentStream.drawString(cell.getText());
    contentStream.endText();
  }

  private void drawTableGridHLines(float rowHeight) throws IOException {
    float currentYOffset = tableStartY;

    contentStream.setLineWidth(table.getBorderWidth());
    float firstLineYOffset = currentYOffset + rowHeight;
    float xOffset = tableStartX + table.getWidth();
    contentStream.drawLine(tableStartX, firstLineYOffset, xOffset, firstLineYOffset);

    for (int i = 0; i < table.getRows().size(); i++) {
      contentStream.drawLine(tableStartX, currentYOffset, xOffset, currentYOffset);
      currentYOffset -= rowHeight;
    }
  }

}
