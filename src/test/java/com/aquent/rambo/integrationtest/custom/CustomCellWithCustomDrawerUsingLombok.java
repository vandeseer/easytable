package com.aquent.rambo.integrationtest.custom;

import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import com.aquent.rambo.easytable.TableDrawer;
import com.aquent.rambo.easytable.drawing.Drawer;
import com.aquent.rambo.easytable.drawing.DrawingContext;
import com.aquent.rambo.easytable.drawing.PositionedStyledText;
import com.aquent.rambo.easytable.drawing.cell.TextCellDrawer;
import com.aquent.rambo.easytable.structure.Row;
import com.aquent.rambo.easytable.structure.Table;
import com.aquent.rambo.easytable.structure.cell.TextCell;

import java.io.IOException;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;

public class CustomCellWithCustomDrawerUsingLombok {

    @SuperBuilder
    private static class MyCustomCell extends TextCell {

        @Override
        public Drawer getDrawer() {
            return new TextCellDrawer(this) {
                @Override
                protected void drawText(DrawingContext drawingContext, PositionedStyledText text) throws IOException {
                    System.out.println("My custom drawer is called :-)");
                    super.drawText(drawingContext, text.toBuilder().text(text.getText().toUpperCase()).build());
                }
            };
        }

    }

    public static void main(String[] args) throws IOException {

        try (final PDDocument document = new PDDocument()) {

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

            document.save("target/customCellDrawer.pdf");
        }

    }

    private static Table createSimpleTable() {
        return Table.builder()
                .addColumnsOfWidth(100, 100, 100, 100)
                .fontSize(8)
                .font(HELVETICA)
                .addRow(Row.builder()
                        .add(MyCustomCell.builder().borderWidth(1).text("One").build())
                        .add(MyCustomCell.builder().borderWidth(1).text("Two").build())
                        .add(MyCustomCell.builder().borderWidth(1).text("Three").build())
                        .add(MyCustomCell.builder().borderWidth(1).text("Four").build())
                        .build())
                .build();
    }

}