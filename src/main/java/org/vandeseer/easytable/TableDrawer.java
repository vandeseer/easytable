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

            for (final CellBaseData cell : row.getCells()) {
                float cellWidth = table.getAvailableCellWidthRespectingSpan(columnCounter, cell.getSpan());

                // Handle the cell's background color
                if (cell.hasBackgroundColor()) {
                    drawCellBackground(cell, startX, startY, cellWidth, rowHeight);
                }

                // Handle the cell's text
                if (cell instanceof CellText) {
                    drawCellText((CellText) cell, cellWidth, startX, startY);
                } else if (cell instanceof CellImage) {
                    drawCellImage((CellImage) cell, cellWidth, startX, startY);
                }

                startX += cellWidth;

                columnCounter += cell.getSpan();
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

            for (final CellBaseData cell : row.getCells()) {
                float cellWidth = table.getAvailableCellWidthRespectingSpan(columnCounter, cell.getSpan());

                // Handle the cell's borders
                final Color cellBorderColor = cell.getBorderColor();
                final Color rowBorderColor = row.getBorderColor();

                if (cell.hasBorderTop() || cell.hasBorderBottom()) {
                    final float correctionLeft = cell.hasBorderLeft() ? cell.getBorderWidthLeft() / 2 : 0;
                    final float correctionRight = cell.hasBorderRight() ? cell.getBorderWidthRight() / 2 : 0;

                    if (cell.hasBorderTop()) {
                        contentStream.moveTo(startX - correctionLeft, startY + rowHeight);
                        drawLine(cellBorderColor, cell.getBorderWidthTop(), startX + cellWidth + correctionRight, startY + rowHeight);
                        contentStream.setStrokingColor(rowBorderColor);
                    }

                    if (cell.hasBorderBottom()) {
                        contentStream.moveTo(startX - correctionLeft, startY);
                        drawLine(cellBorderColor, cell.getBorderWidthBottom(), startX + cellWidth + correctionRight, startY);
                        contentStream.setStrokingColor(rowBorderColor);
                    }
                }

                if (cell.hasBorderLeft() || cell.hasBorderRight()) {
                    final float correctionTop = cell.hasBorderTop() ? cell.getBorderWidthTop() / 2 : 0;
                    final float correctionBottom = cell.hasBorderBottom() ? cell.getBorderWidthBottom() / 2 : 0;

                    if (cell.hasBorderLeft()) {
                        contentStream.moveTo(startX, startY - correctionBottom);
                        drawLine(cellBorderColor, cell.getBorderWidthLeft(), startX, startY + rowHeight + correctionTop);
                        contentStream.setStrokingColor(rowBorderColor);
                    }

                    if (cell.hasBorderRight()) {
                        contentStream.moveTo(startX + cellWidth, startY - correctionBottom);
                        drawLine(cellBorderColor, cell.getBorderWidthRight(), startX + cellWidth, startY + rowHeight + correctionTop);
                        contentStream.setStrokingColor(rowBorderColor);
                    }
                }

                startX += cellWidth;
                columnCounter += cell.getSpan();
            }
        }

        this.isFinished = true;
    }

    private void drawCellText(final CellText cell, final float columnWidth, final float moveX, final float moveY) throws IOException {
        final PDFont currentFont = cell.getFont();
        final int currentFontSize = cell.getFontSize();
        final Color currentTextColor = cell.getTextColor();

        final List<String> lines;

        float maxWidth = cell.getWidthOfTextAndHorizontalPadding() - (cell.getPaddingLeft() + cell.getPaddingRight());
        if (table.isWordBreak()) {
            lines = PdfUtil.getOptimalTextBreakLines(cell.getText(), currentFont, currentFontSize, maxWidth);
        } else {
            lines = Collections.singletonList(cell.getText());
        }

        // Vertical alignment
        float yStartRelative = cell.getRow().getHeight() - cell.getPaddingTop(); // top position
        if (cell.getRow().getHeight() > cell.getHeight()) {
            if (cell.getSettings().getVerticalAlignment() == VerticalAlignment.MIDDLE) {
                yStartRelative = cell.getRow().getHeight() / 2 + (cell.getHeight() - cell.getPaddingBottom() - cell.getPaddingTop()) / 2;
            } else if (cell.getSettings().getVerticalAlignment() == VerticalAlignment.BOTTOM) {
                yStartRelative = cell.getHeight() - cell.getPaddingTop();
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

            }

            drawText(line, currentFont, currentFontSize, currentTextColor, xOffset, yOffset);
        }
    }

    private void drawCellImage(final CellImage cell, final float columnWidth, final float moveX, final float moveY) throws IOException {
        final Point2D.Float size = cell.getFitSize();
        final Point2D.Float drawAt = new Point2D.Float();

        drawAt.x = moveX
                + ((columnWidth - cell.getPaddingLeft() - cell.getPaddingRight()) / 2f) //middle of cell
                + cell.getPaddingLeft()
                - (size.x / 2f);

        drawAt.y = moveY
                + ((cell.getHeight() - cell.getPaddingTop() - cell.getPaddingBottom()) / 2f) // middle of cell
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
    }

}
