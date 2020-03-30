package com.aquent.rambo.easytable.structure.cell.paragraph;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.text.BaseFont;

import java.util.EnumMap;
import java.util.Map;

import com.aquent.rambo.easytable.settings.Settings;

@Builder
@Getter
public class Markup implements ParagraphProcessable {

    public enum MarkupSupportedFont {
        TIMES, COURIER, HELVETICA
    }

    public static final Map<MarkupSupportedFont, BaseFont> FONT_MAP = new EnumMap<>(Markup.MarkupSupportedFont.class);
    static {
        FONT_MAP.put(Markup.MarkupSupportedFont.HELVETICA, BaseFont.Helvetica);
        FONT_MAP.put(Markup.MarkupSupportedFont.COURIER, BaseFont.Courier);
        FONT_MAP.put(Markup.MarkupSupportedFont.TIMES, BaseFont.Times);
    }

    @NonNull
    private String markup;

    @NonNull
    private MarkupSupportedFont font;

    private Float fontSize;

    @SneakyThrows
    @Override
    public void process(Paragraph paragraph, Settings settings) {
        final Float fontSize = getFontSize() != null ? getFontSize() : settings.getFontSize();
        paragraph.addMarkup(getMarkup(), fontSize, FONT_MAP.get(getFont()));
    }

}
