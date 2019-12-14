package org.vandeseer.integrationtest;

import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.paragraph.Hyperlink;
import org.vandeseer.easytable.structure.cell.paragraph.Markup;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import java.awt.*;
import java.io.IOException;

import static java.awt.Color.GRAY;
import static java.awt.Color.LIGHT_GRAY;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.*;
import static org.vandeseer.easytable.settings.HorizontalAlignment.*;
import static org.vandeseer.easytable.settings.VerticalAlignment.*;

public class ParagraphCellTest {

    @Test
    public void testParagraphCell() throws IOException {
        TestUtils.createAndSaveDocumentWithTables("paragraphCell.pdf",
                createSimpleTable()
        );
    }

    private static Table createSimpleTable() throws IOException {
        return Table.builder()
                .addColumnsOfWidth(120, 120, 120, 120)
                .fontSize(8)
                .font(HELVETICA)
                .addRow(Row.builder()
                        .add(ParagraphCell.builder().lineSpacing(2f).borderWidth(1).paragraph(createParagraph1()).build())
                        .add(ParagraphCell.builder().borderWidth(1).verticalAlignment(TOP).paragraph(createParagraph2()).build())
                        .add(ParagraphCell.builder().borderWidth(1).verticalAlignment(MIDDLE).paragraph(createParagraph2()).build())
                        .add(ParagraphCell.builder().borderWidth(1).verticalAlignment(BOTTOM).paragraph(createParagraph2()).build())
                        .build())
                .addRow(Row.builder()
                        .add(ParagraphCell.builder().horizontalAlignment(RIGHT).backgroundColor(GRAY).borderWidth(1).paragraph(createParagraph3()).build())
                        .add(ParagraphCell.builder().horizontalAlignment(LEFT).backgroundColor(LIGHT_GRAY).borderWidth(1).paragraph(createParagraph3()).build())
                        .add(ParagraphCell.builder().horizontalAlignment(CENTER).backgroundColor(GRAY).borderWidth(1).paragraph(createParagraph3()).build())
                        .add(ParagraphCell.builder().horizontalAlignment(JUSTIFY).backgroundColor(LIGHT_GRAY).borderWidth(1).paragraph(createParagraph3()).build())
                        .build())
                .addRow(Row.builder()
                        .font(COURIER)
                        .fontSize(5)
                        .add(ParagraphCell.builder().font(PDType1Font.TIMES_ROMAN).horizontalAlignment(LEFT).backgroundColor(GRAY).borderWidth(1).paragraph(createParagraph4()).build())
                        .add(ParagraphCell.builder().horizontalAlignment(CENTER).backgroundColor(LIGHT_GRAY).borderWidth(1).paragraph(createParagraph4()).build())
                        .add(ParagraphCell.builder().textColor(Color.WHITE).horizontalAlignment(JUSTIFY).backgroundColor(GRAY).borderWidth(1).paragraph(createParagraph4()).build())
                        .add(ParagraphCell.builder().fontSize(8).horizontalAlignment(RIGHT).backgroundColor(LIGHT_GRAY).borderWidth(1).paragraph(createParagraph5()).build())
                        .build())
                .build();
    }

    @SneakyThrows
    private static ParagraphCell.Paragraph createParagraph1() {
        return ParagraphCell.Paragraph.builder()
                .append(StyledText.builder().text("Some people have an ability to write placeholder text... " +
                        "It's an art you're basically born with. You either have it or you don't. " +
                        "Look at that text! Would anyone use that? Can you imagine that, " +
                        "the text of your next webpage?! If Trump Ipsum weren’t my own words, " +
                        "perhaps I’d be dating it.").fontSize(6f).font(PDType1Font.COURIER).build())
                .build();
    }

    @SneakyThrows
    private static ParagraphCell.Paragraph createParagraph2() {
        return ParagraphCell.Paragraph.builder()
                .append(StyledText.builder().text("Some people have an ability to write placeholder text... " +
                        "\n\nIt's an art you're basically born with.").fontSize(6f).font(PDType1Font.COURIER).build())
                .build();
    }

    @SneakyThrows
    private static ParagraphCell.Paragraph createParagraph3() {
        return ParagraphCell.Paragraph.builder()
                .append(StyledText.builder().text("This is some ").fontSize(11f).font(PDType1Font.COURIER).build())
                .append(StyledText.builder().text("simple example ").fontSize(20f).font(PDType1Font.HELVETICA_BOLD_OBLIQUE).build())
                .append(StyledText.builder().text("text").fontSize(7f).font(PDType1Font.HELVETICA).build())
                .append(Markup.builder().markup("This is a link to {link[http://www.pdfbox.org]}PDFBox{link}").fontSize(11f).font(Markup.MarkupSupportedFont.HELVETICA).build())
                .append(Hyperlink.builder().font(HELVETICA).fontSize(18f).text("Pdfbox-Link").url("https://github.com/ralfstuckert/pdfbox-layout").build())
                .build();
    }

    private static ParagraphCell.Paragraph createParagraph4() {
        return ParagraphCell.Paragraph.builder()
                .append(StyledText.builder().text("This placeholder text is gonna be HUGE.\n" +
                        "You have so many different things placeholder " +
                        "text has to be able to do, and I don't believe Lorem " +
                        "Ipsum has the stamina.").build())
                .build();
    }

    private static ParagraphCell.Paragraph createParagraph5() {
        return ParagraphCell.Paragraph.builder()
                .append(StyledText.builder().text("This placeholder text is gonna be ").build())
                .append(StyledText.builder().text("HUGE").font(COURIER_BOLD_OBLIQUE).build())
                .append(StyledText.builder().text(". And there is also ").build())
                .append(StyledText.builder().text("COLOR!").color(Color.RED).font(COURIER_BOLD_OBLIQUE).build())
                .build();
    }

}
