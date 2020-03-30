package com.aquent.rambo.easytable.settings;

import lombok.*;
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

    // We use a boxed Boolean internally in order to be able to model the absence of a value.
    // For callers outside it should expose only the primitive though.
    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.NONE)
    private Boolean wordBreak;

    public boolean isWordBreak() {
        return wordBreak != null && wordBreak;
    }

    public void setWordBreak(boolean wordBreak) {
        this.wordBreak = wordBreak;
    }

    public void fillingMergeBy(Settings settings) {
        fillingMergeFontSettings(settings);
        fillingMergeColorSettings(settings);
        fillingMergeAlignmentSettings(settings);
        fillingMergeWordBreakSetting(settings);
    }

    private void fillingMergeWordBreakSetting(Settings settings) {
        // Note that we use the boxed Boolean only here internally!
        if (wordBreak == null && settings.wordBreak != null) {
            wordBreak = settings.getWordBreak();
        }
    }

    private void fillingMergeColorSettings(Settings settings) {
        if (getTextColor() == null && settings.getTextColor() != null) {
            textColor = settings.getTextColor();
        }

        if (getBackgroundColor() == null && settings.getBackgroundColor() != null) {
            backgroundColor = settings.getBackgroundColor();
        }

        if (getBorderColor() == null && settings.getBorderColor() != null) {
            borderColor = settings.getBorderColor();
        }
    }

    private void fillingMergeAlignmentSettings(Settings settings) {
        if (getHorizontalAlignment() == null && settings.getHorizontalAlignment() != null) {
            horizontalAlignment = settings.getHorizontalAlignment();
        }

        if (getVerticalAlignment() == null && settings.getVerticalAlignment() != null) {
            verticalAlignment = settings.getVerticalAlignment();
        }
    }

    private void fillingMergeFontSettings(Settings settings) {
        if (getFont() == null && settings.getFont() != null) {
            font = settings.getFont();
        }

        if (getFontSize() == null && settings.getFontSize() != null) {
            fontSize = settings.getFontSize();
        }
    }

}
