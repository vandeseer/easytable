package org.vandeseer.easytable;

import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.vandeseer.easytable.structure.Table;

import java.awt.geom.Point2D;
import java.io.IOException;

@SuperBuilder
public class RepeatedHeaderTableDrawer extends TableDrawer {

    protected RepeatedHeaderTableDrawer(float startX, float startY, PDPageContentStream contentStream, Table table, float endY) {
        super(startX, startY, contentStream, table, endY);
    }

    @Override
    public void draw() throws IOException {

        if (rowToDraw > 0) {
            drawRow(new Point2D.Float(this.startX, this.startY), table.getRows().get(0), 0, (drawer, drawingContext) -> {
                drawer.drawBackground(drawingContext);
                drawer.drawContent(drawingContext);
                drawer.drawBorders(drawingContext);
            });
        }

        super.draw();
    }

}
