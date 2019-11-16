package org.vandeseer.integrationtest;

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
import java.util.LinkedList;
import java.util.List;

public class LinkedTextCellTest {

    // TODO Test different alignemnts (horizontal, vertical)
    // TODO Test different fonts! (sans, serif etc.)
    // TODO Test line breaks (again with different alignments and different fonts!)

    // TODO Extend Test Util, that it automatically creates new pages

    @Test
    public void testLinkedTextCell() throws IOException {
        List<Table> tables = new LinkedList<>();

        for (VerticalAlignment verticalAlignment : VerticalAlignment.values()) {
            //for (HorizontalAlignment horizontalAlignment : HorizontalAlignment.values()) {

                tables.add(Table.builder().addColumnsOfWidth(200, 300)
                        .addRow(Row.builder()
                                .add(TextCell.builder().text("baz").borderWidth(1).build())
                                .add(buildLinkTextCellAligned(HorizontalAlignment.LEFT, verticalAlignment))
                                .build())
                        .build());
            //}
        }

        TestUtils.createAndSaveDocumentWithTables("linkedTextCell.pdf", tables.toArray(new Table[0]));
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
                .build()).borderWidth(1).build();
    }

}
