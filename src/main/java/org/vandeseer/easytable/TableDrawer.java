package org.vandeseer.easytable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.util.PdfUtil;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Comparator;
import java.util.function.Supplier;

@SuperBuilder(toBuilder = true)
public class TableDrawer {

    @Setter
    @Accessors(chain = true, fluent = true)
    protected PDPageContentStream contentStream;

    protected final Table table;

    @Setter
    @Accessors(chain = true, fluent = true)
    protected float startX;

    @Setter
    @Accessors(chain = true, fluent = true)
    protected float startY;

    protected float endY;

    protected int rowToDraw = 0;

    @Getter
    protected boolean isFinished = false;

    protected TableDrawer(float startX, float startY, PDPageContentStream contentStream, Table table, float endY) {
        this.contentStream = contentStream;
        this.table = table;

        this.startX = startX;
        this.startY = startY - PdfUtil.getFontHeight(table.getSettings().getFont(), table.getSettings().getFontSize());

        this.endY = endY;
    }

    public void draw() throws IOException {
        drawWithFunction(new Point2D.Float(this.startX, this.startY), this::drawBackgroundColorAndCellContent, false);
        drawWithFunction(new Point2D.Float(this.startX, this.startY), this::drawBorders, true);
    }

    public void draw(Supplier<PDDocument> documentSupplier, Supplier<PDPage> pageSupplier, float yOffset) throws IOException {
        final PDDocument document = documentSupplier.get();

        do {
            PDPage page = pageSupplier.get();
            document.addPage(page);

            try (final PDPageContentStream newPageContentStream = new PDPageContentStream(document, page)) {
                contentStream(newPageContentStream).draw();
            }

            startY(page.getMediaBox().getHeight() - yOffset);
        } while (!isFinished());
    }

    protected void drawWithFunction(Point2D.Float startingPoint, TableDrawerFunction function, boolean isLastAction) throws IOException {
        float y = startingPoint.y;

        for (int i = rowToDraw; i < table.getRows().size(); i++) {
            final Row row = table.getRows().get(i);
            int columnCounter = 0;

            // First of all, we need to check whether we should draw any further ...
            final float lowestPoint = y - row.getCells().stream()
                    .map(AbstractCell::getHeight)
                    .max(Comparator.naturalOrder())
                    .orElse(row.getHeight());

            if (lowestPoint < endY) {
                if (isLastAction) {
                    rowToDraw = i;
                }
                return;
            }

            float x = startingPoint.x;
            y -= row.getHeight();

            for (AbstractCell cell : row.getCells()) {

                while (table.isRowSpanAt(i, columnCounter)) {
                    x += table.getColumns().get(columnCounter).getWidth();
                    columnCounter++;
                }

                // This is the interesting part :)
                function.accept(new Point2D.Float(x, y), cell);

                x += cell.getWidth();
                columnCounter += cell.getColSpan();
            }
        }

        if (isLastAction) {
            this.isFinished = true;
        }
    }

    protected void drawBackgroundColorAndCellContent(Point2D.Float start, AbstractCell cell) throws IOException {

        final float rowHeight = cell.getRow().getHeight();
        final float height = cell.getHeight() > rowHeight ? cell.getHeight() : rowHeight;
        final float y = cell.getHeight() > rowHeight ? start.y + rowHeight - cell.getHeight() : start.y;

        // Handle the cell's background color
        if (cell.hasBackgroundColor()) {
            drawCellBackground(cell, new Point2D.Float(start.x, y), height);
        }

        cell.getDrawer().draw(new DrawingContext(contentStream, start));
    }

    protected void drawBorders(Point2D.Float start, AbstractCell cell) throws IOException {
        final float rowHeight = cell.getRow().getHeight();
        final float cellWidth = cell.getWidth();

        final float height = cell.getHeight() > rowHeight ? cell.getHeight() : rowHeight;
        final float sY = cell.getHeight() > rowHeight ? start.y + rowHeight - cell.getHeight() : start.y;

        // Handle the cell's borders
        final Color cellBorderColor = cell.getBorderColor();
        final Color rowBorderColor = cell.getRow().getSettings().getBorderColor();

        if (cell.hasBorderTop() || cell.hasBorderBottom()) {
            final float correctionLeft = cell.getBorderWidthLeft() / 2;
            final float correctionRight = cell.getBorderWidthRight() / 2;

            if (cell.hasBorderTop()) {
                contentStream.moveTo(start.x - correctionLeft, start.y + rowHeight);
                drawLine(cellBorderColor, cell.getBorderWidthTop(), start.x + cellWidth + correctionRight, start.y + rowHeight);
                contentStream.setStrokingColor(rowBorderColor);
            }

            if (cell.hasBorderBottom()) {
                contentStream.moveTo(start.x - correctionLeft, sY);
                drawLine(cellBorderColor, cell.getBorderWidthBottom(), start.x + cellWidth + correctionRight, sY);
                contentStream.setStrokingColor(rowBorderColor);
            }
        }

        if (cell.hasBorderLeft() || cell.hasBorderRight()) {
            final float correctionTop = cell.getBorderWidthTop() / 2;
            final float correctionBottom = cell.getBorderWidthBottom() / 2;

            if (cell.hasBorderLeft()) {
                contentStream.moveTo(start.x, sY - correctionBottom);
                drawLine(cellBorderColor, cell.getBorderWidthLeft(), start.x, sY + height + correctionTop);
                contentStream.setStrokingColor(rowBorderColor);
            }

            if (cell.hasBorderRight()) {
                contentStream.moveTo(start.x + cellWidth, sY - correctionBottom);
                drawLine(cellBorderColor, cell.getBorderWidthRight(), start.x + cellWidth, sY + height + correctionTop);
                contentStream.setStrokingColor(rowBorderColor);
            }
        }
    }

    protected void drawLine(Color color, float width, float toX, float toY) throws IOException {
        contentStream.setLineWidth(width);
        contentStream.lineTo(toX, toY);
        contentStream.setStrokingColor(color);
        contentStream.stroke();
    }

    protected void drawCellBackground(final AbstractCell cell, Point2D.Float start, final float height)
            throws IOException {
        contentStream.setNonStrokingColor(cell.getBackgroundColor());

        contentStream.addRect(start.x, start.y, cell.getWidth(), height);
        contentStream.fill();
        contentStream.closePath();

        // Reset NonStrokingColor to default value
        contentStream.setNonStrokingColor(Color.BLACK);
    }

    // TODO Muss wieder erweiterbar bzw vererbar sein!
    public static void drawText(String text, PDFont font, int fontSize, Color color, float x, float y, PDPageContentStream contentStream)
            throws IOException {
        contentStream.beginText();
        contentStream.setNonStrokingColor(color);
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
        contentStream.setCharacterSpacing(0);
    }

}
