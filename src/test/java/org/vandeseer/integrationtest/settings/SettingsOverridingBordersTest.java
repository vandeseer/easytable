package org.vandeseer.integrationtest.settings;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.settings.BorderStyle;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;

import static java.awt.Color.BLACK;
import static java.awt.Color.GRAY;
import static org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName.*;
import static org.junit.Assert.assertTrue;
import static org.vandeseer.TestUtils.getActualPdfFor;
import static org.vandeseer.TestUtils.getExpectedPdfFor;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;
import static org.vandeseer.easytable.settings.VerticalAlignment.MIDDLE;

public class SettingsOverridingBordersTest {

    public static final String FILE_NAME = "settingsOverridingBorders.pdf";

    @Test
    public void createDocumentWithTables() throws Exception {
        TestUtils.createAndSaveDocumentWithTables(FILE_NAME,
                createTableWithFontSettingAndBorderColorOverriding()
        );

        CompareResult compareResult = new PdfComparator<>(getExpectedPdfFor(FILE_NAME), getActualPdfFor(FILE_NAME)).compare();
        assertTrue(compareResult.isEqual());
    }

    private Table createTableWithFontSettingAndBorderColorOverriding() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 100, 100)
                .padding(1f)
                .borderWidth(2)
                .borderStyle(BorderStyle.DOTTED)
                .horizontalAlignment(CENTER)
                .fontSize(16).font(new PDType1Font(HELVETICA))
                .borderColor(Color.RED);

        tableBuilder.addRow(
                Row.builder()
                        .add(
                                TextCell.builder()
                                        .text("AbcDef")
                                        .colSpan(2)
                                        .borderWidth(1)
                                        .build()
                        )
                        .add(
                                TextCell.builder()
                                        .text("Hij klmn")
                                        .build()
                        )
                        .borderWidth(3)
                        .borderStyle(BorderStyle.SOLID)
                        .font(new PDType1Font(COURIER_BOLD)).fontSize(12)
                        .build()
        );

        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder().text("Puro").backgroundColor(Color.YELLOW).build())
                        .add(TextCell.builder().text("Boozoo").build())
                        .add(TextCell.builder().text("bazoo").build())
                        .borderWidth(2.4f)
                        .borderStyle(BorderStyle.SOLID)
                        .borderColor(BLACK)
                        .font(new PDType1Font(COURIER_BOLD))
                        .fontSize(12)
                        .build()
        );

        tableBuilder.addRow(
                Row.builder()
                        .add(
                                TextCell.builder()
                                        .text("Pur")
                                        .borderWidth(0)
                                        .borderWidthRight(1)
                                        .borderWidthBottom(1)
                                        .backgroundColor(Color.YELLOW)
                                        .build()
                        )
                        .add(
                                TextCell.builder()
                                        .text("Heyho")
                                        .borderWidth(0)
                                        .borderWidthBottom(2)
                                        .build()
                        )
                        .add(
                                TextCell.builder()
                                        .text("BazBaz")
                                        .padding(22f)
                                        .borderWidthBottom(1.5f)
                                        .borderStyle(BorderStyle.SOLID)
                                        .build()
                        )
                        .borderWidth(0.2f)
                        .borderStyle(BorderStyle.DASHED)
                        .borderColor(Color.BLUE)
                        .font(new PDType1Font(COURIER_BOLD)).fontSize(12).build());

        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder().text("Pur").backgroundColor(Color.YELLOW).borderWidth(1).build())
                        .add(
                                TextCell.builder()
                                        .text("Booz2")
                                        .borderWidth(0)
                                        .borderWidthBottom(4)
                                        .borderStyle(BorderStyle.SOLID)
                                        .padding(6)
                                        .build()
                        )
                        .add(
                                TextCell.builder()
                                        .text("bazBoo")
                                        .borderColor(GRAY)
                                        .borderWidth(0)
                                        .borderWidthBottom(1)
                                        .borderStyle(BorderStyle.DASHED)
                                        .font(new PDType1Font(HELVETICA_OBLIQUE))
                                        .fontSize(5)
                                        .verticalAlignment(MIDDLE)
                                        .build()
                        )
                        .build());

        return tableBuilder.build();
    }

}
