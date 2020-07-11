package org.vandeseer.easytable.drawing;

import lombok.Builder;
import lombok.Getter;
import org.vandeseer.easytable.settings.BorderStyle;

import java.awt.*;

@Builder(toBuilder = true)
@Getter
public class PositionedLine {

    private float width;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private Color color;
    private Color resetColor;
    private BorderStyle borderStyle;

}
