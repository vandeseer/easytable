package org.vandeseer.easytable.structure.cell;

import lombok.experimental.SuperBuilder;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.cell.VerticalTextCellDrawer;

@SuperBuilder
public class VerticalTextCell extends AbstractTextCell {

    protected Drawer createDefaultDrawer() {
        return new VerticalTextCellDrawer(this);
    }

}
