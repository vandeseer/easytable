package org.vandeseer.easytable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.vandeseer.easytable.structure.Row;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Queue;
import java.util.function.Supplier;

@SuperBuilder
public class RepeatedHeaderTableDrawer extends TableDrawer {

    @Builder.Default
    private int numberOfRowsToRepeat = 1;

    private Float headerHeight;
    
	@Getter(value = AccessLevel.NONE)
	private boolean startTableInNewPage;

    @Override
    protected void drawPage(PageData pageData) {
        if (pageData.firstRowOnPage != 0) {
            float adaption = 0;
            for (int i = 0; i < numberOfRowsToRepeat; i++) {
                adaption += table.getRows().get(i).getHeight();
                Point2D.Float startPoint = new Point2D.Float(this.startX, this.startY + calculateHeightForFirstRows() - adaption);
                drawRow(startPoint, table.getRows().get(i), i, (drawer, drawingContext) -> {
                    drawer.drawBackground(drawingContext);
                    drawer.drawContent(drawingContext);
                    drawer.drawBorders(drawingContext);
                });
            }
        }

        drawerList.forEach(drawer ->
                drawWithFunction(pageData, new Point2D.Float(this.startX, this.startY), drawer)
        );
    }
    
	@Override
	protected Queue<PageData> computeRowsOnPagesWithNewPageStartOf(float yOffsetOnNewPage) {
		float minimumRowsToFitHeight = 0;
		int minimumRowsToFit = table.getRows().size() > numberOfRowsToRepeat ? numberOfRowsToRepeat + 1
				: numberOfRowsToRepeat;

		for (final Row row : table.getRows().subList(0, minimumRowsToFit))
			minimumRowsToFitHeight += row.getHeight();

		if (startY - minimumRowsToFitHeight < endY) {
			startY = yOffsetOnNewPage + calculateHeightForFirstRows();
			startTableInNewPage = true;
		}
		return super.computeRowsOnPagesWithNewPageStartOf(yOffsetOnNewPage);
	}

	@Override
	protected PDPage determinePageToDraw(int index, PDDocument document, Supplier<PDPage> pageSupplier) {
		final PDPage pageToDrawOn;
		if ((index == 0 && startTableInNewPage) || index > 0 || document.getNumberOfPages() == 0) {
			startTableInNewPage = false;
			pageToDrawOn = pageSupplier.get();
			document.addPage(pageToDrawOn);
		} else 
			pageToDrawOn = document.getPage(document.getNumberOfPages() - 1);		
		return pageToDrawOn;
	}

    @Override
    public void draw(Supplier<PDDocument> documentSupplier, Supplier<PDPage> pageSupplier, float yOffset) throws IOException {
        super.draw(documentSupplier, pageSupplier, yOffset + calculateHeightForFirstRows());
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

}
