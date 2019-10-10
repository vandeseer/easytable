package org.vandeseer.easytable.drawing.cell;

import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.DrawingUtil;
import org.vandeseer.easytable.drawing.Line;
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
            final float height = Math.max(cell.getHeight(), rowHeight);
            final float y = rowHeight < cell.getHeight()
                    ? start.y + rowHeight - cell.getHeight()
                    : start.y;

            DrawingUtil.drawRectangle(contentStream, start.x, y, height, cell.getWidth(), cell.getBackgroundColor());
        }
    }

    @Override
    public abstract void drawContent(DrawingContext drawingContext);

    @Override
    @SneakyThrows
    public void drawBorders(DrawingContext drawingContext) {
        final Point2D.Float start = drawingContext.getStartingPoint();
        final PDPageContentStream contentStream = drawingContext.getContentStream();

        final float cellWidth = cell.getWidth();

        final float rowHeight = cell.getRow().getHeight();
        final float height = Math.max(cell.getHeight(), rowHeight);
        final float sY = rowHeight < cell.getHeight()
                ? start.y + rowHeight - cell.getHeight()
                : start.y;

        // Handle the cell's borders
        final Color cellBorderColor = cell.getBorderColor();
        final Color rowBorderColor = cell.getRow().getSettings().getBorderColor();

        if (cell.hasBorderTop() || cell.hasBorderBottom()) {
            final float correctionLeft = cell.getBorderWidthLeft() / 2;
            final float correctionRight = cell.getBorderWidthRight() / 2;

            if (cell.hasBorderTop()) {
                DrawingUtil.drawLine(contentStream, Line.builder()
                        .startX(start.x - correctionLeft)
                        .startY(start.y + rowHeight)
                        .endX(start.x + cellWidth + correctionRight)
                        .endY(start.y + rowHeight)
                        .width(cell.getBorderWidthTop())
                        .color(cellBorderColor)
                        .resetColor(rowBorderColor)
                        .build());
            }

            if (cell.hasBorderBottom()) {
                DrawingUtil.drawLine(contentStream, Line.builder()
                        .startX(start.x - correctionLeft)
                        .startY(sY)
                        .endX(start.x + cellWidth + correctionRight)
                        .endY(sY)
                        .width(cell.getBorderWidthBottom())
                        .color(cellBorderColor)
                        .resetColor(rowBorderColor)
                        .build());
            }
        }

        if (cell.hasBorderLeft() || cell.hasBorderRight()) {
            final float correctionTop = cell.getBorderWidthTop() / 2;
            final float correctionBottom = cell.getBorderWidthBottom() / 2;

            if (cell.hasBorderLeft()) {
                DrawingUtil.drawLine(contentStream, Line.builder()
                        .startX(start.x)
                        .startY(sY - correctionBottom)
                        .endX(start.x)
                        .endY(sY + height + correctionTop)
                        .width(cell.getBorderWidthLeft())
                        .color(cellBorderColor)
                        .resetColor(rowBorderColor)
                        .build());
            }

            if (cell.hasBorderRight()) {
                DrawingUtil.drawLine(contentStream, Line.builder()
                        .startX(start.x + cellWidth)
                        .startY(sY - correctionBottom)
                        .endX(start.x + cellWidth)
                        .endY(sY + height + correctionTop)
                        .width(cell.getBorderWidthRight())
                        .color(cellBorderColor)
                        .resetColor(rowBorderColor)
                        .build());
            }
        }
    }

}
