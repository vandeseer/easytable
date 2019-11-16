package org.vandeseer.integrationtest;

import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.LinkedTextCell;
import org.vandeseer.easytable.structure.cell.LinkedTextCell.LinkedText;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.io.IOException;
import java.net.URL;

public class LinkedTextCellTest {

    @Test
    public void testLinkedTextCell() throws IOException {
        Table table = Table.builder().addColumnsOfWidth(200, 300)
                .addRow(Row.builder()
                        .add(TextCell.builder().text("baz").borderWidth(1).build())
                        .add(LinkedTextCell.builder().linkedText(
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
                                    .append("hey ho ")
                                    .append("new link", new URL("http://www.apache.com"))
                                    .append(" ha! ")
                                    .append("new link", new URL("http://www.apache.com"))
                                    .append(" ho. ho.")
                                .build()).borderWidth(1).build())
                        .build())
                .build();

        TestUtils.createAndSaveDocumentWithTables("linkedTextCell.pdf", table);
    }

}
