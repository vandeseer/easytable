package org.vandeseer.easytable.structure.cell;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.cell.RoboImageAndTextsCellDrawer;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class RoboImageAndTextsCell extends ImageCell {

	private String text1;
	private int textFontSize1;
	private Color textColor1;
	private PDFont textFont1;
	
	private String text2;
	private int textFontSize2;
	private Color textColor2;
	private PDFont textFont2;
	
	@Override
	protected Drawer createDefaultDrawer() {
		return new RoboImageAndTextsCellDrawer(this);
	}

}
