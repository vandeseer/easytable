package org.vandeseer.pdfbox.easytable;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

public class TableDrawer {

  private final float tableStartX;
  private final float tableStartY;
  private final PDPageContentStream contentStream;
  private final Table table;

  public TableDrawer(final PDPageContentStream contentStream, final Table table, final float startX, final float startY)
      throws IOException {
    this.contentStream = contentStream;
    this.table = table;
    tableStartX = startX;
    tableStartY = startY - table.getFontHeight();
  }

  public void draw() throws IOException {
    float startX = tableStartX;
    float startY = tableStartY;

    for (Row row : table.getRows()) {
      final float rowHeight = table.getFontSize() + row.getVerticalPadding();
      int columnCounter = 0;

      for (Cell cell : row.getCells()) {
        final float columnWidth = table.getColumns().get(columnCounter).getWidth();
        // Handle the cell's background color
        if (cell.hasBackgroundColor()) {
          drawCellBackground(cell, startX, startY, columnWidth, rowHeight);
        }

        // Handle the cell's borders
        if (cell.hasBorderTop()) {
          drawHorizontalCellBorder(cell, startX, startY + rowHeight, columnWidth);
        }
        
        if (cell.hasBorderBottom()) {
          drawHorizontalCellBorder(cell, startX, startY, columnWidth);
        }
        
        if (cell.hasBorderLeft()) {
          drawVerticalCellBorder(cell, startX, startY, rowHeight);
        }

        if (cell.hasBorderRight()) {
          drawVerticalCellBorder(cell, startX + columnWidth, startY, rowHeight);
        }
        
        // Handle the cell's text
        if (cell.hasText()) {
          drawCellText(cell, columnWidth, startX, startY);
        }

        startX += columnWidth;
        columnCounter++;
      }
      startX = tableStartX;
      startY -= rowHeight;
    }
  }

  private void drawVerticalCellBorder(Cell cell, float startX, float startY, float rowHeight)
      throws IOException {
    contentStream.setLineWidth(cell.getBorderWidth());
    contentStream.drawLine(startX, startY, startX, startY + rowHeight + cell.getBorderWidth() / 2);
  }

  private void drawHorizontalCellBorder(Cell cell, float startX, float startY, float columnWidth)
      throws IOException {
    contentStream.setLineWidth(cell.getBorderWidth());
    contentStream.drawLine(startX, startY, startX + columnWidth + cell.getBorderWidth() / 2, startY);
  }

  private void drawCellBackground(final Cell cell, final float startX, final float startY, final float width, final float height)
      throws IOException {
    contentStream.setNonStrokingColor(cell.getBackgroundColor());
    contentStream.fillRect(startX, startY, width, height);
    contentStream.closeSubPath();

    // Reset NonStroking Color to default value
    contentStream.setNonStrokingColor(Color.BLACK);
  }

  private void drawCellText(final Cell cell, final float columnWidth, final float moveX, final float moveY)
      throws IOException {
    contentStream.beginText();
    contentStream.setFont(table.getFont(), table.getFontSize());

    float xOffset = moveX + cell.getPaddingLeft();
    final float yOffset = moveY + cell.getPaddingTop();

    if (cell.getHorizontalAlignment().equals(Cell.HorizontalAlignment.RIGHT)) {
      // For the calculation of text width, see:
      // http://stackoverflow.com/questions/24004539/right-alignment-text-in-pdfbox
      final float textWidth =
          (table.getFont().getStringWidth(cell.getText()) / 1000f) * table.getFontSize();
      xOffset = moveX + (columnWidth - (textWidth + cell.getPaddingRight()));
    }

    contentStream.moveTextPositionByAmount(xOffset, yOffset);
    contentStream.drawString(cell.getText());
    contentStream.endText();
  }

}
