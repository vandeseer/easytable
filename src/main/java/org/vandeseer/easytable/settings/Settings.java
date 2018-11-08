package org.vandeseer.easytable.settings;

import lombok.Builder;
import lombok.Data;
import org.apache.pdfbox.pdmodel.font.PDFont;

@Data
@Builder
public class Settings {

    private PDFont font;
    private Integer fontSize;

    private HorizontalAlignment horizontalAlignment;
    private VerticalAlignment verticalAlignment;

    public void fillingMergeBy(Settings settings) {
        if (getFont() == null && settings.getFont() != null) {
            font = settings.getFont();
        }

        if (getFontSize() == null && settings.getFontSize() != null) {
            fontSize = settings.getFontSize();
        }

        if (getHorizontalAlignment() == null && settings.getHorizontalAlignment() != null) {
            horizontalAlignment = settings.getHorizontalAlignment();
        }

        if (getVerticalAlignment() == null && settings.getVerticalAlignment() != null) {
            verticalAlignment = settings.getVerticalAlignment();
        }
    }

}
