package org.vandeseer.easytable.settings;

import lombok.Builder;
import lombok.Data;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;

@Data
@Builder
public class Settings {

    private PDFont font;
    private Integer fontSize;

    private Color textColor;
    private Color backgroundColor;

    private Color borderColor;

    private HorizontalAlignment horizontalAlignment;
    private VerticalAlignment verticalAlignment;

    private Boolean wordBreak;

    public void fillingMergeBy(Settings settings) {
        if (getFont() == null && settings.getFont() != null) {
            font = settings.getFont();
        }

        if (getFontSize() == null && settings.getFontSize() != null) {
            fontSize = settings.getFontSize();
        }

        if (getTextColor() == null && settings.getTextColor() != null) {
            textColor = settings.getTextColor();
        }

        if (getBackgroundColor() == null && settings.getBackgroundColor() != null) {
            backgroundColor = settings.getBackgroundColor();
        }

        if (getBorderColor() == null && settings.getBorderColor() != null) {
            borderColor = settings.getBorderColor();
        }

        if (getHorizontalAlignment() == null && settings.getHorizontalAlignment() != null) {
            horizontalAlignment = settings.getHorizontalAlignment();
        }

        if (getVerticalAlignment() == null && settings.getVerticalAlignment() != null) {
            verticalAlignment = settings.getVerticalAlignment();
        }

        if (getWordBreak() == null && settings.getWordBreak() != null) {
            wordBreak = settings.getWordBreak();
        }
    }

}
