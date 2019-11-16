package org.vandeseer.easytable.structure.cell;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.cell.TextCellDrawer;

@Getter
@SuperBuilder(toBuilder = true)
public class TextCell extends AbstractTextCell {

    @NonNull
    protected String text;

    protected Drawer createDefaultDrawer() {
        return new TextCellDrawer(this);
    }

}
