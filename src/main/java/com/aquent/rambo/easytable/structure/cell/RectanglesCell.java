package com.aquent.rambo.easytable.structure.cell;

import java.awt.Color;
import java.util.List;

import com.aquent.rambo.easytable.drawing.Drawer;
import com.aquent.rambo.easytable.drawing.cell.RectanglesCellDrawer;

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

	private List<RectangleCellDetails> rectangleCellDetails;
    
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
