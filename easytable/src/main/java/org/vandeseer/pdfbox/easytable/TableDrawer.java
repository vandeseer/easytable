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
    drawTableTexts();
    drawTableGridHLines(table.getRowHeight());
  }

  private void drawTableTexts() throws IOException {
    float startX = tableStartX;
    float startY = tableStartY;

    for (Row row : table.getRows()) {
      final float rowHeight = table.getFontSize() + row.getVerticalPadding();
      int columnCounter = 0;

      for (Cell cell : row.getCells()) {
        // Handle the cell's background color
        final float columnWidth = table.getColumns().get(columnCounter).getWidth();
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
      startX = tableStartX;
      startY -= rowHeight;
    }
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

  private void drawTableGridHLines(final float rowHeight) throws IOException {
    float currentYOffset = tableStartY;

    contentStream.setLineWidth(table.getBorderWidth());
    final float firstLineYOffset = currentYOffset + rowHeight;
    final float xOffset = tableStartX + table.getWidth();
    contentStream.drawLine(tableStartX, firstLineYOffset, xOffset, firstLineYOffset);

    for (int i = 0; i < table.getRows().size(); i++) {
      contentStream.drawLine(tableStartX, currentYOffset, xOffset, currentYOffset);
      currentYOffset -= rowHeight;
    }
  }

}
