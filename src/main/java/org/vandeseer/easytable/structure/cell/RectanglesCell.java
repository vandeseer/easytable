package org.vandeseer.easytable.structure.cell;

import java.awt.Color;
import java.util.List;

import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.cell.RectanglesCellDrawer;


import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * 
 * @author vtambe
 *
 */
@Getter
@SuperBuilder(toBuilder = true)
public class RectanglesCell extends AbstractCell {

	protected RectanglesCell(AbstractCellBuilder<?, ?> b) {
		super(b);
	}

	public List<RectangleCellDetails> rectangleCellDetails;
    
    private boolean isMultiColumn;

	 @Override
     public float getMinHeight() {
         return (getRow().getHeight() * 80 / 100) + getVerticalPadding();
     }

	@Override
	protected Drawer createDefaultDrawer() {
		return new RectanglesCellDrawer(this);
	}

}
