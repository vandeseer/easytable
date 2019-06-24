package org.vandeseer.easytable;

import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.awt.geom.Point2D;
import java.io.IOException;

@FunctionalInterface
public interface TableDrawerFunction {

    void accept(Point2D.Float start, AbstractCell cell) throws IOException;

}
