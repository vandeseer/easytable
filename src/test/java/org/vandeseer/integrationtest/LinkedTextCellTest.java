package org.vandeseer.integrationtest;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.LinkedTextCell;
import org.vandeseer.easytable.structure.cell.LinkedTextCell.LinkedText;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class LinkedTextCellTest {

    @Test
    public void testLinkedTextCell() throws IOException {
        List<Table> tables = new LinkedList<>();

        Table.TableBuilder tableBuilder = Table.builder().addColumnsOfWidth(200, 320);
        tableBuilder.fontSize(14);

        for (VerticalAlignment verticalAlignment : VerticalAlignment.values()) {
            for (HorizontalAlignment horizontalAlignment : HorizontalAlignment.values()) {
                for (PDFont font : new HashSet<>(Arrays.asList(PDType1Font.COURIER_BOLD, PDType1Font.HELVETICA))) {

                    tableBuilder
                            .addRow(Row.builder()
                                    .font(font)
                                    .add(TextCell.builder().text("baz").borderWidth(1).build())
                                    .add(buildLinkTextCellAligned(horizontalAlignment, verticalAlignment))
                                    .build());
                }
            }
        }

        tables.add(tableBuilder.build());
        TestUtils.createAndSaveDocumentWithTables("linkedTextCell.pdf", tables);
    }

    @Test
    public void testLinkedTextCellLineBreak() throws IOException {
        List<Table> tables = new LinkedList<>();

            for (HorizontalAlignment horizontalAlignment : HorizontalAlignment.values()) {

                tables.add(Table.builder().addColumnsOfWidth(200, 100)
                        .addRow(Row.builder()
                                .add(TextCell.builder().text("baz").borderWidth(1).build())
                                .add(LinkedTextCell.builder()
                                        .minHeight(100f)
                                        .horizontalAlignment(horizontalAlignment)
                                        .linkedText(
                                                LinkedText.builder()
                                                        .append("fu bar ")
                                                        .append(
                                                                "superLongLinkThatMustBeSplitBecauseItIsTooLong",
                                                                new URL("http://www.stackoverflow.com")
                                                        )
                                                        .append(" baz ")
                                                        .append("last link", new URL("http://github.com"))
                                                        .build()).borderWidth(1).build())
                                .build())
                        .build());
            }

        TestUtils.createAndSaveDocumentWithTables("linkedTextCellLineBreak.pdf", tables);
    }

    private LinkedTextCell buildLinkTextCellAligned(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) throws MalformedURLException {
        return LinkedTextCell.builder()
                .minHeight(100f)
                .verticalAlignment(verticalAlignment)
                .horizontalAlignment(horizontalAlignment)
                .linkedText(
                LinkedText.builder()
                    .append("fu bar ")
                    .append("link", new URL("http://www.stackoverflow.com"))
                    .append(" baz ")
                    .append("link2", new URL("http://www.github.com"))
                    .append("\n")
                    .append("hey ho ")
                    .append("new link", new URL("http://www.apache.com"))
                    .append(" ha! ")
                    .append("new link", new URL("http://www.apache.com"))
                    .append(" ho. ho.")
                    .append("\n")
                    .append("a new ")
                    .append("link", new URL("http://www.stackoverflow.com"))
                    .append(" and yet another ")
                    .append("link2", new URL("http://www.github.com"))
                    .append(" and that's about it.")
                    .append("\n")
                    .append("hey ho this is very long so there is a line break for sure my friend ")
                    .append("new link", new URL("http://www.apache.com"))
                    .append(" ha! ")
                    .append("new link", new URL("http://www.apache.com"))
                    .append(" ho. ho.")
                    .append("last link", new URL("http://www.google.com"))
                .build()).borderWidth(1).build();
    }

}
