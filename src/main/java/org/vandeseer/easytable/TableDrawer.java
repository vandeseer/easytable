package org.vandeseer.easytable;

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

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
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

    protected final List<BiConsumer<Drawer, DrawingContext>> drawerList = new LinkedList<>();
    {
        this.drawerList.add((drawer, drawingContext) -> {
            drawer.drawBackground(drawingContext);
            drawer.drawContent(drawingContext);
        });
        this.drawerList.add(Drawer::drawBorders);
    }

    public static class PageData {

        public final int firstRowOnPage;
        public final int firstRowOnNextPage;

        public PageData(int firstRowOnPage, int firstRowOnNextPage) {
            this.firstRowOnPage = firstRowOnPage;
            this.firstRowOnNextPage = firstRowOnNextPage;
        }
    }

    public void draw() {
        drawPage(new PageData(0, table.getRows().size()));
    }

    protected void drawPage(PageData pageData) {
        drawerList.forEach(drawer ->
            drawWithFunction(pageData, new Point2D.Float(this.startX, this.startY), drawer)
        );
    }

    private Queue<PageData> computeRowsOnPagesWithNewPageStartOf(float yOffsetOnNewPage) {
        final Queue<PageData> dataForPages = new LinkedList<>();

        float y = startY;

        int firstRowOnPage = 0;
        int lastRowOnPage = 0;

        for (final Row row : table.getRows()) {
            if (isRowTooHighToBeDrawnOnPage(row, yOffsetOnNewPage)) {
                throw new RowIsToHighException("There is a row that is too high to be drawn on a single page");
            }

            if (isNotDrawableOnPage(y, row)) {
                dataForPages.add(new PageData(firstRowOnPage, lastRowOnPage));
                y = yOffsetOnNewPage;
                firstRowOnPage = lastRowOnPage;
            }

            y -= row.getHeight();
            lastRowOnPage++;
        }

        // add the remaining page data
        dataForPages.add(new PageData(firstRowOnPage, lastRowOnPage));

        return dataForPages;
    }

    private boolean isRowTooHighToBeDrawnOnPage(Row row, float yOffsetOnNewPage) {
        return row.getHeight() > (yOffsetOnNewPage - endY);
    }

    public void draw(Supplier<PDDocument> documentSupplier, Supplier<PDPage> pageSupplier, float yOffset) throws IOException {
        final PDDocument document = documentSupplier.get();

        // We create one throwaway page to be able to calculate the page data upfront
        float startOnNewPage = pageSupplier.get().getMediaBox().getHeight() - yOffset;
        final Queue<PageData> pageDataQueue = computeRowsOnPagesWithNewPageStartOf(startOnNewPage);

        for (int i = 0; !pageDataQueue.isEmpty(); i++) {
            final PDPage page;

            if (i > 0 || document.getNumberOfPages() == 0) {
                page = pageSupplier.get();
                document.addPage(page);
            } else {
                page = document.getPage(document.getNumberOfPages() - 1);
            }

            try (final PDPageContentStream newPageContentStream = new PDPageContentStream(document, page, APPEND, false)) {
                contentStream(newPageContentStream).drawPage(pageDataQueue.poll());
            }

            startY(page.getMediaBox().getHeight() - yOffset);
        }
    }

    protected void drawWithFunction(PageData pageData, Point2D.Float startingPoint, BiConsumer<Drawer, DrawingContext> consumer) {
        float y = startingPoint.y;

        for (int rowIndex = pageData.firstRowOnPage; rowIndex < pageData.firstRowOnNextPage; rowIndex++) {
            final Row row = table.getRows().get(rowIndex);
            y -= row.getHeight();
            drawRow(new Point2D.Float(startingPoint.x, y), row, rowIndex, consumer);
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

    private boolean isNotDrawableOnPage(float startY, Row row) {
        return startY - getHighestCellOf(row) < endY;
    }

    private Float getHighestCellOf(Row row) {
        return row.getCells().stream()
                .map(AbstractCell::getHeight)
                .max(Comparator.naturalOrder())
                .orElse(row.getHeight());
    }

}
