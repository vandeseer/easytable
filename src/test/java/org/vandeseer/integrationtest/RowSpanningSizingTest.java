package org.vandeseer.integrationtest;

import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

public class RowSpanningSizingTest {

    @Test
    public void createDocumentWithTables() throws Exception {
        TestUtils.createAndSaveDocumentWithTables("rowSpanningSizing.pdf",
                createRegularTable(),
                createComplexTable1(),
                createComplexTable2(),
                createComplexTable3()
        );
    }

    private Table createRegularTable() {
        return Table.builder()
                .addColumnsOfWidth(50, 50)
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("1").build())
                        .add(TextCell.builder().borderWidth(1).text("fu bza asd fad fda dsafa afa fsdfs fdsfs fds").rowSpan(2).build())
                        .build())
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("2\n2").build())
                        .build())
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("fu").build())
                        .add(TextCell.builder().borderWidth(1).text("bar").build())
                        .build())
                .build();
    }

    private Table createComplexTable1() {
        return Table.builder()
                .addColumnsOfWidth(50, 50, 50, 50)
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("1").build())
                        .add(TextCell.builder().borderWidth(1).text("fu bza asd fad fda dsafa afa fsdfs fdsfs fds").rowSpan(3).build())
                        .add(TextCell.builder().borderWidth(1).text("3").build())
                        .add(TextCell.builder().borderWidth(1).text("fu bza asd fad fda dsafa afa fsdfs fdsfs fds even longer more breaks").rowSpan(2).build())
                        .build())
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("1").build())
                        .add(TextCell.builder().borderWidth(1).text("3").build())
                        .build())
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("fu").build())
                        .add(TextCell.builder().borderWidth(1).text("bar").build())
                        .add(TextCell.builder().borderWidth(1).text("baz").build())
                        .build())
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("1").build())
                        .add(TextCell.builder().borderWidth(1).text("2").build())
                        .add(TextCell.builder().borderWidth(1).text("3").build())
                        .add(TextCell.builder().borderWidth(1).text("4").build())
                        .build())
                .build();
    }

    private Table createComplexTable2() {
        return Table.builder()
                .addColumnsOfWidth(50, 50, 50, 50)
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("1").build())
                        .add(TextCell.builder().borderWidth(1).text("fu bza asd fad fda dsafa afa fsdfs fdsfs fds").rowSpan(3).build())
                        .add(TextCell.builder().borderWidth(1).text("3").build())
                        .add(TextCell.builder().borderWidth(1).text("fu bza asd fad fda dsafa afa fsdfs fdsfs fds even longer more breaks").rowSpan(2).build())
                        .build())
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("1").build())
                        .add(TextCell.builder().borderWidth(1).text("3").build())
                        .build())
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("fu").build())
                        .add(TextCell.builder().borderWidth(1).text("bar").build())
                        .add(TextCell.builder().borderWidth(1).text("baz baz baz baz bza bza baz").rowSpan(2).build())
                        .build())
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("1").build())
                        .add(TextCell.builder().borderWidth(1).text("2").build())
                        .add(TextCell.builder().borderWidth(1).text("3").build())
                        .build())
                .build();
    }

    private Table createComplexTable3() {
        return Table.builder()
                .addColumnsOfWidth(50, 50, 50, 50)
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("1").build())
                        .add(TextCell.builder().borderWidth(1).text("fu bza asd fad fda dsafa afa fsdfs fdsfs fds").rowSpan(3).build())
                        .add(TextCell.builder().borderWidth(1).text("3").build())
                        .add(TextCell.builder().borderWidth(1).text("4").build())
                        .build())
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("1").build())
                        .add(TextCell.builder().borderWidth(1).text("3").build())
                        .add(TextCell.builder().borderWidth(1).text("fu bza asd fad fda dsafa afa fsdfs fdsfs fds even longer more breaks").rowSpan(2).build())
                        .build())
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("fu").build())
                        .add(TextCell.builder().borderWidth(1).text("bar").build())
                        .build())
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("1").build())
                        .add(TextCell.builder().borderWidth(1).text("2").build())
                        .add(TextCell.builder().borderWidth(1).text("3").build())
                        .add(TextCell.builder().borderWidth(1).text("4").build())
                        .build())
                .build();
    }

}
