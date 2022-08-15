package org.vandeseer.regressiontest;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Before;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.RepeatedHeaderTableDrawer;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode.APPEND;
import static org.vandeseer.TestUtils.getActualPdfFor;
import static org.vandeseer.TestUtils.getExpectedPdfFor;

public class Issue111RepeatHeaderTableTest {

	private static final String FILE_NAME_TEST_1 = TestUtils.TARGET_SUBFOLDER_REGRESSION + "/"
			+ "repeatHeaderTable_issue111.pdf";
	private static final String FILE_NAME_TEST_2 = TestUtils.TARGET_SUBFOLDER_REGRESSION + "/"
			+ "repeatHeaderTable_issue111_2.pdf";
	private static final String FILE_NAME_TEST_3 = TestUtils.TARGET_SUBFOLDER_REGRESSION + "/"
			+ "repeatHeaderTable_issue111_3.pdf";
	private static final String FILE_NAME_TEST_4 = TestUtils.TARGET_SUBFOLDER_REGRESSION + "/"
			+ "repeatHeaderTable_issue111_4.pdf";

	private Table table;

	private PDDocument document;

	@Before
	public void before() throws IOException {
		TestUtils.assertRegressionFolderExists();

		document = new PDDocument();
		PDPage my_page = new PDPage(PDRectangle.A4);
		document.addPage(my_page);

		PDPageContentStream content = new PDPageContentStream(document, my_page, APPEND, true);
		content.beginText();
		content.setFont(PDType1Font.TIMES_ROMAN, 15);
		content.newLineAtOffset(50, 500);
		content.showText("This line is added to ensure table is drawn on new page when space not available.");
		content.endText();
		content.close();

		TableBuilder builder = Table.builder().addColumnsOfWidth(100, 400).borderColor(Color.LIGHT_GRAY).borderWidth(1)
				.backwardsCompatibleFontHeight(true)
				.font(PDType1Font.TIMES_ROMAN).fontSize(13).verticalAlignment(VerticalAlignment.TOP)
				.horizontalAlignment(HorizontalAlignment.LEFT).padding(5)

				.addRow(Row.builder().add(TextCell.builder().text("Repeat Row 1").borderWidth(0).colSpan(2).build())
						.build())
				.addRow(Row.builder().add(TextCell.builder().text("Repeat Row 2").borderWidth(0).colSpan(2).build())
						.build());

		for (int i = 1; i < 5; i++) {
			builder.addRow(Row.builder().add(TextCell.builder().text(String.valueOf(i)).build())
					.add(TextCell.builder()
							.text("EASYTABLE PDFBoX EASYTABLE PDFBoX EASYTABLE PDFBoX EASYTABLE PDFBoX EASYTABLE PDFBoX"
									+ "EASYTABLE PDFBoX EASYTABLE PDFBoX EASYTABLE PDFBoX")
							.lineSpacing(1).build())
					.build());
		}
		table = builder.build();
	}

	@Test
	public void createTableWhenPageHeightForHeadersAndOneDataRow() throws IOException {

		createTable(150f, FILE_NAME_TEST_1);
	}

	@Test
	public void createTableInNewPageWhenPageHeightForHeadersOnly() throws IOException {

		createTable(100f, FILE_NAME_TEST_2);
	}

	@Test
	public void createTableInNewPageWhenPageHeightForOneHeaderOnly() throws IOException {

		createTable(70f, FILE_NAME_TEST_3);
	}

	// Checks if any extra space added to top of table in new page
	@Test
	public void createTableInNewPageWhenPageHeightForNoHeaders() throws IOException {

		createTable(30f, FILE_NAME_TEST_4);
	}

	private void createTable(float startY, String outputFileName) throws IOException {

		RepeatedHeaderTableDrawer.builder().table(table).startX(50f).startY(startY).endY(50F).numberOfRowsToRepeat(2)
				.build().draw(() -> document, () -> new PDPage(PDRectangle.A4), 50f);

		document.save(TestUtils.TARGET_FOLDER + "/" + outputFileName);
		document.close();

		final CompareResult compareResult = new PdfComparator<>(getExpectedPdfFor(outputFileName),
				getActualPdfFor(outputFileName)).compare();

		assertTrue(compareResult.isEqual());
	}

}
