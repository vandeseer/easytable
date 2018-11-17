package org.vandeseer.integrationtest;

import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellText;

import java.awt.*;
import java.io.IOException;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.*;
import static org.vandeseer.easytable.settings.HorizontalAlignment.LEFT;
import static org.vandeseer.easytable.settings.HorizontalAlignment.RIGHT;
import static org.vandeseer.easytable.settings.VerticalAlignment.BOTTOM;

// TODO test border color on row level
// TODO test the precedence of cell level settings of row level/table level settings
// TODO can we somehow put the "cursor" directly to where the content stream is right now?!
public class SettingsTest {

    private final static Color PURPLE_LIGHT_1 = new Color(206, 186, 230);
    private final static Color PURPLE_LIGHT_2 = new Color(230, 218, 242);

    @Test
    public void differentSettings() throws IOException {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(200, 200, 70)
                .fontSize(8)
                .font(HELVETICA)
                .wordBreak(true);

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("This is top right aligned without a border." +
                        "It has a tiny font and a pretty big padding")
                        .horizontalAlignment(RIGHT)
                        .verticalAlignment(VerticalAlignment.TOP)
                        .fontSize(5)
                        .padding(20)
                        .build())
                .add(CellText.builder().text("And this is another cell with a very long long long text that tells a nice" +
                        " and useless story, because Iam to lazy to get a lorem-ipsum. It is bottom aligned.")
                        .lineSpacing(1.8f)
                        .padding(50)
                        .verticalAlignment(BOTTOM)
                        .backgroundColor(PURPLE_LIGHT_1)
                        .build())
                .add(CellText.builder().text("This is center and middle aligned with a line spacing of 1.1")
                        .verticalAlignment(VerticalAlignment.MIDDLE)
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .lineSpacing(1.1f)
                        .font(TIMES_ROMAN)
                        .build())
                .backgroundColor(PURPLE_LIGHT_2)
                .height(250)
                .build());

        tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text("This is left bottom aligned.")
                        .horizontalAlignment(LEFT)
                        .verticalAlignment(BOTTOM)
                        .textColor(Color.BLACK)
                        .font(HELVETICA_OBLIQUE)
                        .fontSize(16)
                        .padding(30)
                        .build())
                .add(CellText.builder().text("Bavaria ipsum dolor sit amet Schaung kost nix des Biagadn obandeln. " +
                        "Di gscheit des is hoid aso kummd Haberertanz heitzdog de Sonn des is a gmahde Wiesn, " +
                        "Charivari: Kimmt gscheid singd Buam Leonhardifahrt da pfundig gar nia need. " +
                        "Jo mei Kuaschwanz wia ned woar pfenningguat. Wos griasd eich midnand hi om auf’n Gipfe des " +
                        "wiad a Mordsgaudi lem und lem lossn Weibaleid obacht mei ebba, in da. " +
                        "Xaver mechad Schorsch ned woar, mim Radl foahn in da da. Auf’d Schellnsau auszutzeln is des " +
                        "liab Griasnoggalsubbm wea ko, dea ko hob mei.")
                        .padding(20)
                        .backgroundColor(Color.DARK_GRAY)
                        .textColor(Color.WHITE)
                        .lineSpacing(0.9f)
                        .font(COURIER_BOLD)
                        .build())
                .add(CellText.builder().text("This is bottom right aligned and does have a padding of 10, " +
                        "but a line spacing of 0.6")
                        .verticalAlignment(BOTTOM)
                        .horizontalAlignment(RIGHT)
                        .padding(10)
                        .lineSpacing(0.6f)
                        .build())
                .backgroundColor(Color.LIGHT_GRAY)
                .build());

        TestUtils.createAndSaveDocumentWithTable(tableBuilder.build(), "differentFontsInCells.pdf");
    }


}
