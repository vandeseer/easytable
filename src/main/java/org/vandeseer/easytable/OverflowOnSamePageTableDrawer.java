package org.vandeseer.easytable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Queue;
import java.util.function.Supplier;

import static org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode.APPEND;

/**
 * Experimental drawer that allows for several "lanes" on the same
 * page in case it would overflow the current page.
 */
@SuperBuilder(toBuilder = true)
public class OverflowOnSamePageTableDrawer extends TableDrawer {

    @Builder.Default
    private final int lanesPerPage = 2;

    @Builder.Default
    private final float spaceInBetween = 10f;

    // This is really a private field.
    // Shouldn't bet set via the Builder, too – see below ...
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int actualTableLane = 1;

    protected void drawPage(PageData pageData) {
        drawerList.forEach(drawer ->
                drawWithFunction(pageData, new Point2D.Float(this.startX + calculateXOffset(), this.startY), drawer)
        );
    }

    private float calculateXOffset() {
        final float widthOfTableLanes = (actualTableLane - 1) * table.getWidth();
        final float spacing =
                actualTableLane > 1
                        ? (actualTableLane - 1) * spaceInBetween
                        : 0;

        return widthOfTableLanes + spacing;
    }

    public void draw(Supplier<PDDocument> documentSupplier, Supplier<PDPage> pageSupplier, float yOffset) throws IOException {
        final PDDocument document = documentSupplier.get();

        // We create one throwaway page to be able to calculate the page data upfront
        float startOnNewPage = pageSupplier.get().getMediaBox().getHeight() - yOffset;
        determinePageToStartTable(startOnNewPage);
        final Queue<PageData> pageDataQueue = computeRowsOnPagesWithNewPageStartOf(startOnNewPage);

        for (int i = 0; !pageDataQueue.isEmpty(); i++) {
            final PDPage pageToDrawOn = determinePageToDraw(i, document, pageSupplier);

            if ((i == 0 && startTableInNewPage) || i > 0 || document.getNumberOfPages() == 0 || actualTableLane != lanesPerPage) {
                startTableInNewPage = false;
            }

            if (i == 0) {
                tableStartPage = pageToDrawOn;
            }

            try (final PDPageContentStream newPageContentStream = new PDPageContentStream(document, pageToDrawOn, APPEND, compress)) {
                for (int j = 1; j <= lanesPerPage && !pageDataQueue.isEmpty(); j++) {
                    actualTableLane = j;
                    if (actualTableLane > 1) {
                        this.startY = startOnNewPage;
                    }
                    this.contentStream(newPageContentStream)
                            .page(pageToDrawOn)
                            .drawPage(pageDataQueue.poll());
                }
            }

            startY(pageToDrawOn.getMediaBox().getHeight() - yOffset);
        }
    }

    // The following solely exists for excluding the "actualTableLane" from Lombok's builder ...
    // Also see: https://stackoverflow.com/questions/30717640/how-to-exclude-property-from-lombok-builder/44395110#44395110
    public static abstract class OverflowOnSamePageTableDrawerBuilder<C extends OverflowOnSamePageTableDrawer, B extends OverflowOnSamePageTableDrawerBuilder<C, B>> extends TableDrawerBuilder<C, B> {

        private B actualTableLane(int actualTableLane) {
            this.actualTableLane = actualTableLane;
            return self();
        }

    }

}
