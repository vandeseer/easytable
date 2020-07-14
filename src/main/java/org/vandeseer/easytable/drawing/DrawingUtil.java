package org.vandeseer.easytable.drawing;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;
import java.io.IOException;

public class DrawingUtil {

    private DrawingUtil() {
    }

    public static void drawText(PDPageContentStream contentStream, PositionedStyledText styledText) throws IOException {
        contentStream.beginText();
        contentStream.setTextRise(styledText.getTextRise());
        contentStream.setNonStrokingColor(styledText.getColor());
        contentStream.setFont(styledText.getFont(), styledText.getFontSize());
        contentStream.newLineAtOffset(styledText.getX(), styledText.getY());
        contentStream.showText(styledText.getText());
        contentStream.endText();
        contentStream.setCharacterSpacing(0);
    }

    public static void drawLine(PDPageContentStream contentStream, PositionedLine line) throws IOException {
        contentStream.moveTo(line.getStartX(), line.getStartY());
        contentStream.setLineWidth(line.getWidth());
        contentStream.lineTo(line.getEndX(), line.getEndY());
        contentStream.setStrokingColor(line.getColor());
        contentStream.stroke();
        contentStream.setStrokingColor(line.getResetColor());
    }

    public static void drawRectangle(PDPageContentStream contentStream, PositionedRectangle rectangle)
            throws IOException {
        contentStream.setNonStrokingColor(rectangle.getColor());

        contentStream.addRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        contentStream.fill();

        // Reset NonStrokingColor to default value
        contentStream.setNonStrokingColor(Color.BLACK);
    }
}
