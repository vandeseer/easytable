package com.aquent.rambo.easytable.structure.cell;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import com.aquent.rambo.easytable.drawing.Drawer;
import com.aquent.rambo.easytable.drawing.cell.VerticalTextCellDrawer;

@Getter
@SuperBuilder(toBuilder = true)
public class VerticalTextCell extends AbstractTextCell {

    @NonNull
    private String text;

    protected Drawer createDefaultDrawer() {
        return new VerticalTextCellDrawer(this);
    }

}
