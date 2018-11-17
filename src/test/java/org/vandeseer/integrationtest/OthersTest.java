package org.vandeseer.integrationtest;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.CellText;

import java.awt.*;
import java.io.IOException;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
import static org.vandeseer.easytable.settings.HorizontalAlignment.LEFT;
import static org.vandeseer.easytable.settings.HorizontalAlignment.RIGHT;

// TODO test border color on row level
// TODO test the precedence of cell level settings of row level/table level settings
// TODO can we somehow put the "cursor" directly to where the content stream is right now?!
public class OthersTest {

    @Test
    public void createTableWithDifferentFontsInCells() throws IOException {
        // Define the table structure first
        final TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(300, 120, 70)
                .fontSize(8)
                .font(HELVETICA)
                .wordBreak(true);

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("This is top right aligned without a border; the next cell has line spacing 1.5f")
                        .horizontalAlignment(RIGHT)
                        .verticalAlignment(VerticalAlignment.TOP)
                        .build())
                .add(CellText.builder().text("And this is another cell with a very long long long text that tells a nice" +
                        " and useless story, because Iam to lazy to get a lorem-ipsum.")
                        .lineSpacing(1.5f).build())
                .add(CellText.builder().text("This is center and middle aligned with a line spacing of 1.2f")
                        .backgroundColor(Color.ORANGE)
                        .verticalAlignment(VerticalAlignment.MIDDLE)
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .lineSpacing(1.2f)
                        .build())
                .backgroundColor(Color.BLUE)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("This is left bottom aligned with a border on the bottom")
                        .horizontalAlignment(LEFT)
                        .verticalAlignment(VerticalAlignment.BOTTOM)
                        .borderWidthBottom(1)
                        .build())
                .add(CellText.builder().text("Bavaria ipsum dolor sit amet Schaung kost nix des Biagadn obandeln. " +
                        "Di gscheit des is hoid aso kummd Haberertanz heitzdog de Sonn des is a gmahde Wiesn, " +
                        "Charivari: Kimmt gscheid singd Buam Leonhardifahrt da pfundig gar nia need. " +
                        "Jo mei Kuaschwanz wia ned woar pfenningguat. Wos griasd eich midnand hi om auf’n Gipfe des " +
                        "wiad a Mordsgaudi lem und lem lossn Weibaleid obacht mei ebba, in da. " +
                        "Xaver mechad Schorsch ned woar, mim Radl foahn in da da. Auf’d Schellnsau auszutzeln is des " +
                        "liab Griasnoggalsubbm wea ko, dea ko hob mei.").build())
                .add(CellText.builder().text("This is bottom left aligned")
                        .backgroundColor(Color.LIGHT_GRAY)
                        .verticalAlignment(VerticalAlignment.BOTTOM)
                        .build())
                .backgroundColor(Color.DARK_GRAY)
                .build());

        TestUtils.createAndSaveDocumentWithTable(tableBuilder.build(), "sampleDifferentFontsInCells.pdf");
    }

    @Test
    public void createRingManagerDocument() throws Exception {
        final PDDocument document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        final float startY = page.getMediaBox().getHeight() - 150;
        final int startX = 56;

        final PDPageContentStream contentStream = new PDPageContentStream(document, page);
        final Table table = getRingManagerTable();

        TableDrawer.builder().contentStream(contentStream).table(table).startX(startX).startY(startY).build().draw();

        contentStream.setFont(HELVETICA, 8.0f);
        contentStream.beginText();

        contentStream.newLineAtOffset(startX, startY - (table.getHeight() + 22));
        contentStream.showText("Dieser Kampf muss der WB nicht entsprechen, da als Sparringskampf angesetzt.");
        contentStream.endText();

        contentStream.close();

        document.save("target/ringmanager.pdf");
        document.close();
    }

    private Table getRingManagerTable() {
        final TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(26, 70, 390)
                .fontSize(9)
                .borderColor(Color.GRAY)
                .font(HELVETICA);

        final float borderWidthOuter = 1.5f;
        final float borderWidthInner = 1.0f;

        tableBuilder.addRow(Row.builder().add(
                CellText.builder().text("1.")
                        .borderWidthTop(borderWidthOuter)
                        .borderWidthLeft(borderWidthOuter)
                        .borderWidthRight(borderWidthInner)
                        .build())
                .add(
                        CellText.builder().text("WK DBV(s)")
                                .borderWidthTop(borderWidthOuter)
                                .borderWidthRight(borderWidthInner)
                                .borderWidthLeft(borderWidthInner)
                                .build())
                .add(
                        CellText.builder().text("Rote Ecke:")
                                .borderWidthTop(borderWidthOuter)
                                .borderWidthRight(borderWidthOuter)
                                .build()
                ).build());

        tableBuilder.addRow(Row.builder().add(
                CellText.builder().text("").borderWidthLeft(borderWidthOuter).build())
                .add(
                        CellText.builder().text("Jugend")
                                .borderWidthRight(borderWidthInner)
                                .borderWidthLeft(borderWidthInner)
                                .build())
                .add(
                        CellText.builder().text("Thomas Test, m, FC St. Pauli, 01.01.1998, Jugend, 67,5 kg, 12K (8S, 4N, 0U)")
                                .borderWidthBottom(borderWidthInner)
                                .borderWidthRight(borderWidthOuter)
                                .build())
                .build());

        tableBuilder.addRow(Row.builder().add(
                CellText.builder().text("").borderWidthLeft(borderWidthOuter).build()).add(
                CellText.builder().text("3x3")
                        .borderWidthRight(borderWidthInner)
                        .borderWidthLeft(borderWidthInner)
                        .build()).add(
                CellText.builder().text("Blaue Ecke:").borderWidthRight(borderWidthOuter).build()).build());

        tableBuilder.addRow(Row.builder().add(
                CellText.builder().text("")
                        .borderWidthLeft(borderWidthOuter)
                        .borderWidthBottom(borderWidthOuter)
                        .build()).add(
                CellText.builder().text("10 Uz, KS")
                        .borderWidthRight(borderWidthInner)
                        .borderWidthLeft(borderWidthInner)
                        .borderWidthBottom(borderWidthOuter)
                        .build()).add(
                CellText.builder().text("Bernd Beispiel, m, Wedeler TSV, 02.01.1999, Jugend, 68,2 kg, 9K (7S, 2N, 0U)")
                        .borderWidthBottom(borderWidthOuter)
                        .borderWidthRight(borderWidthOuter)
                        .build()).build());

        return tableBuilder.build();
    }

}
