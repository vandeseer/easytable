package org.vandeseer.easytable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellBaseData;
import org.vandeseer.easytable.structure.cell.CellImage;
import org.vandeseer.easytable.structure.cell.CellText;
import org.vandeseer.easytable.util.PdfUtil;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TableDrawer {

    @Setter
    @Accessors(chain = true, fluent = true)
    private PDPageContentStream contentStream;

    private final Table table;

    @Setter
    @Accessors(chain = true, fluent = true)
    private float startX;

    @Setter
    @Accessors(chain = true, fluent = true)
    private float startY;

    private float endY;

    private int rowToDraw = 0;

    @Getter
    private boolean isFinished = false;

    @Builder
    TableDrawer(float startX, float startY, PDPageContentStream contentStream, Table table, float endY) {
        this.contentStream = contentStream;
        this.table = table;

        this.startX = startX;
        this.startY = startY - PdfUtil.getFontHeight(table.getSettings().getFont(), table.getSettings().getFontSize());

        this.endY = endY;
    }

    public void draw() throws IOException {
        drawBackgroundAndCellContent(this.startX, this.startY);
        drawBorders(this.startX, this.startY);
    }

    private void drawBackgroundAndCellContent(float initialX, float initialY) throws IOException {
        float startX;
        float startY = initialY;

        for (int i = rowToDraw; i < table.getRows().size(); i++) {
            final Row row = table.getRows().get(i);
            final float rowHeight = row.getHeight();
            int columnCounter = 0;

            startX = initialX;
            startY -= rowHeight;

            if (startY < endY) {
                return;
            }

            List<CellBaseData> cells = row.getCells();
            for (int j = 0; j < cells.size(); j++) {
                // Row span
                // TODO refactor!!
                int k = 0;
                while (table.isRowSpanAt(i, j + k)) {
                    for (int x = 0; x < table.getRowSpanCellFor(i, j + k); x++) {
                        startX += table.getColumns().get(columnCounter).getWidth();
                        columnCounter++;
                    }
                    k++;
                }

                CellBaseData cell = cells.get(j);
                float cellWidth = table.getAvailableCellWidthRespectingSpan(columnCounter, cell.getColSpan());

                // Handle the cell's background color
                if (cell.hasBackgroundColor()) {
                    float height = cell.getHeight() > rowHeight ? cell.getHeight() : rowHeight; // TODO
                    float sY = cell.getHeight() > rowHeight ? startY + rowHeight - cell.getHeight() : startY; // TODO
                    drawCellBackground(cell, startX, sY, cellWidth, height);
                }

                // Handle the cell's text
                if (cell instanceof CellText) {
                    drawCellText((CellText) cell, cellWidth, startX, startY);
                } else if (cell instanceof CellImage) {
                    float sY = cell.getHeight() > rowHeight ? startY + rowHeight - cell.getHeight() : startY; // TODO
                    drawCellImage((CellImage) cell, cellWidth, startX, sY);
                }

                startX += cellWidth;
                columnCounter += cell.getColSpan();
            }
        }
    }

    private void drawBorders(float initialX, float initialY) throws IOException {
        float startX;
        float startY = initialY;

        for (int i = rowToDraw; i < table.getRows().size(); i++) {
            final Row row = table.getRows().get(i);
            final float rowHeight = row.getHeight();
            int columnCounter = 0;

            startX = initialX;
            startY -= rowHeight;

            if (startY < endY) {
                rowToDraw = i;
                return;
            }

            List<CellBaseData> cells = row.getCells();
            for (int j = 0; j < cells.size(); j++) {
                // Row span
                // TODO refactor!!
                int k = 0;
                while (table.isRowSpanAt(i, j + k)) {
                    for (int x = 0; x < table.getRowSpanCellFor(i, j + k); x++) {
                        startX += table.getColumns().get(columnCounter).getWidth();
                        columnCounter++;
                    }
                    k++;
                }

                CellBaseData cell = cells.get(j);
                float cellWidth = table.getAvailableCellWidthRespectingSpan(columnCounter, cell.getColSpan());

                // Handle the cell's borders
                final Color cellBorderColor = cell.getBorderColor();
                final Color rowBorderColor = row.getSettings().getBorderColor();

                if (cell.hasBorderTop() || cell.hasBorderBottom()) {
                    final float correctionLeft = cell.getBorderWidthLeft() / 2;
                    final float correctionRight = cell.getBorderWidthRight() / 2;

                    if (cell.hasBorderTop()) {
                        contentStream.moveTo(startX - correctionLeft, startY + rowHeight);
                        drawLine(cellBorderColor, cell.getBorderWidthTop(), startX + cellWidth + correctionRight, startY + rowHeight);
                        contentStream.setStrokingColor(rowBorderColor);
                    }

                    if (cell.hasBorderBottom()) {
                        float sY = cell.getHeight() > rowHeight ? startY + rowHeight - cell.getHeight() : startY; // TODO
                        contentStream.moveTo(startX - correctionLeft, sY);
                        drawLine(cellBorderColor, cell.getBorderWidthBottom(), startX + cellWidth + correctionRight, sY);
                        contentStream.setStrokingColor(rowBorderColor);
                    }
                }

                if (cell.hasBorderLeft() || cell.hasBorderRight()) {
                    final float correctionTop = cell.getBorderWidthTop() / 2;
                    final float correctionBottom = cell.getBorderWidthBottom() / 2;

                    if (cell.hasBorderLeft()) {
                        float sY = cell.getHeight() > rowHeight ? startY + rowHeight - cell.getHeight() : startY; // TODO
                        float height = cell.getHeight() > rowHeight ? cell.getHeight() : rowHeight; // TODO
                        contentStream.moveTo(startX, sY - correctionBottom);
                        drawLine(cellBorderColor, cell.getBorderWidthLeft(), startX, sY + height + correctionTop);
                        contentStream.setStrokingColor(rowBorderColor);
                    }

                    if (cell.hasBorderRight()) {
                        float sY = cell.getHeight() > rowHeight ? startY + rowHeight - cell.getHeight() : startY; // TODO
                        float height = cell.getHeight() > rowHeight ? cell.getHeight() : rowHeight; // TODO
                        contentStream.moveTo(startX + cellWidth, sY - correctionBottom);
                        drawLine(cellBorderColor, cell.getBorderWidthRight(), startX + cellWidth, sY + height + correctionTop);
                        contentStream.setStrokingColor(rowBorderColor);
                    }
                }

                startX += cellWidth;
                columnCounter += cell.getColSpan();
            }
        }

        this.isFinished = true;
    }

    private void drawCellText(final CellText cell, final float columnWidth, final float moveX, float moveY) throws IOException {
        final PDFont currentFont = cell.getFont();
        final int currentFontSize = cell.getFontSize();
        final Color currentTextColor = cell.getTextColor();

        float maxWidth = cell.getWidthOfText();

        final List<String> lines = cell.isWordBreak()
                ? PdfUtil.getOptimalTextBreakLines(cell.getText(), currentFont, currentFontSize, maxWidth)
                : Collections.singletonList(cell.getText());

        // Vertical alignment
        float yStartRelative = cell.getRow().getHeight() - cell.getPaddingTop(); // top position
        if (cell.getRow().getHeight() > cell.getHeight() || cell.getRowSpan() > 1) {

            if (cell.getSettings().getVerticalAlignment() == VerticalAlignment.MIDDLE) {

                float outerHeight = cell.getRowSpan() > 1 ? cell.getHeight() : cell.getRow().getHeight();
                yStartRelative = outerHeight / 2 + cell.getTextHeight() / 2;

                if (cell.getRowSpan() > 1) {
                    float rowSpanAdaption = cell.calculateHeightForRowSpan() - cell.getRow().getHeight();
                    yStartRelative -= rowSpanAdaption;
                }

            } else if (cell.getSettings().getVerticalAlignment() == VerticalAlignment.BOTTOM) {

                yStartRelative = cell.getTextHeight() + cell.getPaddingBottom();

                if (cell.getRowSpan() > 1) {
                    float rowSpanAdaption = cell.calculateHeightForRowSpan() - cell.getRow().getHeight();
                    yStartRelative -= rowSpanAdaption;
                }
            }
        }

        float yOffset = moveY + yStartRelative;
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);

            float xOffset = moveX + cell.getPaddingLeft();
            yOffset -= (
                    PdfUtil.getFontHeight(currentFont, currentFontSize) // font height
                            + (i > 0 ? PdfUtil.getFontHeight(currentFont, currentFontSize) * cell.getLineSpacing() : 0f) // line spacing
            );

            final float textWidth = PdfUtil.getStringWidth(line, currentFont, currentFontSize);

            // Handle horizontal alignment by adjusting the xOffset
            if (cell.getSettings().getHorizontalAlignment() == HorizontalAlignment.RIGHT) {
                xOffset = moveX + (columnWidth - (textWidth + cell.getPaddingRight()));

            } else if (cell.getSettings().getHorizontalAlignment() == HorizontalAlignment.CENTER) {
                final float diff = (columnWidth - textWidth) / 2;
                xOffset = moveX + diff;

            } else if (cell.getSettings().getHorizontalAlignment() == HorizontalAlignment.JUSTIFY) {

                // Code from https://stackoverflow.com/questions/20680430/is-it-possible-to-justify-text-in-pdfbox
                float charSpacing = 0;
                if (line.length() > 1) {
                    float size = PdfUtil.getStringWidth(line, cell.getFont(), cell.getFontSize());
                    float free = cell.getWidthOfText() - size;
                    if (free > 0) {
                        charSpacing = free / (line.length() - 1);
                    }
                }

                // Don't justify the last line
                if (i < lines.size() -1) {

                    // setCharacterSpacing() is available in PDFBox version 2.0.4 and higher.
                    contentStream.setCharacterSpacing(charSpacing);
                }
            }

            drawText(line, currentFont, currentFontSize, currentTextColor, xOffset, yOffset);
        }
    }

    private void drawCellImage(final CellImage cell, final float columnWidth, final float moveX, final float moveY) throws IOException {
        final Point2D.Float size = cell.getFitSize();
        final Point2D.Float drawAt = new Point2D.Float();

        drawAt.x = moveX
                + ((columnWidth - cell.getHorizontalPadding()) / 2f) //middle of cell
                + cell.getPaddingLeft()
                - (size.x / 2f);

        drawAt.y = moveY
                + ((cell.getHeight() - cell.getVerticalPadding()) / 2f) // middle of cell
                + cell.getPaddingBottom()
                - (size.y / 2f);

        contentStream.drawImage(cell.getImage(), drawAt.x, drawAt.y, size.x, size.y);
    }

    private void drawLine(Color color, float width, float toX, float toY) throws IOException {
        contentStream.setLineWidth(width);
        contentStream.lineTo(toX, toY);
        contentStream.setStrokingColor(color);
        contentStream.stroke();
    }

    private void drawCellBackground(final CellBaseData cell, final float startX, final float startY, final float width, final float height)
            throws IOException {
        contentStream.setNonStrokingColor(cell.getBackgroundColor());

        contentStream.addRect(startX, startY, width, height);
        contentStream.fill();
        contentStream.closePath();

        // Reset NonStrokingColor to default value
        contentStream.setNonStrokingColor(Color.BLACK);
    }

    private void drawText(String text, PDFont font, int fontSize, Color color, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.setNonStrokingColor(color);
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
        contentStream.setCharacterSpacing(0);
    }

}
