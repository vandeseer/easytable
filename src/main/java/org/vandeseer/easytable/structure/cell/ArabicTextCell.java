package org.vandeseer.easytable.structure.cell;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.cell.ArabicTextCellDrawer;

@Getter
@SuperBuilder(toBuilder = true)
public class ArabicTextCell extends AbstractTextCell {

    @NonNull
    private String text;

    protected Drawer createDefaultDrawer() {
        return new ArabicTextCellDrawer(this);
    }

}
