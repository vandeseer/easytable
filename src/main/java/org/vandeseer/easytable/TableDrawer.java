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
import java.util.Comparator;
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
        drawWithFunction(new Point2D.Float(this.startX, this.startY), this::drawBackgroundColorAndCellContent, false);
        drawWithFunction(new Point2D.Float(this.startX, this.startY), this::drawBorders, true);
    }

    private void drawWithFunction(Point2D.Float startingPoint, TableDrawerFunction function, boolean isLastAction) throws IOException {
        float y = startingPoint.y;

        for (int i = rowToDraw; i < table.getRows().size(); i++) {
            final Row row = table.getRows().get(i);
            int columnCounter = 0;

            // First of all, we need to check whether we should draw any further ...
            final float lowestPoint = y - row.getCells().stream()
                    .map(CellBaseData::getHeight)
                    .max(Comparator.naturalOrder())
                    .orElse(row.getHeight());

            if (lowestPoint < endY) {
                if (isLastAction) {
                    rowToDraw = i;
                }
                return;
            }

            float x = startingPoint.x;
            y -= row.getHeight();

            for (CellBaseData cell : row.getCells()) {

                while (table.isRowSpanAt(i, columnCounter)) {
                    x += table.getColumns().get(columnCounter).getWidth();
                    columnCounter++;
                }

                float cellWidth = table.getAvailableCellWidthRespectingSpan(columnCounter, cell.getColSpan());

                // This is the interesting part :)
                function.accept(new Point2D.Float(x, y), row, cell, cellWidth);

                x += cellWidth;
                columnCounter += cell.getColSpan();
            }
        }

        if (isLastAction) {
            this.isFinished = true;
        }
    }

    private void drawBackgroundColorAndCellContent(Point2D.Float start, Row row, CellBaseData cell, float cellWidth) throws IOException {

        final float rowHeight = row.getHeight();
        final float height = cell.getHeight() > rowHeight ? cell.getHeight() : rowHeight;
        final float y = cell.getHeight() > rowHeight ? start.y + rowHeight - cell.getHeight() : start.y;

        // Handle the cell's background color
        if (cell.hasBackgroundColor()) {
            drawCellBackground(cell, start.x, y, cellWidth, height);
        }

        // Handle the cell's content
        if (cell instanceof CellText) {
            drawCellText((CellText) cell, cellWidth, start.x, start.y);
        } else if (cell instanceof CellImage) {
            drawCellImage((CellImage) cell, cellWidth, start.x, start.y);
        }

    }

    private void drawBorders(Point2D.Float start, Row row, CellBaseData cell, float cellWidth) throws IOException {
        final float rowHeight = row.getHeight();

        final float height = cell.getHeight() > rowHeight ? cell.getHeight() : rowHeight;
        final float sY = cell.getHeight() > rowHeight ? start.y + rowHeight - cell.getHeight() : start.y;

        // Handle the cell's borders
        final Color cellBorderColor = cell.getBorderColor();
        final Color rowBorderColor = row.getSettings().getBorderColor();

        if (cell.hasBorderTop() || cell.hasBorderBottom()) {
            final float correctionLeft = cell.getBorderWidthLeft() / 2;
            final float correctionRight = cell.getBorderWidthRight() / 2;

            if (cell.hasBorderTop()) {
                contentStream.moveTo(start.x - correctionLeft, start.y + rowHeight);
                drawLine(cellBorderColor, cell.getBorderWidthTop(), start.x + cellWidth + correctionRight, start.y + rowHeight);
                contentStream.setStrokingColor(rowBorderColor);
            }

            if (cell.hasBorderBottom()) {
                contentStream.moveTo(start.x - correctionLeft, sY);
                drawLine(cellBorderColor, cell.getBorderWidthBottom(), start.x + cellWidth + correctionRight, sY);
                contentStream.setStrokingColor(rowBorderColor);
            }
        }

        if (cell.hasBorderLeft() || cell.hasBorderRight()) {
            final float correctionTop = cell.getBorderWidthTop() / 2;
            final float correctionBottom = cell.getBorderWidthBottom() / 2;

            if (cell.hasBorderLeft()) {
                contentStream.moveTo(start.x, sY - correctionBottom);
                drawLine(cellBorderColor, cell.getBorderWidthLeft(), start.x, sY + height + correctionTop);
                contentStream.setStrokingColor(rowBorderColor);
            }

            if (cell.hasBorderRight()) {
                contentStream.moveTo(start.x + cellWidth, sY - correctionBottom);
                drawLine(cellBorderColor, cell.getBorderWidthRight(), start.x + cellWidth, sY + height + correctionTop);
                contentStream.setStrokingColor(rowBorderColor);
            }
        }
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

        // TODO Refactor out vertical alignment for image and text cells? The logic is the same for both types ...
        // Vertical alignment
        float yStartRelative = cell.getRow().getHeight() - cell.getPaddingTop(); // top position
        if (cell.getRow().getHeight() > cell.getHeight() || cell.getRowSpan() > 1) {

            if (cell.getSettings().getVerticalAlignment() == VerticalAlignment.MIDDLE) {

                float outerHeight = cell.getRowSpan() > 1 ? cell.getHeight() : cell.getRow().getHeight();
                yStartRelative = outerHeight / 2 + size.y / 2;

                if (cell.getRowSpan() > 1) {
                    float rowSpanAdaption = cell.calculateHeightForRowSpan() - cell.getRow().getHeight();
                    yStartRelative -= rowSpanAdaption;
                }

            } else if (cell.getSettings().getVerticalAlignment() == VerticalAlignment.BOTTOM) {

                yStartRelative = size.y + cell.getPaddingBottom();

                if (cell.getRowSpan() > 1) {
                    float rowSpanAdaption = cell.calculateHeightForRowSpan() - cell.getRow().getHeight();
                    yStartRelative -= rowSpanAdaption;
                }
            }
        }

        // Handle horizontal alignment by adjusting the xOffset
        float xOffset = moveX + cell.getPaddingLeft();
        if (cell.getSettings().getHorizontalAlignment() == HorizontalAlignment.RIGHT) {
            xOffset = moveX + (columnWidth - (size.x + cell.getPaddingRight()));

        } else if (cell.getSettings().getHorizontalAlignment() == HorizontalAlignment.CENTER) {
            final float diff = (columnWidth - size.y) / 2;
            xOffset = moveX + diff;

        }

        drawAt.x = xOffset;
        drawAt.y = moveY + yStartRelative - size.y;

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
