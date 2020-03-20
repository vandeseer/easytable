package org.vandeseer.integrationtest;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.ImageCell;
import org.vandeseer.easytable.structure.cell.TextCell;

// Test for ISSUE-40 https://github.com/vandeseer/easytable/issues/40
public class ImageSizingOnRowSpanningTest {

	@Test
	public void testImageSizingOnRowSpanning() throws Exception {

		float columns[];
		columns = new float[3];
		columns[0] = 100f;
		columns[1] = 100f;
		columns[2] = 100f;
		final Table.TableBuilder tableBuilder = Table.builder().addColumnsOfWidth(columns);

		final PDImageXObject glider = TestUtils.createGliderImage();

		// First row
		tableBuilder.addRow(Row.builder().add(TextCell.builder().text("Row 1 Col2").borderWidth(0).build())
				.add(TextCell.builder().text("Row 1 Col3").borderWidth(0).build())
				.add(ImageCell.builder().rowSpan(5).image(glider).borderWidth(0).build()).build());

		// Second row
		tableBuilder.addRow(
				Row.builder().add(ImageCell.builder().maxHeight(20).rowSpan(2).image(glider).borderWidth(0).build())
						.add(TextCell.builder().text("Row 2 Col3").borderWidth(0).build()).build());
		// Third row
		tableBuilder.addRow(Row.builder()
				// .add(ImageCell.builder().image(glider).borderWidth(1).build())
				.add(TextCell.builder().text("Row 3 Col3").borderWidth(0).build()).build());

		// Second row
		tableBuilder.addRow(
				Row.builder().add(ImageCell.builder().maxHeight(20).rowSpan(2).image(glider).borderWidth(0).build())
						.add(TextCell.builder().text("Row 2 Col3").borderWidth(0).build()).build());
		// Third row
		tableBuilder.addRow(Row.builder()
				// .add(ImageCell.builder().image(glider).borderWidth(1).build())
				.add(TextCell.builder().text("Row 3 Col3").borderWidth(0).build()).build());

		TestUtils.createAndSaveDocumentWithTable("imageCellSizingOnRowSpanning.pdf", tableBuilder.build());
	}

}
