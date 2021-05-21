package org.vandeseer.easytable.drawing;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;
import java.io.IOException;

import static org.vandeseer.easytable.settings.BorderStyle.SOLID;
import static org.vandeseer.easytable.util.PdfUtil.*;

public class DrawingUtil {

    private DrawingUtil() {
    }

    public static void drawText(PDPageContentStream contentStream, PositionedStyledText styledText) throws IOException {
        contentStream.beginText();
        contentStream.setNonStrokingColor(styledText.getColor());
        contentStream.setFont(styledText.getFont(), styledText.getFontSize());
        contentStream.newLineAtOffset(styledText.getX(), styledText.getY());
        contentStream.showText(styledText.getText());
        contentStream.endText();
        contentStream.setCharacterSpacing(0);
    }

    public static void drawExtendedText(PDPageContentStream contentStream, PositionedStyledText styledText) throws IOException
    {
        char[] text = styledText.getText().toCharArray();
        boolean isSuper = false;
        boolean isSub = false;

        float fontRaisePercentage = 1/3f;
        float fontLowerPercentage = 0.05f;

        contentStream.beginText();
        contentStream.setNonStrokingColor(styledText.getColor());
        contentStream.setFont(styledText.getFont(), styledText.getFontSize());
        contentStream.newLineAtOffset(styledText.getX(), styledText.getY());

        for (char curr : text)
        {
            if(isSuperScript(curr))
            {
                isSuper = true;
                continue;
            }
            else if (isSubScript(curr))
            {
                isSub = true;
                continue;
            }

            if(isSuper)
            {
                contentStream.setFont(styledText.getFont(), styledText.getFontSize() * subSuperScriptFontRatio);
                contentStream.setTextRise(styledText.getFontSize() * fontRaisePercentage);

                isSuper = false;
            }
            else if (isSub)
            {
                contentStream.setFont(styledText.getFont(), styledText.getFontSize() * subSuperScriptFontRatio);
                contentStream.setTextRise(-styledText.getFontSize() * fontLowerPercentage);

                isSub = false;
            }
            else
            {
                contentStream.setFont(styledText.getFont(), styledText.getFontSize());
                contentStream.setTextRise(0);
            }

            contentStream.showText(curr + "");
        }

        contentStream.endText();
        contentStream.setTextRise(0);
        contentStream.setCharacterSpacing(0);
    }

    public static void drawLine(PDPageContentStream contentStream, PositionedLine line) throws IOException {
        contentStream.moveTo(line.getStartX(), line.getStartY());
        contentStream.setLineWidth(line.getWidth());
        contentStream.lineTo(line.getEndX(), line.getEndY());
        contentStream.setStrokingColor(line.getColor());
        contentStream.setLineDashPattern(line.getBorderStyle().getPattern(), line.getBorderStyle().getPhase());
        contentStream.stroke();
        contentStream.setStrokingColor(line.getResetColor());
        contentStream.setLineDashPattern(SOLID.getPattern(), SOLID.getPhase());
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
