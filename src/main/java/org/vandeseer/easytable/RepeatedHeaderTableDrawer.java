package org.vandeseer.easytable;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import lombok.Builder;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class RepeatedHeaderTableDrawer extends TableDrawer {

	@Builder.Default
	private int numberOfRowsToRepeat = 1;

	private List<Integer> headerRowsIndexs;
	
	@Override
	protected void drawPage(PageData pageData) {
		if (pageData.firstRowOnPage != 0) {
			float adaption = 2f;
			if (headerRowsIndexs != null) {
				for (Integer rowIndex : headerRowsIndexs) {
					adaption += table.getRows().get(rowIndex).getHeight();
					drawRow(new Point2D.Float(this.startX,
							this.startY + calculateHeightForSelectedHeaderRows(headerRowsIndexs) - adaption),
							table.getRows().get(rowIndex), rowIndex, (drawer, drawingContext) -> {
								drawer.drawBackground(drawingContext);
								drawer.drawContent(drawingContext);
								drawer.drawBorders(drawingContext);
							});
				}
			}else {
				for (int idx = 0; idx < numberOfRowsToRepeat ; idx++) {
					adaption += table.getRows().get(idx).getHeight();
					drawRow(new Point2D.Float(this.startX,
							this.startY + calculateHeightForFirstRows(numberOfRowsToRepeat) - adaption), table.getRows().get(idx),
							idx, (drawer, drawingContext) -> {
								drawer.drawBackground(drawingContext);
								drawer.drawContent(drawingContext);
								drawer.drawBorders(drawingContext);
							});

				}
			}
		}

		drawerList.forEach(drawer -> drawWithFunction(pageData, new Point2D.Float(this.startX, this.startY), drawer));
	}

	@Override
	public void draw(Supplier<PDDocument> documentSupplier, Supplier<PDPage> pageSupplier, float yOffset)
			throws IOException {
		if(headerRowsIndexs != null) {
			super.draw(documentSupplier, pageSupplier, yOffset + calculateHeightForSelectedHeaderRows(headerRowsIndexs));
		}else {
			super.draw(documentSupplier, pageSupplier, yOffset + calculateHeightForFirstRows(numberOfRowsToRepeat));
		}
		
	}

	private float calculateHeightForFirstRows(int totalRows) {
		float height = 8f;
		for (int curRow = 0; curRow < totalRows; curRow++) {
			height += table.getRows().get(curRow).getHeight();
		}
		return height;
	}
	
	private float calculateHeightForSelectedHeaderRows(List<Integer> headerRowsIndexes) {
		float height = 8f;
		for(Integer rowIndex : headerRowsIndexes) {
			height += table.getRows().get(rowIndex).getHeight();
		}
		
		return height;
	}

}
