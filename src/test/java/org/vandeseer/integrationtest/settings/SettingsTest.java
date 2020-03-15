package org.vandeseer.integrationtest.settings;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.*;
import static org.vandeseer.TestUtils.getActualPdfFor;
import static org.vandeseer.TestUtils.getExpectedPdfFor;
import static org.vandeseer.easytable.settings.HorizontalAlignment.*;
import static org.vandeseer.easytable.settings.VerticalAlignment.BOTTOM;
import static org.vandeseer.easytable.settings.VerticalAlignment.TOP;

// TODO test border color on row level
// TODO test the precedence of cell level settings of row level/table level settings
public class SettingsTest {

    private final static Color PURPLE_LIGHT_1 = new Color(206, 186, 230);
    private final static Color PURPLE_LIGHT_2 = new Color(230, 218, 242);

    private static final String FILE_NAME = "differentFontsInCells.pdf";

    @Test
    public void differentSettings() throws IOException {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(170, 170, 150)
                .fontSize(8)
                .font(HELVETICA)
                .wordBreak(true);

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text("This is top right aligned without a border." +
                        "It has a tiny font and a pretty big padding")
                        .horizontalAlignment(RIGHT)
                        .verticalAlignment(TOP)
                        .fontSize(5)
                        .padding(20)
                        .build())
                .add(TextCell.builder().text("Another cell with a" +
                        " useless story, because I am to lazy to get a lorem-ipsum. It is bottom aligned.")
                        .lineSpacing(1.8f)
                        .padding(30)
                        .verticalAlignment(BOTTOM)
                        .backgroundColor(PURPLE_LIGHT_1)
                        .build())
                .add(TextCell.builder().text("This is center and middle aligned with a line spacing of 1.1")
                        .verticalAlignment(VerticalAlignment.MIDDLE)
                        .horizontalAlignment(CENTER)
                        .lineSpacing(1.1f)
                        .font(TIMES_ROMAN)
                        .build())
                .backgroundColor(PURPLE_LIGHT_2)
                .height(200f)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text("This is left bottom aligned.")
                        .horizontalAlignment(LEFT)
                        .verticalAlignment(BOTTOM)
                        .textColor(Color.BLACK)
                        .font(HELVETICA_OBLIQUE)
                        .fontSize(16)
                        .padding(30)
                        .build())
                .add(TextCell.builder().text("Bavaria ipsum dolor sit amet Schaung kost nix des Biagadn obandeln. " +
                        "Di gscheit des is hoid aso kummd Haberertanz heitzdog de Sonn des is a gmahde Wiesn, " +
                        "Jo mei Kuaschwanz wia ned woar pfenningguat. Wos griasd eich midnand hi om auf’n Gipfe des " +
                        "wiad a Mordsgaudi lem und lem lossn Weibaleid obacht mei ebba, in da. " +
                        "liab Griasnoggalsubbm wea ko, dea ko hob mei.")
                        .padding(20)
                        .backgroundColor(Color.DARK_GRAY)
                        .horizontalAlignment(JUSTIFY)
                        .textColor(Color.WHITE)
                        .lineSpacing(0.9f)
                        .font(COURIER_BOLD)
                        .build())
                .add(TextCell.builder().text("This is bottom right aligned and does have a padding of 10, " +
                        "but a line spacing of 0.6")
                        .verticalAlignment(BOTTOM)
                        .horizontalAlignment(RIGHT)
                        .padding(10)
                        .lineSpacing(0.6f)
                        .build())
                .backgroundColor(Color.LIGHT_GRAY)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(TextCell.builder().text("Fubar! Top right!")
                        .horizontalAlignment(RIGHT)
                        .verticalAlignment(TOP)
                        .textColor(Color.BLACK)
                        .font(PDType1Font.TIMES_ITALIC)
                        .fontSize(14)
                        .padding(8)
                        .build())
                .add(TextCell.builder().text("Here\nwe\nhave\n\nseveral\nnew\nlines")
                        .padding(20)
                        .backgroundColor(PURPLE_LIGHT_1)
                        .lineSpacing(0.9f)
                        .font(COURIER_BOLD)
                        .build())
                .add(TextCell.builder().text("Bottom. Center.")
                        .verticalAlignment(BOTTOM)
                        .horizontalAlignment(CENTER)
                        .padding(14)
                        .lineSpacing(0.6f)
                        .build())
                .backgroundColor(PURPLE_LIGHT_2)
                .build());

        TestUtils.createAndSaveDocumentWithTable(FILE_NAME, tableBuilder.build());

        CompareResult compareResult = new PdfComparator<>(getExpectedPdfFor(FILE_NAME), getActualPdfFor(FILE_NAME)).compare();
        assertTrue(compareResult.isEqual());
    }


}
