package org.vandeseer.pdfbox.easytable;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import java.awt.*;
import java.io.IOException;

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
        float startX;
        float startY = tableStartY;

        for (Row row : table.getRows()) {
            final float rowHeight = table.getFontHeight() + row.getHeightWithoutFontHeight();
            int columnCounter = 0;

            startX = tableStartX;
            startY -= rowHeight;

            for (Cell cell : row.getCells()) {
                final float columnWidth = table.getColumns().get(columnCounter).getWidth();
                // Handle the cell's background color
                if (cell.hasBackgroundColor()) {
                    drawCellBackground(cell, startX, startY, columnWidth, rowHeight);
                }

                // Handle the cell's borders
                if (cell.hasBorderTop()) {
                    float borderWidth = cell.getBorderWidthTop();
                    float correction = borderWidth / 2;
                    contentStream.setLineWidth(borderWidth);
                    contentStream.drawLine(startX - correction, startY + rowHeight, startX + columnWidth + correction, startY + rowHeight);
                }

                if (cell.hasBorderBottom()) {
                    float borderWidth = cell.getBorderWidthBottom();
                    float correction = borderWidth / 2;
                    contentStream.setLineWidth(borderWidth);
                    contentStream.drawLine(startX - correction, startY, startX + columnWidth + correction, startY);
                }

                if (cell.hasBorderLeft()) {
                    float borderWidth = cell.getBorderWidthLeft();
                    float correction = borderWidth / 2;
                    contentStream.setLineWidth(borderWidth);
                    contentStream.drawLine(startX, startY - correction, startX, startY + rowHeight + correction);
                }

                if (cell.hasBorderRight()) {
                    float borderWidth = cell.getBorderWidthRight();
                    float correction = borderWidth / 2;
                    contentStream.setLineWidth(borderWidth);
                    contentStream.drawLine(startX + columnWidth, startY - correction, startX + columnWidth, startY + rowHeight + correction);
                }

                // Handle the cell's text
                if (cell.hasText()) {
                    drawCellText(cell, columnWidth, startX, startY);
                }

                startX += columnWidth;
                columnCounter++;
            }
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
        final float yOffset = moveY + cell.getPaddingBottom();

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
