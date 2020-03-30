package com.aquent.rambo.easytable.drawing;

import lombok.Builder;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;

@Builder(toBuilder = true)
@Getter
public class PositionedStyledText {

    private final float x;
    private final float y;
    private final String text;
    private final PDFont font;
    private final int fontSize;
    private final Color color;

}
