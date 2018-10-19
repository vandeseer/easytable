package org.vandeseer.pdfbox.easytable;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TableDrawer {

    private final float tableStartX;
    private final float tableStartY;
    private final PDPageContentStream contentStream;
    private final Table table;

    public static class TableDrawerBuilder {
        private float tableStartX;
        private float tableStartY;
        private PDPageContentStream contentStream;
        private Table table;

        // TODO can we somehow put the "cursor" directly to where the content stream is right now?!
        public static TableDrawerBuilder newBuilder() {
            return new TableDrawerBuilder();
        }

        public TableDrawerBuilder startX(final float startX) {
            tableStartX = startX;
            return this;
        }

        public TableDrawerBuilder startY(final float startY) {
            tableStartY = startY;
            return this;
        }

        public TableDrawerBuilder withContentStream(final PDPageContentStream contentStream) {
            this.contentStream = contentStream;
            return this;
        }

        public TableDrawerBuilder withTable(final Table table) {
            this.table = table;
            return this;
        }

        public TableDrawer build() {
            return new TableDrawer(contentStream, table, tableStartX, tableStartY);
        }
    }

    public TableDrawer(final PDPageContentStream contentStream, final Table table, final float startX, final float startY) {
        this.contentStream = contentStream;
        this.table = table;
        tableStartX = startX;
        tableStartY = startY - PdfUtil.getFontHeight(table.getFont(), table.getFontSize());
    }

    public void draw() throws IOException {
        drawBackgroundAndText();
        drawBorders();
    }

    private void drawBackgroundAndText() throws IOException {
        float startX;
        float startY = tableStartY;

        for (final Row row : table.getRows()) {
            final float rowHeight = row.getHeight();
            int columnCounter = 0;

            startX = tableStartX;
            startY -= rowHeight;

            for (final Cell cell : row.getCells()) {
                float cellWidth = 0;
                for (int i = 0; i < cell.getSpan(); i++) {
                    cellWidth += table.getColumns().get(columnCounter + i).getWidth();
                }

                // Handle the cell's background color
                if (cell.hasBackgroundColor()) {
                    drawCellBackground(cell, startX, startY, cellWidth, rowHeight);
                }

                // Handle the cell's text
                if (cell.hasText()) {
                    drawCellText(cell, cellWidth, startX, startY);
                }

                startX += cellWidth;
                columnCounter += cell.getSpan();
            }
        }
    }

    private void drawBorders() throws IOException {
        float startX;
        float startY = tableStartY;

        for (final Row row : table.getRows()) {
            final float rowHeight = row.getHeight();
            int columnCounter = 0;

            startX = tableStartX;
            startY -= rowHeight;

            for (final Cell cell : row.getCells()) {
                float cellWidth = 0;
                for (int i = 0; i < cell.getSpan(); i++) {
                    cellWidth += table.getColumns().get(columnCounter + i).getWidth();
                }

                // Handle the cell's borders
                if (cell.hasBorderTop()) {
                    final float borderWidth = cell.getBorderWidthTop();
                    final float correctionLeft = cell.hasBorderLeft() ? cell.getBorderWidthLeft() / 2 : 0;
                    final float correctionRight = cell.hasBorderRight() ? cell.getBorderWidthRight() / 2 : 0;
                    contentStream.setLineWidth(borderWidth);
                    contentStream.moveTo(startX - correctionLeft, startY + rowHeight);
                    contentStream.lineTo(startX + cellWidth + correctionRight, startY + rowHeight);
                    contentStream.setStrokingColor(cell.getBorderColor());
                    contentStream.stroke();
                    contentStream.setStrokingColor(cell.getParentBorderColor());
                }

                if (cell.hasBorderBottom()) {
                    final float borderWidth = cell.getBorderWidthBottom();
                    final float correctionLeft = cell.hasBorderLeft() ? cell.getBorderWidthLeft() / 2 : 0;
                    final float correctionRight = cell.hasBorderRight() ? cell.getBorderWidthRight() / 2 : 0;
                    contentStream.setLineWidth(borderWidth);
                    contentStream.moveTo(startX - correctionLeft, startY);
                    contentStream.lineTo(startX + cellWidth + correctionRight, startY);
                    contentStream.setStrokingColor(cell.getBorderColor());
                    contentStream.stroke();
                    contentStream.setStrokingColor(cell.getParentBorderColor());
                }

                if (cell.hasBorderLeft()) {
                    final float borderWidth = cell.getBorderWidthLeft();
                    final float correctionTop = cell.hasBorderTop() ? cell.getBorderWidthTop() / 2 : 0;
                    final float correctionBottom = cell.hasBorderBottom() ? cell.getBorderWidthBottom() / 2 : 0;
                    contentStream.setLineWidth(borderWidth);
                    contentStream.moveTo(startX, startY - correctionBottom);
                    contentStream.lineTo(startX, startY + rowHeight + correctionTop);
                    contentStream.setStrokingColor(cell.getBorderColor());
                    contentStream.stroke();
                    contentStream.setStrokingColor(cell.getParentBorderColor());
                }

                if (cell.hasBorderRight()) {
                    final float borderWidth = cell.getBorderWidthRight();
                    final float correctionTop = cell.hasBorderTop() ? cell.getBorderWidthTop() / 2 : 0;
                    final float correctionBottom = cell.hasBorderBottom() ? cell.getBorderWidthBottom() / 2 : 0;
                    contentStream.setLineWidth(borderWidth);
                    contentStream.moveTo(startX + cellWidth, startY - correctionBottom);
                    contentStream.lineTo(startX + cellWidth, startY + rowHeight + correctionTop);
                    contentStream.setStrokingColor(cell.getBorderColor());
                    contentStream.stroke();
                    contentStream.setStrokingColor(cell.getParentBorderColor());
                }

                startX += cellWidth;
                columnCounter += cell.getSpan();
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
        final PDFont currentFont = cell.getFont();
        final int currentFontSize = cell.getFontSize();
        final Color currentTextColor = cell.getTextColor();

        final List<String> lines;

        if (table.isWordBreak()) {
            lines = PdfUtil.getOptimalTextBreak(cell.getText(), currentFont, currentFontSize,
                    columnWidth - cell.getPaddingRight() - cell.getPaddingRight());
        } else {
            lines = Collections.singletonList(cell.getText());
        }

        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            contentStream.beginText();
            contentStream.setNonStrokingColor(currentTextColor);
            contentStream.setFont(currentFont, currentFontSize);

            float xOffset = moveX + cell.getPaddingLeft();
            final float yOffset = moveY
                    + cell.getRow().getHeight() // top-position
                    - PdfUtil.getFontHeight(currentFont, currentFontSize) / 2
                    - ((cell.getRow().getHeight() / (lines.size() + 1)) * (i + 1)); // middle of cell

            final float textWidth = PdfUtil.getStringWidth(line, currentFont, currentFontSize);

            switch (cell.getHorizontalAlignment()) {
                case RIGHT:
                    xOffset = moveX + (columnWidth - (textWidth + cell.getPaddingRight()));
                    break;
                case CENTER:
                    final float diff = (columnWidth - textWidth) / 2;
                    xOffset = moveX + diff;
                    break;
            }

            contentStream.newLineAtOffset(xOffset, yOffset);
            contentStream.showText(line);
            contentStream.endText();


        }
    }

}
