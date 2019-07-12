package org.vandeseer.integrationtest;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.Test;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellVerticalText;

import java.io.IOException;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;

public class VerticalTextCellTest {

    @Test
    public void testVerticalTextCell() throws IOException {
        final PDDocument document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try (final PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

            TableDrawer.builder()
                    .contentStream(contentStream)
                    .table(createSimpleTable())
                    .startX(50)
                    .startY(page.getMediaBox().getHeight() - 50)
                    .build()
                    .draw();

        }

        document.save("target/cellVerticalText.pdf");
        document.close();
    }

    private static Table createSimpleTable() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 100, 100, 100)
                .fontSize(8)
                .font(HELVETICA);

        tableBuilder
            .addRow(Row.builder()
                .add(CellVerticalText.builder().minHeight(30).borderWidth(1).text("This is a super long text that does not fit in one line").build())
                .add(CellVerticalText.builder().minHeight(30).borderWidth(1).text("Two").build())
                .add(CellVerticalText.builder().minHeight(50).borderWidth(1).text("Three").build())
                .add(CellVerticalText.builder().minHeight(30).borderWidth(1).text("Four").build())
                .build())
            .addRow(Row.builder()
                .add(CellVerticalText.builder().borderWidth(1).text("One 1\nFubarbar").build())
                .add(CellVerticalText.builder().borderWidth(1).text("Two").build())
                .add(CellVerticalText.builder().borderWidth(1).text("Three").build())
                .add(CellVerticalText.builder().borderWidth(1).text("Four").build())
                .build());

        return tableBuilder.build();
    }

}
