package org.vandeseer.integrationtest;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;
import org.vandeseer.easytable.structure.cell.paragraph.Hyperlink;
import org.vandeseer.easytable.structure.cell.paragraph.Markup;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

import java.awt.*;
import java.io.IOException;

import static java.awt.Color.*;
import static junit.framework.TestCase.assertTrue;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.*;
import static org.vandeseer.TestUtils.getActualPdfFor;
import static org.vandeseer.TestUtils.getExpectedPdfFor;
import static org.vandeseer.easytable.settings.HorizontalAlignment.*;
import static org.vandeseer.easytable.settings.VerticalAlignment.*;

public class ParagraphCellTest {

    private static final String FILE_NAME = "paragraphCell.pdf";

    @Test
    public void testParagraphCell() throws IOException {
        TestUtils.createAndSaveDocumentWithTables(FILE_NAME,
                createSimpleTable(), createParagraphTable()
        );

        CompareResult compareResult = new PdfComparator<>(getExpectedPdfFor(FILE_NAME), getActualPdfFor(FILE_NAME)).compare();
        assertTrue(compareResult.isEqual());
    }

    private Table createParagraphTable() {
        return Table.builder()
                .backwardsCompatibleFontHeight(true)
                .addColumnsOfWidth(200, 200)
                .borderColor(WHITE)
                .fontSize(8)
                .font(HELVETICA_BOLD_OBLIQUE)
                .addRow(Row.builder()
                        .backgroundColor(GRAY)
                        .textColor(WHITE)
                        .horizontalAlignment(CENTER)
                        .add(TextCell.builder().borderWidth(1).text("Markup").build())
                        .add(TextCell.builder().borderWidth(1).text("No Markup").build())
                        .build())
                .addRow(Row.builder()
                        .backgroundColor(LIGHT_GRAY)
                        .add(ParagraphCell.builder()
                            .borderWidth(1)
                            .padding(8)
                            .lineSpacing(1.2f)
                            .paragraph(Paragraph.builder()
                                .append(Markup.builder()
                                        .markup(
                                                "This is using __Markup__ where you can {color:#efefef}color your text, " +
                                                "{color:#000000}or also just *emphasize* whatever you think " +
                                                "should be *emphasized*). You can also *{color:#efefef}combine both*" +
                                                "{color:#000000}. Furthermore you can add links like this one " +
                                                "that is pointing to {link[https://github.com/ralfstuckert/pdfbox-layout/wiki/Markup]}" +
                                                "markup-information{link} and which is underlined."
                                        )
                                        .font(Markup.MarkupSupportedFont.TIMES)
                                        .build())
                                .build())
                            .build())
                        .add(ParagraphCell.builder()
                            .borderWidth(1)
                            .padding(8)
                            .lineSpacing(1.2f)
                            .paragraph(Paragraph.builder()
                                    .append(StyledText.builder().text("This is some text in one font.").font(HELVETICA).build())
                                    .appendNewLine()
                                    .append(StyledText.builder().text("But this text that introduces a link that follows is different. Here comes the link: ").font(COURIER_BOLD).fontSize(6f).build())
                                    .append(Hyperlink.builder().text("github!").url("http://www.github.com").font(COURIER_BOLD).fontSize(6f).color(WHITE).build())
                                    .appendNewLine(6f)
                                    .append(StyledText.builder().text("There was the link. And now we are using the default font from the cell.").build())
                                    .build())
                            .build())
                .build())
            .build();
    }

    private static Table createSimpleTable() {
        return Table.builder()
                .backwardsCompatibleFontHeight(true)
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
    private static Paragraph createParagraph1() {
        return Paragraph.builder()
                .append(StyledText.builder().text("Some people have an ability to write placeholder text... " +
                        "It's an art you're basically born with. You either have it or you don't. " +
                        "Look at that text! Would anyone use that? Can you imagine that, " +
                        "the text of your next webpage?! If Trump Ipsum weren’t my own words, " +
                        "perhaps I’d be dating it.").fontSize(6f).font(PDType1Font.COURIER).build())
                .build();
    }

    @SneakyThrows
    private static Paragraph createParagraph2() {
        return Paragraph.builder()
                .append(StyledText.builder().text("Some people have an ability to write placeholder text... " +
                        "\n\nIt's an art you're basically born with.").fontSize(6f).font(PDType1Font.COURIER).build())
                .build();
    }

    @SneakyThrows
    private static Paragraph createParagraph3() {
        return Paragraph.builder()
                .append(StyledText.builder().text("This is some ").fontSize(11f).font(PDType1Font.COURIER).build())
                .append(StyledText.builder().text("simple example ").fontSize(20f).font(PDType1Font.HELVETICA_BOLD_OBLIQUE).build())
                .append(StyledText.builder().text("text").fontSize(7f).font(PDType1Font.HELVETICA).build())
                .append(Markup.builder().markup("This is a link to {link[http://www.pdfbox.org]}PDFBox{link}").fontSize(11f).font(Markup.MarkupSupportedFont.HELVETICA).build())
                .append(Hyperlink.builder().font(HELVETICA).fontSize(18f).text("Pdfbox-Link").url("https://github.com/ralfstuckert/pdfbox-layout").build())
                .build();
    }

    private static Paragraph createParagraph4() {
        return Paragraph.builder()
                .append(StyledText.builder().text("This placeholder text is gonna be HUGE.\n" +
                        "You have so many different things placeholder " +
                        "text has to be able to do, and I don't believe Lorem " +
                        "Ipsum has the stamina.").build())
                .build();
    }

    private static Paragraph createParagraph5() {
        return Paragraph.builder()
                .append(StyledText.builder().text("This placeholder text is gonna be ").build())
                .append(StyledText.builder().text("HUGE").font(COURIER_BOLD_OBLIQUE).build())
                .append(StyledText.builder().text(". And there is also ").build())
                .append(StyledText.builder().text("COLOR!").color(Color.RED).font(COURIER_BOLD_OBLIQUE).build())
                .build();
    }

}
