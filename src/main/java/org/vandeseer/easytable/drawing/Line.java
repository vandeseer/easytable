package org.vandeseer.easytable.drawing;

import lombok.Builder;
import lombok.Getter;

import java.awt.*;

@Builder
@Getter
public class Line {

    private float width;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private Color color;
    private Color resetColor;

}
