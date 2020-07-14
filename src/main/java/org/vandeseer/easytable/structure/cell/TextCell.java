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

    private TextCell superScript;

    public void setText(String text) {
        this.text = text;
    }


    public String getText() {
        return text;
    }


    protected Drawer createDefaultDrawer() {
        return new TextCellDrawer(this);
    }

    public TextCell getSuperScript() {
        return superScript;
    }

    public boolean hasSuperScript() {
            return superScript != null;
    }

}
