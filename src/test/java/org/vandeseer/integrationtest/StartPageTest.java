package org.vandeseer.integrationtest;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode.APPEND;
import static org.vandeseer.TestUtils.getActualPdfFor;
import static org.vandeseer.TestUtils.getExpectedPdfFor;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Before;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.TextCell;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;

public class StartPageTest {

	private static final String FULL_TABLE_EXISTING_PAGE_FILE_NAME = "fullTableExistingPage.pdf";
	private static final String START_TABLE_EXISTING_PAGE_FILE_NAME = "startTableExistingPage.pdf";
	private static final String START_TABLE_NEW_PAGE_FILE_NAME = "startTableNewPage.pdf";

	private Table table;

	private PDDocument document;

	private PDPage currentPage;

	private TableDrawer drawer;

	@Before
	public void before() throws IOException {
		TestUtils.assertRegressionFolderExists();

		document = new PDDocument();
		currentPage = new PDPage(PDRectangle.A4);
		document.addPage(currentPage);

		try (final PDPageContentStream content = new PDPageContentStream(document, currentPage, APPEND, false)) {
			content.beginText();
			content.setFont(PDType1Font.TIMES_ROMAN, 15);
			content.newLineAtOffset(50, 500);
			content.showText("This line is added to ensure table is drawn on new page when space not available.");
			content.endText();
			content.close();

			TableBuilder builder = Table.builder().addColumnsOfWidth(150, 150, 150).fontSize(25).font(HELVETICA)
					.padding(5).borderWidth(1)
					.addRow(Row.builder().add(TextCell.builder().text("Header").build())
							.add(TextCell.builder().text("of").build()).add(TextCell.builder().text("Table").build())
							.build());

			for (int i = 1; i < 5; i++) {
				builder.addRow(Row.builder().add(TextCell.builder().text("Row " + i).build())
						.add(TextCell.builder().text("of").build()).add(TextCell.builder().text("Table").build())
						.build()).build();
			}
			table = builder.build();
		}
	}

	private void createTable(float startY, String outputFileName) throws IOException {

		drawer = TableDrawer.builder().table(table).startX(50f).startY(startY).endY(50F).build();

		drawer.draw(() -> document, () -> new PDPage(PDRectangle.A4), 50f);

		document.save(TestUtils.TARGET_FOLDER + "/" + outputFileName);
		document.close();

		final CompareResult compareResult = new PdfComparator<>(getExpectedPdfFor(outputFileName),
				getActualPdfFor(outputFileName)).compare();

		assertTrue(compareResult.isEqual());
	}

	@Test
	public void createCompleteTableInExistingPage() throws IOException {
		createTable(200f, FULL_TABLE_EXISTING_PAGE_FILE_NAME);
		assertEquals(currentPage, drawer.getTableStartPage());
	}

	@Test
	public void createSplitTableStartInExistingPage() throws IOException {
		createTable(120f, START_TABLE_EXISTING_PAGE_FILE_NAME);
		assertEquals(currentPage, drawer.getTableStartPage());
	}

	@Test
	public void createTableStartInNewPage() throws IOException {
		createTable(70f, START_TABLE_NEW_PAGE_FILE_NAME);
		assertEquals(document.getPage(document.getNumberOfPages() - 1), drawer.getTableStartPage());
	}
}
