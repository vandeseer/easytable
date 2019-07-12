package org.vandeseer.easytable.drawing;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.CellVerticalText;
import org.vandeseer.easytable.util.PdfUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class CellVerticalTextDrawer implements Drawer {

    private CellVerticalText cell;

    public CellVerticalTextDrawer() {
    }

    public CellVerticalTextDrawer(CellVerticalText cell) {
        this.cell = cell;
    }

    @Override
    public void setCell(AbstractCell cell) {
        this.cell = (CellVerticalText) cell;
    }

    // As of now: No support for Alignments ...
    // Only Left, Bottom is supported.
    // Need to think about it ;)
    @Override
    public void draw(DrawingContext drawingContext) throws IOException {
        final float startX = drawingContext.getStartingPoint().x;
        final float startY = drawingContext.getStartingPoint().y;

        final PDFont currentFont = cell.getFont();
        final int currentFontSize = cell.getFontSize();
        final Color currentTextColor = cell.getTextColor();

        float yOffset = startY + cell.getPaddingBottom();

        final List<String> lines = cell.isWordBreak()
                ? PdfUtil.getOptimalTextBreakLines(cell.getText(), currentFont, currentFontSize, (cell.getRow().getHeight() - cell.getVerticalPadding()))
                : Collections.singletonList(cell.getText());

        float xOffset = startX + cell.getPaddingLeft() - PdfUtil.getFontHeight(currentFont, currentFontSize);
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);

            xOffset += (
                    PdfUtil.getFontHeight(currentFont, currentFontSize) // font height
                            + (i > 0 ? PdfUtil.getFontHeight(currentFont, currentFontSize) * cell.getLineSpacing() : 0f) // line spacing
            );

            drawText(line, currentFont, currentFontSize, currentTextColor, xOffset, yOffset, drawingContext.getContentStream());
        }
    }


    protected void drawText(String text, PDFont font, int fontSize, Color color, float x, float y, PDPageContentStream contentStream) throws IOException {
        // Rotate by 90 degrees counter clockwise
        final AffineTransform transform = AffineTransform.getTranslateInstance(x, y);
        transform.concatenate(AffineTransform.getRotateInstance(Math.PI * 0.5));
        transform.concatenate(AffineTransform.getTranslateInstance(-x, -y - fontSize));

        contentStream.moveTo(x, y);
        contentStream.beginText();

        // Do the transformation :)
        contentStream.setTextMatrix(transform);

        contentStream.setNonStrokingColor(color);
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
        contentStream.setCharacterSpacing(0);
    }

}
