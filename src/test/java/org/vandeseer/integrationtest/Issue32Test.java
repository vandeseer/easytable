package org.vandeseer.integrationtest;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellImage;
import org.vandeseer.easytable.structure.cell.CellText;

import java.io.IOException;

import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;
import static org.vandeseer.easytable.settings.VerticalAlignment.MIDDLE;

public class Issue32Test {

    // TODO do a test for each vertical alignment ...
    // TODO do a test for each horizontal alignment ...

    @Test
    public void test() throws IOException {
        buildTable();
    }


    private void buildTable() throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();

        float userPageWidth = page.getMediaBox().getWidth() - (2.0F * 72.0f);

        float columnWidth = userPageWidth / 8.0F;

        Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(columnWidth, columnWidth, columnWidth, columnWidth,
                        columnWidth, columnWidth, columnWidth, columnWidth)
                .fontSize(8)
                .font(PDType1Font.HELVETICA)
                .wordBreak(true);

        Row.RowBuilder rowBuilder = Row.builder()
                .add(CellText.builder()
                        .borderWidth(1)
                        .padding(4)
                        .text("Cell 1")
                        .build())
                .add(CellText.builder()
                        .borderWidth(1)
                        .padding(4)
                        .text("Cell 2")
                        .build())
                .add(CellText.builder()
                        .borderWidth(1)
                        .padding(4)
                        .text("Cell 3")
                        .build())
                .add(CellText.builder()
                        .colSpan(4)
                        .borderWidth(1)
                        .padding(4)
                        .colSpan(2)
                        .text("Cell 4\nMore text\nEven more text\nEven more text\n" +
                                "Even more text\nEven more text\nEven more text\n" +
                                "Even more text\nEven more text\nEven more text\nEven more text")
                        .build())
                .add(CellText.builder()
                        .borderWidth(1)
                        .padding(4)
                        .text("Logo")
                        .build());
        try {
            rowBuilder.add(CellImage.builder()
                    .verticalAlignment(MIDDLE) // TODO parameterise for all three alignments
                    .horizontalAlignment(CENTER) // TODO parameterise for all three alignments
                    .borderWidth(1)
                    .padding(2)
                    .image(TestUtils.createGliderImage())
                    .colSpan(2)
                    .build());
        } catch (IOException e) {
            rowBuilder.add(CellText.builder()
                    .borderWidth(1)
                    .padding(4)
                    .text("Logo goes here")
                    .colSpan(2)
                    .build());
        }

        tableBuilder.addRow(rowBuilder.font(PDType1Font.HELVETICA)
                .fontSize(8)
                .horizontalAlignment(CENTER)
                .build());

        Table tableHeader = tableBuilder.build();

        try (final PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            TableDrawer.builder()
                    .contentStream(contentStream)
                    .table(tableHeader)
                    .startX(72.0f)
                    .startY(page.getMediaBox().getHeight() - 72.0f)
                    .build()
                    .draw();
        }

        document.addPage(page);

        document.save("target/issue32.pdf");
        document.close();
    }
}


