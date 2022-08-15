package org.vandeseer.integrationtest.settings;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Column;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
import static org.vandeseer.TestUtils.getActualPdfFor;
import static org.vandeseer.TestUtils.getExpectedPdfFor;

public class SettingsColumnTest {

    private final static Color PURPLE_LIGHT_1 = new Color(206, 186, 230);
    private final static Color PURPLE_LIGHT_2 = new Color(230, 218, 242);

    private static final String FILE_NAME = "settingsColumnSettings.pdf";

    @Test
    public void differentSettings() throws IOException {
        final Table.TableBuilder tableBuilder = Table.builder()
                .backwardsCompatibleFontHeight(true)
                .addColumn(
                        Column.builder()
                                .width(100)
                                .borderWidthLeft(2)
                                .borderColorLeft(Color.BLUE)
                                .backgroundColor(PURPLE_LIGHT_1)
                                .build()
                )
                .addColumn(Column.builder().width(100).fontSize(13).build())
                .addColumn(Column.builder().width(100).backgroundColor(PURPLE_LIGHT_2).build())
                .fontSize(8)
                .font(HELVETICA)
                .wordBreak(true);

        for (int i = 0; i < 10; i++) {
            tableBuilder.addRow(
                    Row.builder()
                            .add(TextCell.builder().text("abcd").build())
                            .add(TextCell.builder().text("abcd").build())
                            .add(TextCell.builder().text("abcd").build())
                            .build()
            );
        }

        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder().text("abcd").build())
                        .add(TextCell.builder().text("abcd").build())
                        .add(TextCell.builder().text("abcd").build())
                        .backgroundColor(Color.GREEN)
                        .build()
        );

        TestUtils.createAndSaveDocumentWithTable(FILE_NAME, tableBuilder.build());

        CompareResult compareResult = new PdfComparator<>(getExpectedPdfFor(FILE_NAME), getActualPdfFor(FILE_NAME)).compare();
        assertTrue(compareResult.isEqual());
    }


}
