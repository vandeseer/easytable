package org.vandeseer.regressiontest;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.Before;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;

import static junit.framework.TestCase.assertTrue;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
import static org.vandeseer.TestUtils.getActualPdfFor;
import static org.vandeseer.TestUtils.getExpectedPdfFor;

public class RingManagerTest {

    private static final String FILE_NAME = TestUtils.TARGET_SUBFOLDER_REGRESSION + "/" + "ringmanager.pdf";

    @Before
    public void before() {
        TestUtils.assertRegressionFolderExists();
    }

    @Test
    public void testRingManagerDocument() throws Exception {
        try (final PDDocument document = new PDDocument()) {
            final PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            final float startY = page.getMediaBox().getHeight() - 150;
            final int startX = 56;

            try (final PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                final Table table = getRingManagerTable();

                TableDrawer.builder()
                        .contentStream(contentStream)
                        .table(table)
                        .startX(startX)
                        .startY(startY)
                        .build()
                        .draw();

                contentStream.setFont(HELVETICA, 8.0f);
                contentStream.beginText();

                contentStream.newLineAtOffset(startX, startY - (table.getHeight() + 22));
                contentStream.showText("Dieser Kampf muss der WB nicht entsprechen, da als Sparringskampf angesetzt.");
                contentStream.endText();
            }

            document.save(TestUtils.TARGET_FOLDER + "/" + FILE_NAME);

            CompareResult compareResult = new PdfComparator<>(getExpectedPdfFor(FILE_NAME), getActualPdfFor(FILE_NAME)).compare();
            assertTrue(compareResult.isEqual());
        }
    }

    private Table getRingManagerTable() {
        final TableBuilder tableBuilder = Table.builder()
                .backwardsCompatibleFontHeight(true)
                .addColumnsOfWidth(26, 70, 390)
                .fontSize(9)
                .borderColor(Color.GRAY)
                .font(HELVETICA);

        final float borderWidthOuter = 1.5f;
        final float borderWidthInner = 1.0f;

        tableBuilder.addRow(Row.builder()
                .add(
                        TextCell.builder().text("1.")
                                .borderWidthTop(borderWidthOuter)
                                .borderWidthLeft(borderWidthOuter)
                                .borderWidthRight(borderWidthInner)
                                .build())
                .add(
                        TextCell.builder().text("WK DBV(s)")
                                .borderWidthTop(borderWidthOuter)
                                .borderWidthRight(borderWidthInner)
                                .borderWidthLeft(borderWidthInner)
                                .build())
                .add(
                        TextCell.builder().text("Rote Ecke:")
                                .borderWidthTop(borderWidthOuter)
                                .borderWidthRight(borderWidthOuter)
                                .build()
                ).build());

        tableBuilder.addRow(Row.builder()
                .add(
                        TextCell.builder()
                                .text("")
                                .borderWidthLeft(borderWidthOuter)
                                .build())
                .add(
                        TextCell.builder().text("Jugend")
                                .borderWidthRight(borderWidthInner)
                                .borderWidthLeft(borderWidthInner)
                                .build())
                .add(
                        TextCell.builder().text("Thomas Test, m, FC St. Pauli, 01.01.1998, Jugend, 67,5 kg, 12K (8S, 4N, 0U)")
                                .borderWidthBottom(borderWidthInner)
                                .borderWidthRight(borderWidthOuter)
                                .build())
                .build());

        tableBuilder.addRow(Row.builder().add(
                TextCell.builder().text("").borderWidthLeft(borderWidthOuter).build()).add(
                TextCell.builder().text("3x3")
                        .borderWidthRight(borderWidthInner)
                        .borderWidthLeft(borderWidthInner)
                        .build()).add(
                TextCell.builder().text("Blaue Ecke:").borderWidthRight(borderWidthOuter).build()).build());

        tableBuilder.addRow(Row.builder().add(
                TextCell.builder().text("")
                        .borderWidthLeft(borderWidthOuter)
                        .borderWidthBottom(borderWidthOuter)
                        .build()).add(
                TextCell.builder().text("10 Uz, KS")
                        .borderWidthRight(borderWidthInner)
                        .borderWidthLeft(borderWidthInner)
                        .borderWidthBottom(borderWidthOuter)
                        .build()).add(
                TextCell.builder().text("Bernd Beispiel, m, Wedeler TSV, 02.01.1999, Jugend, 68,2 kg, 9K (7S, 2N, 0U)")
                        .borderWidthBottom(borderWidthOuter)
                        .borderWidthRight(borderWidthOuter)
                        .build()).build());

        return tableBuilder.build();
    }

}
