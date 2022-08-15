package org.vandeseer.performance;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.io.File;
import java.io.IOException;

public class PerformanceTest {

	private static final int LINES_MAX = 1000;
	private static final int LINES_STEP = 100;

	private static final int CHARS_PER_CELL_MAX = 300;
	private static final int CHARS_PER_CELL_STEP = 50;


	private static final PDRectangle PAGE = PDRectangle.A4;
	private final static int MARGIN = 50;
	private final static float START_Y = PAGE.getHeight() - MARGIN;
	private final static float WIDTH = PAGE.getWidth() - 2 * MARGIN;

	private static final String TARGET_FOLDER_PERFORMANCE = TestUtils.TARGET_FOLDER + "/performance";


	public static void main(String[] args) throws IOException {
		new File(TARGET_FOLDER_PERFORMANCE).mkdirs();

		final PerformanceTest performanceTest = new PerformanceTest();

		for (int numLines = LINES_STEP; numLines <= LINES_MAX; numLines += LINES_STEP) {
			for (int charsPerCell = 0; charsPerCell <= CHARS_PER_CELL_MAX; charsPerCell += CHARS_PER_CELL_STEP) {
				performanceTest.create(numLines, charsPerCell);
			}
		}
	}

	private void create(int numLines, int numCharsPerCell) throws IOException {
		final long start = System.currentTimeMillis();
		final int numberOfPages;

		try (PDDocument document = new PDDocument()) {
			Table.TableBuilder tableBuilder = Table.builder()
					.backwardsCompatibleFontHeight(true)
					.addColumnsOfWidth(WIDTH)
					.font(PDType1Font.HELVETICA)
					.fontSize(10);

			for (int i = 0; i < numLines; i++) {
				tableBuilder.addRow(
						Row.builder().add(
								TextCell.builder()
										.text(repeat('x', numCharsPerCell))
										.borderWidth(1)
										.build())
								.build());
			}

			TableDrawer.builder()
					.table(tableBuilder.build())
					.startX(MARGIN)
					.startY(START_Y)
					.endY(MARGIN)
					.build()
					.draw(() -> document, () -> new PDPage(PAGE), MARGIN);

			numberOfPages = document.getNumberOfPages();
			document.save(String.format(TARGET_FOLDER_PERFORMANCE + "/performance_%d_%d.pdf", numLines, numCharsPerCell));
		}

		long duration = System.currentTimeMillis() - start;
		System.out.println(String.format("Generating %d lines with %d characters each results in %d pages and took %d ms", numLines, numCharsPerCell, numberOfPages, duration));
	}

	private static String repeat(char ch, int repeat) {
		if (repeat <= 0) {
			return "";
		} else {
			char[] buf = new char[repeat];

			for(int i = repeat - 1; i >= 0; --i) {
				buf[i] = ch;
			}

			return new String(buf);
		}
	}

}
