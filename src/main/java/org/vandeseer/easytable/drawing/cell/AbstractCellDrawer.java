package org.vandeseer.easytable.drawing.cell;

import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.DrawingUtil;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class AbstractCellDrawer<T extends AbstractCell> implements Drawer {

    protected T cell;

    public AbstractCellDrawer<T> withCell(T cell) {
        this.cell = cell;
        return this;
    }

    @Override
    @SneakyThrows
    public void drawBackground(DrawingContext drawingContext) {
        if (cell.hasBackgroundColor()) {
            final PDPageContentStream contentStream = drawingContext.getContentStream();
            final Point2D.Float start = drawingContext.getStartingPoint();

            final float rowHeight = cell.getRow().getHeight();
            final float y = cell.getHeight() > rowHeight
                    ? start.y + rowHeight - cell.getHeight()
                    : start.y;

            final Point2D.Float start1 = new Point2D.Float(start.x, y);
            DrawingUtil.drawRectangle(contentStream, start1.x, start1.y, Math.max(cell.getHeight(), rowHeight), cell.getWidth(), cell.getBackgroundColor());
        }
    }

    @Override
    public abstract void drawContent(DrawingContext drawingContext);

    @Override
    @SneakyThrows
    public void drawBorders(DrawingContext drawingContext) {
        final Point2D.Float start = drawingContext.getStartingPoint();
        final PDPageContentStream contentStream = drawingContext.getContentStream();

        final float rowHeight = cell.getRow().getHeight();
        final float cellWidth = cell.getWidth();

        final float height = Math.max(cell.getHeight(), rowHeight);
        final float sY = cell.getHeight() > rowHeight ? start.y + rowHeight - cell.getHeight() : start.y;

        // Handle the cell's borders
        final Color cellBorderColor = cell.getBorderColor();
        final Color rowBorderColor = cell.getRow().getSettings().getBorderColor();

        if (cell.hasBorderTop() || cell.hasBorderBottom()) {
            final float correctionLeft = cell.getBorderWidthLeft() / 2;
            final float correctionRight = cell.getBorderWidthRight() / 2;

            if (cell.hasBorderTop()) {
                contentStream.moveTo(start.x - correctionLeft, start.y + rowHeight);
                DrawingUtil.drawLine(contentStream, cell.getBorderWidthTop(), start.x + cellWidth + correctionRight, start.y + rowHeight, cellBorderColor);
                contentStream.setStrokingColor(rowBorderColor);
            }

            if (cell.hasBorderBottom()) {
                contentStream.moveTo(start.x - correctionLeft, sY);
                DrawingUtil.drawLine(contentStream, cell.getBorderWidthBottom(), start.x + cellWidth + correctionRight, sY, cellBorderColor);
                contentStream.setStrokingColor(rowBorderColor);
            }
        }

        if (cell.hasBorderLeft() || cell.hasBorderRight()) {
            final float correctionTop = cell.getBorderWidthTop() / 2;
            final float correctionBottom = cell.getBorderWidthBottom() / 2;

            if (cell.hasBorderLeft()) {
                contentStream.moveTo(start.x, sY - correctionBottom);
                DrawingUtil.drawLine(contentStream, cell.getBorderWidthLeft(), start.x, sY + height + correctionTop, cellBorderColor);
                contentStream.setStrokingColor(rowBorderColor);
            }

            if (cell.hasBorderRight()) {
                contentStream.moveTo(start.x + cellWidth, sY - correctionBottom);
                DrawingUtil.drawLine(contentStream, cell.getBorderWidthRight(), start.x + cellWidth, sY + height + correctionTop, cellBorderColor);
                contentStream.setStrokingColor(rowBorderColor);
            }
        }
    }

}
