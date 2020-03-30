package com.aquent.rambo.easytable.structure.cell;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.font.PDFont;

import com.aquent.rambo.easytable.drawing.Drawer;
import com.aquent.rambo.easytable.drawing.cell.RoboAnnotationImageCellDrawer;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class RoboAnnotationImageCell extends ImageCell {

	private String imageFooterText;
	private int imageFooterTextFontSize;
	private Color imageFooterTextColor;
	private PDFont imageFooterTextFont;

	private String imageText;
	private int imageTextFontSize;
	private Color imageTextColor;
	private PDFont imageTextFont;

	@Builder.Default
	private float imageCircleRadious = 7.0f;
	private Color imageCircleBGColor;
	
	List<AnnotationComment> annotationsComments = new ArrayList<>();
	
	private float originalHeight;
	private float originalWidth;

	@Override
	protected Drawer createDefaultDrawer() {
		return new RoboAnnotationImageCellDrawer(this);
	}

	
}
