package org.vandeseer.easytable.structure.cell.paragraph;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.Settings;
import rst.pdfbox.layout.elements.Paragraph;

@Builder
@Getter
public class StyledText implements ParagraphProcessable {

    @NonNull
    private String text;

    private Float fontSize;

    private PDFont font;

    @SneakyThrows
    @Override
    public void process(Paragraph paragraph, Settings settings) {
        final Float fontSize = getFontSize() != null ? getFontSize() : settings.getFontSize();
        final PDFont font = getFont() != null ? getFont() : settings.getFont();
        paragraph.addText(getText(), fontSize, font);
    }

}
