package org.vandeseer.integrationtest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellText;

import java.awt.*;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.*;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;

public class SettingsOverridingTest {

    @Test
    public void createSampleDocumentWithFontSettingAndBorderColorOverriding() throws Exception {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 100, 100)
                .horizontalAlignment(CENTER)
                .fontSize(10).font(HELVETICA)
                .borderColor(Color.RED);

        tableBuilder.addRow(
                Row.builder()
                        .add(CellText.builder().text(RandomStringUtils.randomAlphabetic(23)).span(2).borderWidth(1).build())
                        .add(CellText.builder().text("Booz").build())
                        .font(COURIER_BOLD).fontSize(8)
                        .build());

        tableBuilder.addRow(
                Row.builder()
                        .add(CellText.builder().text("Pur").backgroundColor(Color.YELLOW).build())
                        .add(CellText.builder().text("Booz").build())
                        .add(CellText.builder().text("baz").build())
                        .font(COURIER_BOLD).fontSize(8).build());

        tableBuilder.addRow(
                Row.builder()
                        .add(CellText.builder().text("Pur").borderWidthRight(1).borderWidthBottom(1).backgroundColor(Color.YELLOW).build())
                        .add(CellText.builder().text("Booz").borderWidthTop(1).borderWidthLeft(1).build())
                        .add(CellText.builder().text("baz").borderWidthBottom(1).build())
                        .borderColor(Color.GREEN)
                        .font(COURIER_BOLD).fontSize(8).build());

        tableBuilder.addRow(
                Row.builder()
                        .add(CellText.builder().text("Pur").backgroundColor(Color.YELLOW).borderWidth(1).build())
                        .add(CellText.builder().text("Booz").build())
                        .add(CellText.builder().text("baz").borderColor(Color.CYAN).borderWidthBottom(1).font(HELVETICA_OBLIQUE).fontSize(5).build())
                        .build());

        TestUtils.createAndSaveDocumentWithTable(tableBuilder.build(), "fontAndBorderColorSettingsOverriding.pdf");
    }

    @Test
    public void createSampleDocumentWithTextColorAndBackgroundColorOverriding() throws Exception {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 100, 100)
                .horizontalAlignment(CENTER)
                .fontSize(10).font(HELVETICA)
                .textColor(Color.GREEN)
                .backgroundColor(Color.LIGHT_GRAY);

        tableBuilder.addRow(
                Row.builder()
                        .add(CellText.builder().text(RandomStringUtils.randomAlphabetic(23)).span(2).borderWidth(1).build())
                        .add(CellText.builder().text("Booz").borderWidth(1).build())
                        .build());

        tableBuilder.addRow(
                Row.builder()
                        .add(CellText.builder().text("Pur").borderWidth(1).build())
                        .add(CellText.builder().text("Booz").borderWidth(1).build())
                        .add(CellText.builder().text("baz").borderWidth(1).build())
                        .backgroundColor(Color.YELLOW)
                        .textColor(Color.BLACK)
                        .build());

        tableBuilder.addRow(
                Row.builder()
                        .add(CellText.builder().text("Pur").borderWidth(1).backgroundColor(Color.ORANGE).build())
                        .add(CellText.builder().text("Booz").borderWidth(1).build())
                        .add(CellText.builder().text("baz").borderWidth(1).textColor(Color.PINK).build())
                        .build());

        TestUtils.createAndSaveDocumentWithTable(tableBuilder.build(), "textColorAndBackgroundOverriding.pdf");
    }

}
