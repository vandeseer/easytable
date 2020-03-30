package com.aquent.rambo.easytable.drawing.cell;

import java.awt.Color;
import java.awt.geom.Point2D;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.aquent.rambo.easytable.drawing.DrawingContext;
import com.aquent.rambo.easytable.drawing.DrawingUtil;
import com.aquent.rambo.easytable.drawing.PositionedStyledText;
import com.aquent.rambo.easytable.settings.HorizontalAlignment;
import com.aquent.rambo.easytable.structure.cell.RoboImageAndTextsCell;
import com.aquent.rambo.easytable.structure.cell.RoboImageCell;
import com.aquent.rambo.easytable.util.PdfUtil;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor
public class RoboImageAndTextsCellDrawer extends AbstractCellDrawer<RoboImageAndTextsCell> {

    public RoboImageAndTextsCellDrawer(RoboImageAndTextsCell cell) {
        this.cell = cell;
    }

    @Override
    @SneakyThrows
    public void drawContent(DrawingContext drawingContext) {
        final PDPageContentStream contentStream = drawingContext.getContentStream();
        final float moveX = drawingContext.getStartingPoint().x;

        final Point2D.Float size = cell.getFitSize();
        final Point2D.Float drawAt = new Point2D.Float();

        // Handle horizontal alignment by adjusting the xOffset
        float xOffset = moveX + cell.getPaddingLeft();
        if (cell.getSettings().getHorizontalAlignment() == HorizontalAlignment.RIGHT) {
            xOffset = moveX + (cell.getWidth() - (size.x + cell.getPaddingRight()));

        } else if (cell.getSettings().getHorizontalAlignment() == HorizontalAlignment.CENTER) {
            final float diff = (cell.getWidth() - size.x) / 2;
            xOffset = moveX + diff;

        }
        
        float startY = drawingContext.getStartingPoint().y + getAdaptionForVerticalAlignment() - (cell.getPaddingTop() + 2f);
        
        int fontSize1 = cell.getTextFontSize1() > 0 ? cell.getTextFontSize1() : 10;
        Color imageTextColor1 = cell.getTextColor1() != null ? cell.getTextColor1() : Color.BLACK;
        PDFont imageTextFont1 = cell.getTextFont1() != null ? cell.getTextFont1() : PDType1Font.HELVETICA;
        String imageText1 = cell.getText1() != null ? cell.getText1(): "";
        
        if(imageText1 != null && imageText1.length() > 0) {
        	imageText1 = imageText1.length() == 1 ? " "+imageText1 : imageText1;
        	DrawingUtil.drawText(contentStream, PositionedStyledText.builder().x(xOffset).y(startY).font(imageTextFont1).fontSize(fontSize1).color(imageTextColor1).text(imageText1).build());
        	 final float textWidth = PdfUtil.getStringWidth(imageText1, imageTextFont1, fontSize1);
        	 xOffset = xOffset + textWidth + 5;
        }
        
        drawAt.x = xOffset;
        drawAt.y = startY - 2;
        if(cell.getImage() != null) {
        	contentStream.drawImage(cell.getImage(), drawAt.x, drawAt.y, size.x, size.y);
        }
        xOffset = xOffset + size.x + 5;
        
        int fontSize2 = cell.getTextFontSize2() > 0 ? cell.getTextFontSize2() : 10;
        Color imageTextColor2 = cell.getTextColor2() != null ? cell.getTextColor2() : Color.BLACK;
        PDFont imageTextFont2 = cell.getTextFont2() != null ? cell.getTextFont2() : PDType1Font.HELVETICA;
        String imageText2 = cell.getText2() != null ? cell.getText2(): "";
        
        if(imageText2 != null && imageText2.length() > 0) {
        	imageText2 = imageText2.length() == 1 ? " "+imageText2 : imageText2;
        	DrawingUtil.drawText(contentStream, PositionedStyledText.builder().x(xOffset).y(startY).font(imageTextFont2).fontSize(fontSize2).color(imageTextColor2).text(imageText2).build());
        }
        
        
       
        
    }

    @Override
    protected float calculateInnerHeight() {
        return (float) cell.getFitSize().getY();
    }

}
