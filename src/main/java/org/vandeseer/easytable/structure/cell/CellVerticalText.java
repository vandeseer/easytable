package org.vandeseer.easytable.structure.cell;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.vandeseer.easytable.drawing.CellVerticalTextDrawer;
import org.vandeseer.easytable.drawing.Drawer;

@Getter
@SuperBuilder(toBuilder = true)
public class CellVerticalText extends AbstractTextCell {

    @Builder.Default
    private float minHeight = 100f;

    @Override
    public float getHeight() {
        assertIsRendered();

        return getRowSpan() > 1
                ? calculateHeightForRowSpan()
                : getMinHeight();
    }

    protected Drawer createDefaultDrawer() {
        return new CellVerticalTextDrawer(this);
    }

}