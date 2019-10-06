package org.vandeseer.easytable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.util.PdfUtil;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode.APPEND;

@SuperBuilder(toBuilder = true)
public class TableDrawer {

    protected final Table table;

    @Setter
    @Accessors(chain = true, fluent = true)
    protected PDPageContentStream contentStream;

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

    protected final Queue<BiConsumer<Drawer, DrawingContext>> drawerQueue = new LinkedList<>();

    protected final DrawingGuard drawingGuard = new DrawingGuard();

    protected TableDrawer(float startX, float startY, PDPageContentStream contentStream, Table table, float endY) {
        this.contentStream = contentStream;
        this.table = table;

        this.startX = startX;
        this.startY = startY - PdfUtil.getFontHeight(table.getSettings().getFont(), table.getSettings().getFontSize());

        this.endY = endY;
    }

    public void draw() throws IOException {
        this.drawerQueue.add((drawer, drawingContext) -> {
            drawer.drawBackground(drawingContext);
            drawer.drawContent(drawingContext);
        });
        this.drawerQueue.add(Drawer::drawBorders);

        while (!drawerQueue.isEmpty()) {
            drawWithFunction(new Point2D.Float(this.startX, this.startY), drawerQueue.poll());
        }
    }

    public void draw(Supplier<PDDocument> documentSupplier, Supplier<PDPage> pageSupplier, float yOffset) throws IOException {
        final PDDocument document = documentSupplier.get();

        for (int i = 0; !isFinished(); i++) {
            final PDPage page;

            if (i > 0 || document.getNumberOfPages() == 0) {
                page = pageSupplier.get();
                document.addPage(page);
            } else {
                page = document.getPage(document.getNumberOfPages() - 1);
            }

            try (final PDPageContentStream newPageContentStream = new PDPageContentStream(document, page, APPEND, false)) {
                contentStream(newPageContentStream).draw();
            }

            startY(page.getMediaBox().getHeight() - yOffset);
        }
    }

    protected void drawWithFunction(Point2D.Float startingPoint, BiConsumer<Drawer, DrawingContext> consumer) {
        float y = startingPoint.y;

        for (int currentRowToDraw = rowToDraw; currentRowToDraw < table.getRows().size(); currentRowToDraw++) {
            final Row row = table.getRows().get(currentRowToDraw);

            if (isDrawableOnPage(y, row)) {
                if (drawerQueue.isEmpty()) {
                    rowToDraw = currentRowToDraw;
                }

                drawingGuard.noteRowNotFinishedSuccessfully();
                if (drawingGuard.isNoticingDrawingIssue()) {
                    throw new TooManyAttemptsException("Could not successfully draw, most likely a cell's content is " +
                            "bigger than the page it should be drawn onto.");
                }

                return;
            }

            y -= row.getHeight();
            drawRow(new Point2D.Float(startingPoint.x, y), row, currentRowToDraw, consumer);

            drawingGuard.noteRowFinishedSuccessfully();
        }

        if (drawerQueue.isEmpty()) {
            isFinished = true;
        }
    }

    protected void drawRow(Point2D.Float start, Row row, int rowIndex, BiConsumer<Drawer, DrawingContext> consumer) {
        float x = start.x;

        int columnCounter = 0;
        for (AbstractCell cell : row.getCells()) {

            while (table.isRowSpanAt(rowIndex, columnCounter)) {
                x += table.getColumns().get(columnCounter).getWidth();
                columnCounter++;
            }

            // This is the interesting part :)
            consumer.accept(cell.getDrawer(), new DrawingContext(contentStream, new Point2D.Float(x, start.y)));

            x += cell.getWidth();
            columnCounter += cell.getColSpan();
        }
    }

    private boolean isDrawableOnPage(float startY, Row row) {
        return startY - getHighestCellOf(row) < endY;
    }

    private Float getHighestCellOf(Row row) {
        return row.getCells().stream()
                .map(AbstractCell::getHeight)
                .max(Comparator.naturalOrder())
                .orElse(row.getHeight());
    }

}
