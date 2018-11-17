package org.vandeseer.integrationtest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellText;

import java.awt.*;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.COURIER_BOLD;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_OBLIQUE;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;

public class SettingsOverridingTest {

    // TODO we should do a unit test first ;)
    @Test
    public void createSampleDocumentWithFontSettingOverriding() throws Exception {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 100, 100)
                .horizontalAlignment(CENTER)
                .fontSize(10).font(HELVETICA);

        tableBuilder.addRow(
                Row.builder()
                        .add(CellText.builder().text(RandomStringUtils.randomAlphabetic(23)).span(2).borderWidth(1).build())
                        .add(CellText.builder().text("Booz").build())
                        .font(COURIER_BOLD).fontSize(8)
                        .build());

        tableBuilder.addRow(
                Row.builder()
                        .add(CellText.builder().text("Pur").backgroundColor(Color.YELLOW).borderWidth(1).build())
                        .add(CellText.builder().text("Booz").build())
                        .add(CellText.builder().text("baz").build())
                        .font(COURIER_BOLD).fontSize(8).build());

        tableBuilder.addRow(
                Row.builder()
                        .add(CellText.builder().text("Pur").backgroundColor(Color.YELLOW).borderWidth(1).build())
                        .add(CellText.builder().text("Booz").build())
                        .add(CellText.builder().text("baz").font(HELVETICA_OBLIQUE).fontSize(5).build())
                        .build());

        TestUtils.createAndSaveDocumentWithTable(tableBuilder.build(), "fontSettingsOverriding.pdf");
    }

}
