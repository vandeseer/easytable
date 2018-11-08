package org.vandeseer.easytable.settings;

import lombok.Builder;
import lombok.Data;
import org.apache.pdfbox.pdmodel.font.PDFont;

@Data
@Builder
public class FontSettings {

    private PDFont font;
    private Integer fontSize;

    public void fillingMergeBy(FontSettings fontSettings) {
        if (getFont() == null && fontSettings.getFont() != null) {
            font = fontSettings.getFont();
        }

        if (getFontSize() == null && fontSettings.getFontSize() != null) {
            fontSize = fontSettings.getFontSize();
        }
    }

}
