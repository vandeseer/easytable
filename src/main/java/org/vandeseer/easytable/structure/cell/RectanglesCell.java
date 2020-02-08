package org.vandeseer.easytable.structure.cell;

import java.awt.Color;

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

	public float color1Percentage;
	public Color color1Color;

	public float color2Percentage;
	public Color color2Color;

	public float color3Percentage;
	public Color color3Color;

	 @Override
     public float getMinHeight() {
         return (getRow().getHeight() * 80 / 100) + getVerticalPadding();
     }

	@Override
	protected Drawer createDefaultDrawer() {
		return new RectanglesCellDrawer(this);
	}

}
