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
import com.aquent.rambo.easytable.structure.cell.RoboImageCell;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor
public class RoboImageCellDrawer extends AbstractCellDrawer<RoboImageCell> {

    public RoboImageCellDrawer(RoboImageCell cell) {
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

        drawAt.x = xOffset;
        drawAt.y = drawingContext.getStartingPoint().y + getAdaptionForVerticalAlignment() - size.y;

        contentStream.drawImage(cell.getImage(), drawAt.x, drawAt.y, size.x, size.y);
        
        float textStartX = drawAt.x - cell.getPaddingLeft() + 2f;
        float startY = drawAt.y + (size.y - cell.getPaddingTop() - 4f);
        int fontSize = cell.getImageTextFontSize() > 0 ? cell.getImageTextFontSize() : 10;
        Color imageTextColor = cell.getImageTextColor() != null ? cell.getImageTextColor() : Color.WHITE;
        PDFont imageTextFont = cell.getImageTextFont() != null ? cell.getImageTextFont() : PDType1Font.HELVETICA;
        String imageText = cell.getImageText() != null ? cell.getImageText(): "";
        
        float cx = textStartX + 5.5f;
        float cy = startY + 3.0f;
        float r = cell.getImageCircleRadious();
        Color imageBGColor = cell.getImageCircleBGColor() != null ? cell.getImageCircleBGColor() : Color.RED;
        if(imageText != null && imageText.length() > 0) {
        	imageText = imageText.length() == 1 ? " "+imageText:imageText;
        	DrawingUtil.drawCircle(contentStream, cx, cy, r, imageBGColor);
            DrawingUtil.drawText(contentStream, PositionedStyledText.builder().x(textStartX).y(startY).font(imageTextFont).fontSize(fontSize).color(imageTextColor).text(imageText).build());
        }
        
    }

    @Override
    protected float calculateInnerHeight() {
        return (float) cell.getFitSize().getY();
    }

}
