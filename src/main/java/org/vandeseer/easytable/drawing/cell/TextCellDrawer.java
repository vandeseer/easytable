package org.vandeseer.easytable.drawing.cell;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.DrawingUtil;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;
import org.vandeseer.easytable.util.PdfUtil;

import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TextCellDrawer implements Drawer {

    private TextCell cell;

    public TextCellDrawer() {
    }

    public TextCellDrawer(TextCell cell) {
        this.cell = cell;
    }

    @Override
    public void setCell(AbstractCell cell) {
        this.cell = (TextCell) cell;
    }

    @Override
    public void draw(DrawingContext drawingContext) throws IOException {
        final float startX = drawingContext.getStartingPoint().x;
        final float startY = drawingContext.getStartingPoint().y;

        final PDFont currentFont = cell.getFont();
        final int currentFontSize = cell.getFontSize();
        final Color currentTextColor = cell.getTextColor();

        float maxWidth = cell.getWidthOfText();

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

        float yOffset = startY + yStartRelative;

        final List<String> lines = cell.isWordBreak()
                ? PdfUtil.getOptimalTextBreakLines(cell.getText(), currentFont, currentFontSize, maxWidth)
                : Collections.singletonList(cell.getText());

        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);

            float xOffset = startX + cell.getPaddingLeft();
            yOffset -= (
                    PdfUtil.getFontHeight(currentFont, currentFontSize) // font height
                            + (i > 0 ? PdfUtil.getFontHeight(currentFont, currentFontSize) * cell.getLineSpacing() : 0f) // line spacing
            );

            final float textWidth = PdfUtil.getStringWidth(line, currentFont, currentFontSize);

            // Handle horizontal alignment by adjusting the xOffset
            if (cell.getSettings().getHorizontalAlignment() == HorizontalAlignment.RIGHT) {
                xOffset = startX + (cell.getWidth() - (textWidth + cell.getPaddingRight()));

            } else if (cell.getSettings().getHorizontalAlignment() == HorizontalAlignment.CENTER) {
                final float diff = (cell.getWidth() - textWidth) / 2;
                xOffset = startX + diff;

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
                    drawingContext.getContentStream().setCharacterSpacing(charSpacing);
                }
            }

            drawText(line, currentFont, currentFontSize, currentTextColor, xOffset, yOffset, drawingContext.getContentStream());
        }
    }

    protected void drawText(String text, PDFont font, int fontSize, Color color, float x, float y, PDPageContentStream contentStream) throws IOException {
        DrawingUtil.drawText(text, font, fontSize, color, x, y, contentStream);
    }

}
