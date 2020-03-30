package com.aquent.rambo.easytable.structure.cell.paragraph;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.font.PDFont;

import com.aquent.rambo.easytable.settings.Settings;
import com.aquent.rambo.easytable.util.PdfUtil;

import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.text.FontDescriptor;
import rst.pdfbox.layout.text.NewLine;

import java.awt.*;

@Builder
@Getter
public class StyledText implements ParagraphProcessable {

    @NonNull
    private String text;

    private Float fontSize;

    private PDFont font;

    private Color color;

    @SneakyThrows
    @Override
    public void process(Paragraph paragraph, Settings settings) {
        final Float actualFontSize = getFontSize() != null ? getFontSize() : settings.getFontSize();
        final PDFont actualFont = getFont() != null ? getFont() : settings.getFont();
        final Color actualColor = getColor() != null ? getColor() : settings.getTextColor();

        // Handle new lines properly ...
        String[] lines = getText().split(PdfUtil.NEW_LINE_REGEX);
        for (int i = 0; i < lines.length; i++) {
            paragraph.add(new rst.pdfbox.layout.text.StyledText(lines[i], actualFontSize, actualFont, actualColor));
            if (i < lines.length - 1) {
                paragraph.add(new NewLine(new FontDescriptor(actualFont, actualFontSize)));
            }
        }

    }

}
