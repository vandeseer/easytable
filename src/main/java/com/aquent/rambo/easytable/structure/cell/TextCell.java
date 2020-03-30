package com.aquent.rambo.easytable.structure.cell;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import com.aquent.rambo.easytable.drawing.Drawer;
import com.aquent.rambo.easytable.drawing.cell.TextCellDrawer;

@Getter
@SuperBuilder(toBuilder = true)
public class TextCell extends AbstractTextCell {

    @NonNull
    protected String text;

    protected Drawer createDefaultDrawer() {
        return new TextCellDrawer(this);
    }

}
