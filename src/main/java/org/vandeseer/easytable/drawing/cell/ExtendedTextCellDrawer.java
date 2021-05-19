package org.vandeseer.easytable.drawing.cell;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.structure.cell.AbstractTextCell;

@NoArgsConstructor
public class ExtendedTextCellDrawer <T extends AbstractTextCell> extends AbstractCellDrawer<AbstractTextCell>
{
    public ExtendedTextCellDrawer(T cell)
    {
        this.cell = cell;
    }

    @Override
    @SneakyThrows
    public void drawContent(DrawingContext drawingContext)
    {

    }

    @Override
    protected float calculateInnerHeight()
    {
        return cell.getTextHeight();
    }
}
