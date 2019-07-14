package org.vandeseer.easytable.drawing.cell;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.ImageCell;

import java.awt.geom.Point2D;
import java.io.IOException;

public class ImageCellDrawer implements Drawer {

    private ImageCell cell;

    public ImageCellDrawer() {
    }

    public ImageCellDrawer(ImageCell cell) {
        this.cell = cell;
    }

    @Override
    public void setCell(AbstractCell cell) {
        this.cell = (ImageCell) cell;
    }

    @Override
    public void draw(DrawingContext drawingContext) throws IOException {
        final PDPageContentStream contentStream = drawingContext.getContentStream();
        final float moveX = drawingContext.getStartingPoint().x;
        final float moveY = drawingContext.getStartingPoint().y;

        final Point2D.Float size = cell.getFitSize();
        final Point2D.Float drawAt = new Point2D.Float();

        // TODO Refactor out vertical alignment for image and text cells? The logic is the same for both types ...
        // Vertical alignment
        float yStartRelative = cell.getRow().getHeight() - cell.getPaddingTop(); // top position
        if (cell.getRow().getHeight() > cell.getHeight() || cell.getRowSpan() > 1) {

            if (cell.getSettings().getVerticalAlignment() == VerticalAlignment.MIDDLE) {

                float outerHeight = cell.getRowSpan() > 1 ? cell.getHeight() : cell.getRow().getHeight();
                yStartRelative = outerHeight / 2 + size.y / 2;

                if (cell.getRowSpan() > 1) {
                    float rowSpanAdaption = cell.calculateHeightForRowSpan() - cell.getRow().getHeight();
                    yStartRelative -= rowSpanAdaption;
                }

            } else if (cell.getSettings().getVerticalAlignment() == VerticalAlignment.BOTTOM) {

                yStartRelative = size.y + cell.getPaddingBottom();

                if (cell.getRowSpan() > 1) {
                    float rowSpanAdaption = cell.calculateHeightForRowSpan() - cell.getRow().getHeight();
                    yStartRelative -= rowSpanAdaption;
                }
            }
        }

        // Handle horizontal alignment by adjusting the xOffset
        float xOffset = moveX + cell.getPaddingLeft();
        if (cell.getSettings().getHorizontalAlignment() == HorizontalAlignment.RIGHT) {
            xOffset = moveX + (cell.getWidth() - (size.x + cell.getPaddingRight()));

        } else if (cell.getSettings().getHorizontalAlignment() == HorizontalAlignment.CENTER) {
            final float diff = (cell.getWidth() - size.x) / 2;
            xOffset = moveX + diff;

        }

        drawAt.x = xOffset;
        drawAt.y = moveY + yStartRelative - size.y;

        contentStream.drawImage(cell.getImage(), drawAt.x, drawAt.y, size.x, size.y);
    }

}
