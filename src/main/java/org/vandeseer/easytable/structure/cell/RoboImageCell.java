package org.vandeseer.easytable.structure.cell;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.cell.RoboImageCellDrawer;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class RoboImageCell extends ImageCell {

	private String imageText;
	private int imageTextFontSize;
	private Color imageTextColor;
	private PDFont imageTextFont;

	@Builder.Default
	private final float imageCircleRadious = 7.0f;
	private Color imageCircleBGColor;

	@Override
	protected Drawer createDefaultDrawer() {
		return new RoboImageCellDrawer(this);
	}

}
