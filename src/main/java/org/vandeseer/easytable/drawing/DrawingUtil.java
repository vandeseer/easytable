package org.vandeseer.easytable.drawing;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.IOException;

public class DrawingUtil {

    private DrawingUtil() {
    }

    public static void drawText(PDPageContentStream contentStream, float x, float y, String text, PDFont font, int fontSize, Color color)
            throws IOException {
        contentStream.beginText();
        contentStream.setNonStrokingColor(color);
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
        contentStream.setCharacterSpacing(0);
    }

    public static void drawLine(PDPageContentStream contentStream, float width, float toX, float toY, Color color) throws IOException {
        contentStream.setLineWidth(width);
        contentStream.lineTo(toX, toY);
        contentStream.setStrokingColor(color);
        contentStream.stroke();
    }

    public static void drawRectangle(PDPageContentStream contentStream, float x, float y, final float height, float width, Color color)
            throws IOException {
        contentStream.setNonStrokingColor(color);

        contentStream.addRect(x, y, width, height);
        contentStream.fill();
        contentStream.closePath();

        // Reset NonStrokingColor to default value
        contentStream.setNonStrokingColor(Color.BLACK);
    }
}
