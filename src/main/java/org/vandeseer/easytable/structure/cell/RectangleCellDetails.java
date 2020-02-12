package org.vandeseer.easytable.structure.cell;

import java.awt.Color;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@AllArgsConstructor
public class RectangleCellDetails {

	 private float color1Percentage;
     private Color color1Color;

     private float color2Percentage;
     private Color color2Color;
     
     private float color3Percentage;
     private Color color3Color;
     
     private String cellText;
     private int textFontSize;
     private Color cellTextColor;
}
