package org.vandeseer.custom;

import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.drawing.CellTextDrawer;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellText;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;

public class CustomCell {

    @SuperBuilder
    private static class MyCustomCell extends CellText {

//        @Override
//        public float getTextHeight() {
//            assertIsRendered();
//
//            final float notBrokenTextWidth = PdfUtil.getStringWidth(text, getFont(), getFontSize());

//            if (settings.isWordBreak()) {
//
//                float columnsWidth = getColumn().getWidth();
//
//                // We have to take column spanning into account
//                if (getColSpan() > 1) {
//                    Column currentColumn = getColumn();
//                    for (int i = 1; i < getColSpan(); i++) {
//                        columnsWidth += currentColumn.getNext().getWidth();
//                        currentColumn = currentColumn.getNext();
//                    }
//                }
//
//                final float maxWidth = columnsWidth - getHorizontalPadding();
//                List<String> textLines = PdfUtil.getOptimalTextBreakLines(text, getFont(), getFontSize(), maxWidth);
//
//                return textLines.stream()
//                        .map(line -> PdfUtil.getStringWidth(line, getFont(), getFontSize()))
//                        .max(Comparator.naturalOrder())
//                        .orElse(notBrokenTextWidth);
//
//            } else {
//                return notBrokenTextWidth;
//            }
//
//            return notBrokenTextWidth;
//        }
//
//        // TODO
//        @Override
//        public float getWidthOfText() {
//            // Basically the number of lines ...
//            // But there are more differences! Because the number of lines depends on the width and not on the
//            // height of the cell
//            return getFontSize(); // hack ;)
//        }

        @Override
        public Drawer getDrawer() {
            return new CellTextDrawer(this) {
                @Override
                protected void drawText(String text, PDFont font, int fontSize, Color color, float x, float y, PDPageContentStream contentStream)
                        throws IOException {

                    // Rotate by 90 degrees counter clockwise
                    final AffineTransform transform = AffineTransform.getTranslateInstance(x, y);
                    transform.concatenate(AffineTransform.getRotateInstance(Math.PI * 0.5));
                    transform.concatenate(AffineTransform.getTranslateInstance(-x, -y - fontSize));

                    contentStream.moveTo(x, y);
                    contentStream.beginText();

                    // Do the transformation :)
                    contentStream.setTextMatrix(transform);

                    contentStream.setNonStrokingColor(color);
                    contentStream.setFont(font, fontSize);
                    contentStream.newLineAtOffset(x, y);
                    contentStream.showText(text);
                    contentStream.endText();
                    contentStream.setCharacterSpacing(0);
                }
            };
        }

    }

    public static void main(String[] args) throws IOException {
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

        document.save("target/fubarrrrrr.pdf");
        document.close();
    }

    private static Table createSimpleTable() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(100, 100, 100, 100)
                .fontSize(8)
                .font(HELVETICA);

        tableBuilder.addRow(Row.builder()
                .add(MyCustomCell.builder().borderWidth(1).text("One 1\nFubarbar").build())
                .add(MyCustomCell.builder().borderWidth(1).text("Two").build())
                .add(MyCustomCell.builder().borderWidth(1).text("Three").build())
                .add(MyCustomCell.builder().borderWidth(1).text("Four").build())
                .build());

        return tableBuilder.build();
    }

}