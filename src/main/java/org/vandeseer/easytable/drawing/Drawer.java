package org.vandeseer.easytable.drawing;

import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.io.IOException;

public interface Drawer {

    void setCell(AbstractCell cell);

    void draw(DrawingContext drawingContext) throws IOException;

}
