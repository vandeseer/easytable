package org.vandeseer.easytable.drawing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.geom.Point2D;

@Getter
@AllArgsConstructor
public class DrawingContext {

     private final PDPageContentStream contentStream;

     private final PDPage page;

     private final Point2D.Float startingPoint;

}
