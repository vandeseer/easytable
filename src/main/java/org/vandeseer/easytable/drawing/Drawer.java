package org.vandeseer.easytable.drawing;

public interface Drawer {

    void drawContent(DrawingContext drawingContext);

    void drawBackground(DrawingContext drawingContext);

    void drawBorders(DrawingContext drawingContext);

}
