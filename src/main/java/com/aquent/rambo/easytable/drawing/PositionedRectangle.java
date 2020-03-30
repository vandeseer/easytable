package com.aquent.rambo.easytable.drawing;

import lombok.Builder;
import lombok.Getter;

import java.awt.*;

@Builder(toBuilder = true)
@Getter
public class PositionedRectangle {

    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final Color color;

}
