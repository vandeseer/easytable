package org.vandeseer.easytable.drawing.cell;

import java.awt.geom.Point2D;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.DrawingUtil;
import org.vandeseer.easytable.drawing.PositionedRectangle;
import org.vandeseer.easytable.structure.cell.RectanglesCell;

import lombok.SneakyThrows;
/**
 * 
 * @author vtambe
 *
 */
public class RectanglesCellDrawer extends AbstractCellDrawer<RectanglesCell> {
	
	public RectanglesCellDrawer(RectanglesCell rectanglesCell) {
		this.cell = rectanglesCell;
	}

	 @Override
     protected float calculateInnerHeight() {
         return cell.getRow().getHeight() * 80 / 100;
     }
	
	 @SneakyThrows
     @Override
     public void drawContent(DrawingContext drawingContext) {
         final PDPageContentStream contentStream = drawingContext.getContentStream();
         final Point2D.Float start = drawingContext.getStartingPoint();

         final float rowHeight = cell.getRow().getHeight();
         float startY = rowHeight < cell.getHeight() ? start.y + rowHeight - cell.getHeight() : start.y;

         final float calculatedRectangleHeight = cell.getHeight() * 80/ 100;
         float rectHeight = calculatedRectangleHeight * cell.color1Percentage;
         startY = startY + cell.getPaddingBottom();
         System.out.println("RectHeight 1 :" + rectHeight + " startY1 :" + startY);
         float totalRectHeight = rectHeight;
         // Actual
         DrawingUtil.drawRectangle(contentStream,
                 PositionedRectangle.builder()
                         .x(start.x + cell.getPaddingLeft())
                         .y(startY)
                         .width(cell.getWidth() - cell.getHorizontalPadding())
                         .height(rectHeight)
                         .color(cell.getColor1Color()).build()
         );

         // Unused
         if(cell.color2Percentage > 0f) {
         	startY = startY + rectHeight ; //cell.getPaddingBottom() + RectanglesCell.RECTANGLE_HEIGHT * cell.color1Percentage;
         	rectHeight = calculatedRectangleHeight * cell.color2Percentage;
         	
         	System.out.println("RectHeight 2 :" + rectHeight + " startY2 :" + startY);
         	totalRectHeight += rectHeight;
         	DrawingUtil.drawRectangle(contentStream,
                     PositionedRectangle.builder()
                             .x(start.x + cell.getPaddingLeft())
                             .y(startY)
                             .width(cell.getWidth() - cell.getHorizontalPadding())
                             .height(rectHeight)
                             .color(cell.getColor2Color()).build()
             );
         }
         if(cell.color3Percentage > 0f) {
         	startY = startY + rectHeight;//cell.getPaddingBottom() + RectanglesCell.RECTANGLE_HEIGHT * cell.color2Percentage;
         	rectHeight = calculatedRectangleHeight * cell.color3Percentage;
         	
         	System.out.println("RectHeight 3 :" + rectHeight + " startY3 :" + startY);
         	totalRectHeight += rectHeight;
         	 DrawingUtil.drawRectangle(contentStream,
                      PositionedRectangle.builder()
                              .x(start.x + cell.getPaddingLeft())
                              .y(startY)
                              .width(cell.getWidth() - cell.getHorizontalPadding())
                              .height(rectHeight)
                              .color(cell.getColor3Color()).build()
              );
         }
         System.out.println("Total Rect Height :" + totalRectHeight);
        
     }

	

}
