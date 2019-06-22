package org.vandeseer.easytable;

import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.CellBaseData;

import java.awt.geom.Point2D;
import java.io.IOException;

@FunctionalInterface
public interface TableDrawerFunction {

    void accept(Point2D.Float start, Row row, CellBaseData cell) throws IOException;

}
