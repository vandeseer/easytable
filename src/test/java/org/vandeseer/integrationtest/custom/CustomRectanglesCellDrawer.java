package org.vandeseer.integrationtest.custom;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.DrawingUtil;
import org.vandeseer.easytable.drawing.PositionedRectangle;
import org.vandeseer.easytable.drawing.cell.AbstractCellDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;

public class CustomRectanglesCellDrawer {

    public static final float COLUMN_WIDTH = 50f;

    @SuperBuilder
    @Getter
    private static class RectanglesCell extends AbstractCell {

        private float color1Percentage;
        private Color color1Color;

        private float color2Percentage;
        private Color color2Color;
        
        private float color3Percentage;
        private Color color3Color;
        
        private boolean isMultiColumn;

        @Override
        public float getMinHeight() {
            return (getRow().getHeight() * 80 / 100) + getVerticalPadding();
        }

        @Override
        protected Drawer createDefaultDrawer() {

            return new RectanglesCellAbstractCellDrawer(this);
        }

        private class RectanglesCellAbstractCellDrawer extends AbstractCellDrawer<RectanglesCell> {

            public RectanglesCellAbstractCellDrawer(RectanglesCell rectanglesCell) {
                this.cell = rectanglesCell;
            }

            @SneakyThrows
            @Override
            public void drawContent(DrawingContext drawingContext) {
                final PDPageContentStream contentStream = drawingContext.getContentStream();
                final Point2D.Float start = drawingContext.getStartingPoint();

                final float rowHeight = cell.getRow().getHeight();
                float startY = rowHeight < cell.getHeight() ? start.y + rowHeight - cell.getHeight() : start.y;

                final float calculatedRectangleHeight = cell.getHeight() * 80/ 100;
                float rectHeight = calculatedRectangleHeight * cell.color1Percentage;
                startY = startY + cell.getPaddingBottom();
                System.out.println("RectHeight 1 :" + rectHeight + " startY1 :" + startY);
                float totalRectHeight = rectHeight;
                // Actual
                DrawingUtil.drawRectangle(contentStream,
                        PositionedRectangle.builder()
                                .x(start.x + cell.getPaddingLeft())
                                .y(startY)
                                .width(cell.getWidth() - cell.getHorizontalPadding())
                                .height(rectHeight)
                                .color(cell.getColor1Color()).build()
                );

                // Unused
                if(cell.color2Percentage > 0f) {
                	startY = startY + rectHeight ; //cell.getPaddingBottom() + RectanglesCell.RECTANGLE_HEIGHT * cell.color1Percentage;
                	rectHeight = calculatedRectangleHeight * cell.color2Percentage;
                	
                	System.out.println("RectHeight 2 :" + rectHeight + " startY2 :" + startY);
                	totalRectHeight += rectHeight;
                	DrawingUtil.drawRectangle(contentStream,
                            PositionedRectangle.builder()
                                    .x(start.x + cell.getPaddingLeft())
                                    .y(startY)
                                    .width(cell.getWidth() - cell.getHorizontalPadding())
                                    .height(rectHeight)
                                    .color(cell.getColor2Color()).build()
                    );
                }
                if(cell.color3Percentage > 0f) {
                	startY = startY + rectHeight;//cell.getPaddingBottom() + RectanglesCell.RECTANGLE_HEIGHT * cell.color2Percentage;
                	rectHeight = calculatedRectangleHeight * cell.color3Percentage;
                	
                	System.out.println("RectHeight 3 :" + rectHeight + " startY3 :" + startY);
                	totalRectHeight += rectHeight;
                	 DrawingUtil.drawRectangle(contentStream,
                             PositionedRectangle.builder()
                                     .x(start.x + cell.getPaddingLeft())
                                     .y(startY)
                                     .width(cell.getWidth() - cell.getHorizontalPadding())
                                     .height(rectHeight)
                                     .color(cell.getColor3Color()).build()
                     );
                }
                System.out.println("Total Rect Height :" + totalRectHeight);
               
            }

            @Override
            protected float calculateInnerHeight() {
                return getRow().getHeight() * 80 / 100;
            }

        }
    }

    public static void main(String[] args) throws IOException {

        try(final PDDocument document = new PDDocument()) {

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

            document.save("target/customCellDrawerWithRectangles-test.pdf");
        }
    }

    private static Table createSimpleTable() {
        return Table.builder()
                .addColumnsOfWidth(COLUMN_WIDTH, COLUMN_WIDTH, COLUMN_WIDTH, COLUMN_WIDTH, COLUMN_WIDTH, COLUMN_WIDTH, COLUMN_WIDTH)
                .fontSize(8)
                .font(HELVETICA)
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("MON").build())
                        .add(TextCell.builder().borderWidth(1).text("TUE").build())
                        .add(TextCell.builder().borderWidth(1).text("WED").build())
                        .add(TextCell.builder().borderWidth(1).text("THU").build())
                        .add(TextCell.builder().borderWidth(1).text("FRI").build())
                        .add(TextCell.builder().borderWidth(1).text("SAT").build())
                        .add(TextCell.builder().borderWidth(1).text("SUN").build())
                        .build())
                .addRow(Row.builder().height(40f)
                        .add(RectanglesCell.builder().borderWidth(1).isMultiColumn(true).color1Color(Color.GRAY).color1Percentage(0.2f).color2Color(Color.BLUE).color2Percentage(0.3f).color3Color(Color.RED).color3Percentage(0.5f).build())
                        .add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.33f).color2Color(Color.BLUE).color2Percentage(0.33f).color3Color(Color.RED).color3Percentage(0.34f).build())
                        .add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.20f).color2Color(Color.BLUE).color2Percentage(0.35f).color3Color(Color.RED).color3Percentage(0.45f).build())
                        .add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.45f).color2Color(Color.BLUE).color2Percentage(0.55f).build())
                        .add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.45f).color2Color(Color.BLUE).color2Percentage(0.55f).build())
                        .add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.45f).color2Color(Color.BLUE).color2Percentage(0.55f).build())
                        .add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.2f).color2Color(Color.BLUE).color2Percentage(0.8f).build())
                        .build())
                .addRow(Row.builder().height(60f)
                        .add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.4f).color2Color(Color.BLUE).color2Percentage(0.6f).build())
                        .add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.5f).color2Color(Color.BLUE).color2Percentage(0.5f).build())
                        .add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.4f).color2Color(Color.BLUE).color2Percentage(0.6f).build())
                        .add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.45f).color2Color(Color.BLUE).color2Percentage(0.55f).build())
                        .add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.45f).color2Color(Color.BLUE).color2Percentage(0.55f).build())
                        .add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.45f).color2Color(Color.BLUE).color2Percentage(0.55f).build())
                        .add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(1.0f).build())
                        .build())
                .build();
    }

}