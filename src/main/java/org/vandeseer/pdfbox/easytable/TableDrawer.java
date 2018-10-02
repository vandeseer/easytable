package org.vandeseer.pdfbox.easytable;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;
import java.io.IOException;

public class TableDrawer {

    private final float tableStartX;
    private final float tableStartY;
    private final PDPageContentStream contentStream;
    private final Table table;

    public TableDrawer(final PDPageContentStream contentStream, final Table table, final float startX, final float startY) {
        this.contentStream = contentStream;
        this.table = table;
        tableStartX = startX;
        tableStartY = startY - table.getFontHeight();
    }

    public void draw() throws IOException {
        drawBackgroundAndText();
        drawBorders();
    }

    private void drawBackgroundAndText() throws IOException {
        float startX;
        float startY = tableStartY;

        for (Row row : table.getRows()) {
            final float rowHeight = row.getFontHeight() + row.getHeightWithoutFontHeight();
            int columnCounter = 0;

            startX = tableStartX;
            startY -= rowHeight;

            for (Cell cell : row.getCells()) {
                final float columnWidth = table.getColumns().get(columnCounter).getWidth();
                // Handle the cell's background color
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
        }
    }

    private void drawBorders() throws IOException {
        float startX;
        float startY = tableStartY;

        for (Row row : table.getRows()) {
            final float rowHeight = row.getFontHeight() + row.getHeightWithoutFontHeight();
            int columnCounter = 0;

            startX = tableStartX;
            startY -= rowHeight;

            for (Cell cell : row.getCells()) {
                final float columnWidth = table.getColumns().get(columnCounter).getWidth();

                // Handle the cell's borders
                if (cell.hasBorderTop()) {
                    float borderWidth = cell.getBorderWidthTop();
                    float correctionLeft = cell.hasBorderLeft() ? cell.getBorderWidthLeft() / 2 : 0;
                    float correctionRight = cell.hasBorderRight() ? cell.getBorderWidthRight() / 2 : 0;
                    contentStream.setLineWidth(borderWidth);
                    contentStream.moveTo(startX - correctionLeft, startY + rowHeight);
                    contentStream.lineTo(startX + columnWidth + correctionRight, startY + rowHeight);
                    contentStream.setStrokingColor(cell.getBorderColor());
                    contentStream.stroke();
                    contentStream.setStrokingColor(cell.getParentBorderColor());
                }

                if (cell.hasBorderBottom()) {
                    float borderWidth = cell.getBorderWidthBottom();
                    float correctionLeft = cell.hasBorderLeft() ? cell.getBorderWidthLeft() / 2 : 0;
                    float correctionRight = cell.hasBorderRight() ? cell.getBorderWidthRight() / 2 : 0;
                    contentStream.setLineWidth(borderWidth);
                    contentStream.moveTo(startX - correctionLeft, startY);
                    contentStream.lineTo(startX + columnWidth + correctionRight, startY);
                    contentStream.setStrokingColor(cell.getBorderColor());
                    contentStream.stroke();
                    contentStream.setStrokingColor(cell.getParentBorderColor());
                }

                if (cell.hasBorderLeft()) {
                    float borderWidth = cell.getBorderWidthLeft();
                    float correctionTop = cell.hasBorderTop() ? cell.getBorderWidthTop() / 2 : 0;
                    float correctionBottom = cell.hasBorderBottom() ? cell.getBorderWidthBottom() / 2 : 0;
                    contentStream.setLineWidth(borderWidth);
                    contentStream.moveTo(startX, startY - correctionBottom);
                    contentStream.lineTo(startX, startY + rowHeight + correctionTop);
                    contentStream.setStrokingColor(cell.getBorderColor());
                    contentStream.stroke();
                    contentStream.setStrokingColor(cell.getParentBorderColor());
                }

                if (cell.hasBorderRight()) {
                    float borderWidth = cell.getBorderWidthRight();
                    float correctionTop = cell.hasBorderTop() ? cell.getBorderWidthTop() / 2 : 0;
                    float correctionBottom = cell.hasBorderBottom() ? cell.getBorderWidthBottom() / 2 : 0;
                    contentStream.setLineWidth(borderWidth);
                    contentStream.moveTo(startX + columnWidth, startY - correctionBottom);
                    contentStream.lineTo(startX + columnWidth, startY + rowHeight + correctionTop);
                    contentStream.setStrokingColor(cell.getBorderColor());
                    contentStream.stroke();
                    contentStream.setStrokingColor(cell.getParentBorderColor());
                }

                startX += columnWidth;
                columnCounter++;
            }
        }
    }

    private void drawCellBackground(final Cell cell, final float startX, final float startY, final float width, final float height)
            throws IOException {
        contentStream.setNonStrokingColor(cell.getBackgroundColor());

        contentStream.addRect(startX, startY, width, height);
        contentStream.fill();
        contentStream.closePath();

        // Reset NonStroking Color to default value
        contentStream.setNonStrokingColor(Color.BLACK);
    }

    private void drawCellText(final Cell cell, final float columnWidth, final float moveX, final float moveY) throws IOException {
        contentStream.beginText();
        contentStream.setNonStrokingColor(cell.getTextColor());
        contentStream.setFont(cell.getFont().orElse(table.getFont()), cell.getFontSize().orElse(table.getFontSize()));

        float xOffset = moveX + cell.getPaddingLeft();
        final float yOffset = moveY + cell.getPaddingBottom();

        int currentFontSize = cell.getFontSize().orElse(table.getFontSize());
        final float textWidth = (table.getFont().getStringWidth(cell.getText()) / 1000f) * currentFontSize;

        switch (cell.getHorizontalAlignment()){
            case RIGHT:
                xOffset = moveX + (columnWidth - (textWidth + cell.getPaddingRight()));
                break;
            case CENTER:
                final float diff = (columnWidth - textWidth) / 2;
                xOffset = moveX + diff;
                break;
        }

        contentStream.newLineAtOffset(xOffset, yOffset);
        contentStream.showText(cell.getText());
        contentStream.endText();
    }

}
