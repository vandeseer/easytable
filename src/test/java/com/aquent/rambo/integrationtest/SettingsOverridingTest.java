package com.aquent.rambo.integrationtest;

import org.junit.Test;

import com.aquent.rambo.TestUtils;
import com.aquent.rambo.easytable.structure.Row;
import com.aquent.rambo.easytable.structure.Table;
import com.aquent.rambo.easytable.structure.cell.TextCell;

import java.awt.*;

import static com.aquent.rambo.easytable.settings.HorizontalAlignment.CENTER;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.*;

public class SettingsOverridingTest {

    @Test
    public void createDocumentWithTables() throws Exception {
        TestUtils.createAndSaveDocumentWithTables("settingsOverriding.pdf",
                createTableWithFontSettingAndBorderColorOverriding(),
                createTableWithTextColorAndBackgroundColorOverriding(),
                createSampleDocumentWithWordBreakOverriding()
        );
    }


    private Table createTableWithFontSettingAndBorderColorOverriding() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 100, 100)
                .horizontalAlignment(CENTER)
                .fontSize(10).font(HELVETICA)
                .borderColor(Color.RED);

        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder().text("FCmjGVylqCjoxxfFWhehSrm").colSpan(2).borderWidth(1).build())
                        .add(TextCell.builder().text("Booz").build())
                        .font(COURIER_BOLD).fontSize(8)
                        .build());

        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder().text("Pur").backgroundColor(Color.YELLOW).build())
                        .add(TextCell.builder().text("Booz").build())
                        .add(TextCell.builder().text("baz").build())
                        .font(COURIER_BOLD).fontSize(8).build());

        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder().text("Pur").borderWidthRight(1).borderWidthBottom(1).backgroundColor(Color.YELLOW).build())
                        .add(TextCell.builder().text("Booz").borderWidthTop(1).borderWidthLeft(1).build())
                        .add(TextCell.builder().text("baz").borderWidthBottom(1).build())
                        .borderColor(Color.GREEN)
                        .font(COURIER_BOLD).fontSize(8).build());

        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder().text("Pur").backgroundColor(Color.YELLOW).borderWidth(1).build())
                        .add(TextCell.builder().text("Booz").build())
                        .add(TextCell.builder().text("baz").borderColor(Color.CYAN).borderWidthBottom(1).font(HELVETICA_OBLIQUE).fontSize(5).build())
                        .build());

        return tableBuilder.build();
    }

    private Table createTableWithTextColorAndBackgroundColorOverriding() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 100, 100)
                .horizontalAlignment(CENTER)
                .fontSize(10).font(HELVETICA)
                .textColor(Color.GREEN)
                .backgroundColor(Color.LIGHT_GRAY);

        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder().text("OWpTlEgQPoSmoyjdNcQdVbc").colSpan(2).borderWidth(1).build())
                        .add(TextCell.builder().text("Booz").borderWidth(1).build())
                        .build());

        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder().text("Pur").borderWidth(1).build())
                        .add(TextCell.builder().text("Booz").borderWidth(1).build())
                        .add(TextCell.builder().text("baz").borderWidth(1).build())
                        .backgroundColor(Color.YELLOW)
                        .textColor(Color.BLACK)
                        .build());

        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder().text("Pur").borderWidth(1).backgroundColor(Color.ORANGE).build())
                        .add(TextCell.builder().text("Booz").borderWidth(1).build())
                        .add(TextCell.builder().text("baz").borderWidth(1).textColor(Color.PINK).build())
                        .build());

        return tableBuilder.build();
    }

    private Table createSampleDocumentWithWordBreakOverriding() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(150, 150, 150)
                .horizontalAlignment(CENTER);

        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder().text("This is long and should therefore break. This is long and should therefore break. This is long and should therefore break.")
                                .colSpan(2).borderWidth(1).wordBreak(true).build())
                        .add(TextCell.builder().text("This, too, is too long for not breaking.").borderWidth(1).build())
                        .wordBreak(false)
                        .build());

        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder().text("This should break, because the row uses overriding.").borderWidth(1).build())
                        .add(TextCell.builder().text("This uses the overriding and should hence not break!").borderWidth(1).wordBreak(false).build())
                        .add(TextCell.builder().text("This should break, because the row uses overriding.").borderWidth(1).build())
                        .build());

        tableBuilder.addRow(
                Row.builder()
                        .add(TextCell.builder().text("This should break, because the table uses overriding.").borderWidth(1).build())
                        .add(TextCell.builder().text("This should break, because the table uses overriding.").borderWidth(1).build())
                        .add(TextCell.builder().text("This should break, because the table uses overriding.").borderWidth(1).build())
                        .build());

        return tableBuilder.build();
    }

}
