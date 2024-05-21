package org.vandeseer.easytable.drawing.cell;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.DrawingUtil;
import org.vandeseer.easytable.drawing.PositionedStyledText;
import org.vandeseer.easytable.structure.cell.AbstractTextCell;
import org.vandeseer.easytable.util.PdfUtil;

import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.vandeseer.easytable.settings.HorizontalAlignment.*;


@NoArgsConstructor
public class ExtendedTextCellDrawer<T extends AbstractTextCell> extends TextCellDrawer<T>
{
    public ExtendedTextCellDrawer(T cell)
    {
        this.cell = cell;
    }

    @Override
    @SneakyThrows
    public void drawContent(DrawingContext drawingContext) {
        final float startX = drawingContext.getStartingPoint().x;

        final PDFont currentFont = cell.getFont();
        final int currentFontSize = cell.getFontSize();
        final Color currentTextColor = cell.getTextColor();

        float yOffset = drawingContext.getStartingPoint().y + getAdaptionForVerticalAlignment();
        float xOffset = startX + cell.getPaddingLeft();

        final List<String> lines = calculateAndGetLines(currentFont, currentFontSize, cell.getMaxWidth());
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);

            yOffset -= calculateYOffset(currentFont, currentFontSize, i);

            final float textWidth = PdfUtil.getExtendedStringWidth(line, currentFont, currentFontSize);

            // Handle horizontal alignment by adjusting the xOffset
            if (cell.isHorizontallyAligned(RIGHT)) {
                xOffset = startX + (cell.getWidth() - (textWidth + cell.getPaddingRight()));

            } else if (cell.isHorizontallyAligned(CENTER)) {
                xOffset = startX + (cell.getWidth() - textWidth) / 2;

            } else if (cell.isHorizontallyAligned(JUSTIFY) && isNotLastLine(lines, i)) {
                drawingContext.getContentStream().setCharacterSpacing(calculateCharSpacingFor(line));
            }

            drawText(
                    drawingContext,
                    PositionedStyledText.builder()
                            .x(xOffset)
                            .y(yOffset)
                            .text(line)
                            .font(currentFont)
                            .fontSize(currentFontSize)
                            .color(currentTextColor)
                            .build()
            );
        }
    }

    @Override
    protected void drawText(DrawingContext drawingContext, PositionedStyledText positionedStyledText) throws IOException
    {
        DrawingUtil.drawExtendedText(
                drawingContext.getContentStream(),
                positionedStyledText
        );
    }

    @Override
    protected List<String> calculateAndGetLines(PDFont currentFont, int currentFontSize, float maxWidth)
    {
        return cell.isWordBreak()
                ? PdfUtil.getOptimalExtendedTextBreakLines(cell.getText(), currentFont, currentFontSize, maxWidth)
                : Collections.singletonList(cell.getText());
    }
}
