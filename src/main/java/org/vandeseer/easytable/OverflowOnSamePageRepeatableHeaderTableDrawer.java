package org.vandeseer.easytable;

import static org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode.APPEND;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Queue;
import java.util.function.Supplier;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.vandeseer.easytable.TableDrawer.PageData;
import org.vandeseer.easytable.structure.Row;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

	@SuperBuilder(toBuilder = true)
	public class OverflowOnSamePageRepeatableHeaderTableDrawer extends TableDrawer {

	    @Builder.Default
	    private final int lanesPerPage = 2;

	    @Builder.Default
	    private final float spaceInBetween = 10f;

		@Builder.Default
		private int numberOfRowsToRepeat = 1;

		private Float headerHeight;
		
	    // This is really meant as a private field.
	    // Unfortunately it might be settable by the builder and we can't make it final :/
	    @Getter(AccessLevel.NONE)
	    @Setter(AccessLevel.NONE)
	    @Builder.Default
	    private int actualTableLane = 1;

	    @Override
	    protected void drawPage(PageData pageData) {
			if (pageData.firstRowOnPage != 0) {
				drawHeaderForCurrentLane();
			}
	        drawerList.forEach(drawer ->
	                drawWithFunction(pageData, new Point2D.Float(this.startX + calculateXOffset(), this.startY ), drawer)
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

		private float calculateHeightForFirstRows() {
			if (headerHeight != null) {
				return headerHeight;
			}

			float height = 0;
			for (int i = 0; i < numberOfRowsToRepeat; i++) {
				height += table.getRows().get(i).getHeight();
			}

			// Cache and return
			headerHeight = height;
			return height;
		}
		
		@Override
		protected void determinePageToStartTable(float yOffsetOnNewPage) {
			float minimumRowsToFitHeight = 0;
			int minimumRowsToFit = table.getRows().size() > numberOfRowsToRepeat
					? numberOfRowsToRepeat + 1
					: numberOfRowsToRepeat;

			for (final Row row : table.getRows().subList(0, minimumRowsToFit))
				minimumRowsToFitHeight += row.getHeight();

			if (startY - minimumRowsToFitHeight < endY) {
				startY = yOffsetOnNewPage + calculateHeightForFirstRows();
				startTableInNewPage = true;
			}
		}
		
		public void drawHeaderForCurrentLane() {
			float adaption = 0;
			for (int k = 0; k < numberOfRowsToRepeat; k++) {
				adaption += table.getRows().get(k).getHeight();
				Point2D.Float startPoint = new Point2D.Float(this.startX + calculateXOffset(),
						this.startY + calculateHeightForFirstRows() - adaption);
				drawRow(startPoint, table.getRows().get(k), k, (drawer, drawingContext) -> {
					drawer.drawBackground(drawingContext);
					drawer.drawContent(drawingContext);
					drawer.drawBorders(drawingContext);
				});
			}
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

	}

