package com.aquent.rambo.easytable.drawing;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class DrawingUtil {

    private DrawingUtil() {
    }

    /*public static void drawText(PDPageContentStream contentStream, PositionedStyledText styledText) throws IOException {
        contentStream.beginText();
        contentStream.setNonStrokingColor(styledText.getColor());
        contentStream.setFont(styledText.getFont(), styledText.getFontSize());
        contentStream.newLineAtOffset(styledText.getX(), styledText.getY());
        contentStream.showText(styledText.getText());
        contentStream.endText();
        contentStream.setCharacterSpacing(0);
    }*/
    
    public static void drawText(PDPageContentStream contentStream, PositionedStyledText styledText) throws IOException {
        contentStream.beginText();
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
        contentStream.closePath();

        // Reset NonStrokingColor to default value
        contentStream.setNonStrokingColor(Color.BLACK);
    }
    
    public static void drawCircle(PDPageContentStream contentStream, float cx, float cy, float r, Color rgb) throws IOException {
    	final float k = 0.552284749831f;
    	contentStream.setNonStrokingColor(rgb);
    	contentStream.moveTo(cx - r, cy);
        contentStream.curveTo(cx - r, cy + k * r, cx - k * r, cy + r, cx, cy + r);
        contentStream.curveTo(cx + k * r, cy + r, cx + r, cy + k * r, cx + r, cy);
        contentStream.curveTo(cx + r, cy - k * r, cx + k * r, cy - r, cx, cy - r);
        contentStream.curveTo(cx - k * r, cy - r, cx - r, cy - k * r, cx - r, cy);
        contentStream.fill();
     // Reset NonStrokingColor to default value
        contentStream.setNonStrokingColor(Color.BLACK);
    }
}
