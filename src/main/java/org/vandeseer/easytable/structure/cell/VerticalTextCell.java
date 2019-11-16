package org.vandeseer.easytable.structure.cell;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.cell.VerticalTextCellDrawer;

@Getter
@SuperBuilder(toBuilder = true)
public class VerticalTextCell extends AbstractTextCell {

    @NonNull
    private String text;

    protected Drawer createDefaultDrawer() {
        return new VerticalTextCellDrawer(this);
    }

}
