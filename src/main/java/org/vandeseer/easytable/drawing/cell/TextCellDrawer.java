package org.vandeseer.easytable.drawing.cell;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.DrawingUtil;
import org.vandeseer.easytable.drawing.PositionedStyledText;
import org.vandeseer.easytable.structure.Text;
import org.vandeseer.easytable.structure.cell.AbstractTextCell;
import org.vandeseer.easytable.util.PdfUtil;

import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.vandeseer.easytable.settings.HorizontalAlignment.*;

@NoArgsConstructor
public class TextCellDrawer<T extends AbstractTextCell> extends AbstractCellDrawer<AbstractTextCell> {

    public TextCellDrawer(T cell) {
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

        final List<List<Text>> lines = calculateAndGetLines(currentFont, currentFontSize, cell.getMaxWidth());
        for (int i = 0; i < lines.size(); i++) {
            final List<Text> line = lines.get(i);

            yOffset -= calculateYOffset(currentFont, currentFontSize, i);

            final float textWidth = PdfUtil.getStringWidth(line.stream().map(Text::getText).collect(Collectors.joining()), currentFont, currentFontSize);

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
    protected float calculateInnerHeight() {
        return cell.getTextHeight();
    }


    private float calculateYOffset(PDFont currentFont, int currentFontSize, int lineIndex) {
        return PdfUtil.getFontHeight(currentFont, currentFontSize) // font height
                + (lineIndex > 0 ? PdfUtil.getFontHeight(currentFont, currentFontSize) * cell.getLineSpacing() : 0f); // line spacing
    }

    static boolean isNotLastLine(List<List<Text>> lines, int i) {
        return i != lines.size() - 1;
    }

    // Code from https://stackoverflow.com/questions/20680430/is-it-possible-to-justify-text-in-pdfbox
    protected float calculateCharSpacingFor(List<Text> line) {
        float charSpacing = 0;
        int joinedLineLength = line.stream().map(Text::getText).collect(Collectors.joining()).length();
        if (joinedLineLength > 1) {
            float size = PdfUtil.getStringWidth(line.stream().map(Text::getText).collect(Collectors.joining()), cell.getFont(), cell.getFontSize());
            float free = cell.getWidthOfText() - size;
            if (free > 0) {
                charSpacing = free / (joinedLineLength - 1);
            }
        }
        return charSpacing;
    }

    protected List<List<Text>> calculateAndGetLines(PDFont currentFont, int currentFontSize, float maxWidth) {
        return cell.isWordBreak()
                ? PdfUtil.getOptimalTextBreakLines(cell.getText(), currentFont, currentFontSize, maxWidth)
                : Collections.singletonList(cell.getText());
    }

    protected void drawText(DrawingContext drawingContext, PositionedStyledText positionedStyledText) throws IOException {
        DrawingUtil.drawText(
                drawingContext.getContentStream(),
                positionedStyledText
        );
    }

}
