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

    private HorizontalAlignment horizontalAlignment;
    private VerticalAlignment verticalAlignment;

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

        if (getHorizontalAlignment() == null && settings.getHorizontalAlignment() != null) {
            horizontalAlignment = settings.getHorizontalAlignment();
        }

        if (getVerticalAlignment() == null && settings.getVerticalAlignment() != null) {
            verticalAlignment = settings.getVerticalAlignment();
        }
    }

}
