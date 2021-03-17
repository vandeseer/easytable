package org.vandeseer.easytable.drawing.cell;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.cell.VerticalTextCell;
import org.vandeseer.easytable.util.PdfUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;
import static org.vandeseer.easytable.settings.HorizontalAlignment.RIGHT;

/**
 * Allows vertical text drawing. Note that this class is still not fully
 * developed, e.g. there is no support for text alignment settings yet.
 */
@NoArgsConstructor
public class VerticalTextCellDrawer extends AbstractCellDrawer<VerticalTextCell> {

    public VerticalTextCellDrawer(VerticalTextCell cell) {
        this.cell = cell;
    }

    /**
     * Does not yet support the settings of alignments.
     *
     * @param drawingContext
     */
    @Override
    @SneakyThrows
    public void drawContent(DrawingContext drawingContext) {
        final float startX = drawingContext.getStartingPoint().x;
        final float startY = drawingContext.getStartingPoint().y;

        final PDFont currentFont = cell.getFont();
        final int currentFontSize = cell.getFontSize();
        final Color currentTextColor = cell.getTextColor();

        float yOffset = startY + cell.getPaddingBottom();

        float height = cell.getRow().getHeight();

        if (cell.getRowSpan() > 1) {
            float rowSpanAdaption = cell.calculateHeightForRowSpan() - cell.getRow().getHeight();
            yOffset -= rowSpanAdaption;
            height = cell.calculateHeightForRowSpan();
        }

        final List<String> lines = cell.isWordBreak()
                ? PdfUtil.getOptimalTextBreakLines(cell.getText(), currentFont, currentFontSize, (height - cell.getVerticalPadding()))
                : Collections.singletonList(cell.getText());

        float textHeight = 0;
        for (String line : lines) {
            float currentHeight = PdfUtil.getStringWidth(line, currentFont, currentFontSize);
            textHeight = currentHeight > textHeight ? currentHeight : textHeight;
        }
        if (cell.isVerticallyAligned(VerticalAlignment.MIDDLE)) {
            yOffset += (height - textHeight - cell.getPaddingTop() - cell.getPaddingBottom()) / 2;
        } else if (cell.isVerticallyAligned(VerticalAlignment.TOP)) {
            yOffset += (height - textHeight - cell.getPaddingTop() - cell.getPaddingBottom());
        }

        float xOffset = startX + cell.getPaddingLeft() - PdfUtil.getFontHeight(currentFont, currentFontSize);

        float textWidth = (PdfUtil.getFontHeight(currentFont, currentFontSize) // font height
                + PdfUtil.getFontHeight(currentFont, currentFontSize) * cell.getLineSpacing()) * lines.size(); // line spacing;

        if (cell.isHorizontallyAligned(CENTER)) {
            xOffset = xOffset + ((cell.getWidth() - cell.getPaddingRight() - cell.getPaddingLeft()) / 2 - textWidth / 2);
        } else if (cell.isHorizontallyAligned(RIGHT)) {
            xOffset = xOffset + cell.getWidth() - cell.getPaddingRight() - cell.getPaddingLeft() - textWidth;
        }

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            xOffset += (
                    PdfUtil.getFontHeight(currentFont, currentFontSize) // font height
                            + (i > 0 ? PdfUtil.getFontHeight(currentFont, currentFontSize) * cell.getLineSpacing() : 0f) // line spacing
            );

            drawText(line, currentFont, currentFontSize, currentTextColor, xOffset, yOffset, drawingContext.getContentStream());
        }
    }

    // TODO this is currently not used!
    @Override
    protected float calculateInnerHeight() {
        return 0;
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
