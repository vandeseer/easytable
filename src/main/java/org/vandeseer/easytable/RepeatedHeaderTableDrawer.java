package org.vandeseer.easytable;

import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellBaseData;

import java.awt.geom.Point2D;
import java.io.IOException;

@SuperBuilder
public class RepeatedHeaderTableDrawer extends TableDrawer {

    protected RepeatedHeaderTableDrawer(float startX, float startY, PDPageContentStream contentStream, Table table, float endY) {
        super(startX, startY, contentStream, table, endY);
    }

    @Override
    public void draw() throws IOException {
        // If we have drawn already some rows and we are called again we are on a new page and we need to
        // draw the header first of all ...
        if (rowToDraw > 0) {
            drawHeaderWithFunction(new Point2D.Float(this.startX, this.startY), this::drawBackgroundColorAndCellContent);
            drawHeaderWithFunction(new Point2D.Float(this.startX, this.startY), this::drawBorders);
            this.startY -= table.getRows().get(0).getHeight();
        }

        drawWithFunction(new Point2D.Float(this.startX, this.startY), this::drawBackgroundColorAndCellContent, false);
        drawWithFunction(new Point2D.Float(this.startX, this.startY), this::drawBorders, true);
    }

    protected void drawHeaderWithFunction(Point2D.Float startingPoint, TableDrawerFunction function) throws IOException {
        float x = startingPoint.x;
        float y = startingPoint.y;

        final Row headerRow = table.getRows().get(0);
        int columnCounter = 0;

        y -= headerRow.getHeight();

        for (CellBaseData cell : headerRow.getCells()) {

            while (table.isRowSpanAt(0, columnCounter)) {
                x += table.getColumns().get(columnCounter).getWidth();
                columnCounter++;
            }

            float cellWidth = table.getAvailableCellWidthRespectingSpan(columnCounter, cell.getColSpan());

            function.accept(new Point2D.Float(x, y), headerRow, cell, cellWidth);

            x += cellWidth;
            columnCounter += cell.getColSpan();
        }
    }

}
