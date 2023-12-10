package org.vandeseer.regressiontest;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Before;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName.HELVETICA;
import static org.vandeseer.TestUtils.getActualPdfFor;
import static org.vandeseer.TestUtils.getExpectedPdfFor;
import static org.vandeseer.easytable.settings.HorizontalAlignment.*;

public class Issue20WordWrappingTest {

    private static final String FILE_NAME_TEST_1 = TestUtils.TARGET_SUBFOLDER_REGRESSION + "/" + "wordWrapping_issue20.pdf";
    private static final String FILE_NAME_TEST_2 = TestUtils.TARGET_SUBFOLDER_REGRESSION + "/" + "wordWrapping_issue20_2.pdf";
    private static final String FILE_NAME_TEST_3 = TestUtils.TARGET_SUBFOLDER_REGRESSION + "/" + "wordWrapping_issue20_3.pdf";

    @Before
    public void before() {
        TestUtils.assertRegressionFolderExists();
    }

    @Test
    public void createSampleDocumentWithWordWrapping_Issue20() throws Exception {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(8, 8, 8)
                .fontSize(12)
                .font(new PDType1Font(HELVETICA))
                .borderColor(Color.RED)
                .wordBreak(true);

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text("a").borderWidth(1).padding(0f).horizontalAlignment(CENTER).build())
                .add(TextCell.builder().text("B").borderWidth(1).padding(0f).build())
                .add(TextCell.builder().text("#").borderWidth(1).padding(0f).build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text("c").borderWidth(1).padding(0f).horizontalAlignment(RIGHT).build())
                .add(TextCell.builder().text("D").borderWidth(1).padding(0f).build())
                .add(TextCell.builder().text("g").borderWidth(1).padding(0f).build())
                .build());

        TestUtils.createAndSaveDocumentWithTable(FILE_NAME_TEST_1, tableBuilder.build());

        CompareResult compareResult = new PdfComparator<>(getExpectedPdfFor(FILE_NAME_TEST_1), getActualPdfFor(FILE_NAME_TEST_1)).compare();
        assertTrue(compareResult.isEqual());
    }

    @Test
    public void createSampleDocumentWithWordWrapping_Issue20_2() throws Exception {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(70, 260, 70, 70)
                .fontSize(12)
                .font(new PDType1Font(HELVETICA))
                .borderColor(Color.WHITE)
                .wordBreak(true);

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().borderWidth(1).text("Stückzahl").build())
                .add(TextCell.builder().borderWidth(1).horizontalAlignment(CENTER).text("Artikel Bez.").build())
                .add(TextCell.builder().borderWidth(1).horizontalAlignment(CENTER).text("Einzelpreis").build())
                .add(TextCell.builder().borderWidth(1).horizontalAlignment(CENTER).text("Gesamt").build())
                .backgroundColor(Color.LIGHT_GRAY)
                .build());

        for (int i = 0; i < 10; i++) {
            int price = 200;
            tableBuilder.addRow(Row.builder()
                    .add(TextCell.builder().text(String.valueOf(i + 1)).horizontalAlignment(RIGHT).build())
                    .add(TextCell.builder().text("Thermaltake Desktop Tenor VB 2000 SNS").build())
                    .add(TextCell.builder().text(price + " €").horizontalAlignment(RIGHT).build())
                    .add(TextCell.builder().text((i + 1) * price + " €").horizontalAlignment(RIGHT).build())
                    .build());
        }

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text(String.valueOf(3)).horizontalAlignment(RIGHT).build())
                .add(TextCell.builder().text("This breaks because it is too long Thermaltake Desktop Tenor VB 2000 SNS").build())
                .add(TextCell.builder().text(120 + " €").horizontalAlignment(RIGHT).build())
                .add(TextCell.builder().text(3 * 120 + " €").horizontalAlignment(RIGHT).build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text(String.valueOf(3)).horizontalAlignment(RIGHT).build())
                .add(TextCell.builder().text("Fubar Article").build())
                .add(TextCell.builder().text(234 + " €").horizontalAlignment(RIGHT).build())
                .add(TextCell.builder().text(3 * 234 + " €").horizontalAlignment(RIGHT).build())
                .build());

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text("This is a text that spans over four columns. Hooray! Also it breaks" +
                        "because it is too long. This is a text that spans over four columns. Hooray! " +
                        "Also it breaks.").colSpan(4).build())
                .backgroundColor(Color.LIGHT_GRAY)
                .build());

        for (int i = 0; i < 5; i++) {
            int price = 429;
            tableBuilder.addRow(Row.builder()
                    .add(TextCell.builder().text(String.valueOf(i + 1)).horizontalAlignment(RIGHT).build())
                    .add(TextCell.builder().text("Some other fancy product").build())
                    .add(TextCell.builder().text(price + " €").horizontalAlignment(RIGHT).build())
                    .add(TextCell.builder().text((i + 1) * price + " €").horizontalAlignment(RIGHT).build())
                    .build());
        }

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text("Gesamt Seite 1:").horizontalAlignment(RIGHT).colSpan(3).borderWidth(1).build())
                .add(TextCell.builder().text("1 600 €").horizontalAlignment(RIGHT).borderWidth(1).build())
                .backgroundColor(Color.LIGHT_GRAY)
                .build());

        TestUtils.createAndSaveDocumentWithTable(FILE_NAME_TEST_2, tableBuilder.build());

        CompareResult compareResult = new PdfComparator<>(getExpectedPdfFor(FILE_NAME_TEST_2), getActualPdfFor(FILE_NAME_TEST_2)).compare();
        assertTrue(compareResult.isEqual());
    }

    @Test
    public void issue20() throws IOException {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 100)
                .fontSize(8)
                .font(new PDType1Font(HELVETICA))
                .addRow(Row.builder()
                        .add(TextCell.builder().text("K.").horizontalAlignment(LEFT).borderWidth(1).build())
                        .add(TextCell.builder().text("fubar").horizontalAlignment(LEFT).borderWidth(1).build())
                        .horizontalAlignment(RIGHT)
                        .build())
                .wordBreak(true);

        TestUtils.createAndSaveDocumentWithTable(FILE_NAME_TEST_3, tableBuilder.build());

        CompareResult compareResult = new PdfComparator<>(getExpectedPdfFor(FILE_NAME_TEST_3), getActualPdfFor(FILE_NAME_TEST_3)).compare();
        assertTrue(compareResult.isEqual());
    }

}
