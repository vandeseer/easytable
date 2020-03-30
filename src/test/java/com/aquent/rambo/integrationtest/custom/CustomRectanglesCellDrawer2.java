package com.aquent.rambo.integrationtest.custom;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.aquent.rambo.easytable.TableDrawer;
import com.aquent.rambo.easytable.drawing.Drawer;
import com.aquent.rambo.easytable.drawing.DrawingContext;
import com.aquent.rambo.easytable.drawing.DrawingUtil;
import com.aquent.rambo.easytable.drawing.PositionedRectangle;
import com.aquent.rambo.easytable.drawing.PositionedStyledText;
import com.aquent.rambo.easytable.drawing.cell.AbstractCellDrawer;
import com.aquent.rambo.easytable.structure.Row;
import com.aquent.rambo.easytable.structure.Table;
import com.aquent.rambo.easytable.structure.cell.AbstractCell;
import com.aquent.rambo.easytable.structure.cell.TextCell;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;

public class CustomRectanglesCellDrawer2 {

    public static final float COLUMN_WIDTH = 50f;

    @SuperBuilder
    @Getter
    private static class RectanglesCell extends AbstractCell {

        private List<RectangleCellDetails> rectangleCellDetails;
        
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
                float startX = start.x + cell.getPaddingLeft();
                float rectCellWidth = cell.getWidth() - cell.getHorizontalPadding();
                if(cell.isMultiColumn) {
                	rectCellWidth = rectCellWidth/2;
                }
                for(RectangleCellDetails rectangleCellDetails:cell.getRectangleCellDetails()) {
                	
                	float rectHeight = calculatedRectangleHeight * rectangleCellDetails.getColor1Percentage();
                	startY = startY + cell.getPaddingBottom();
                    System.out.println("RectHeight 1 :" + rectHeight + " startY1 :" + startY + " Color : " + rectangleCellDetails.getColor1Color());
                    float totalRectHeight = rectHeight;
                    if(cell.isMultiColumn) {
                    	DrawingUtil.drawText(contentStream, PositionedStyledText.builder().x(startX+5).y(startY).font(PDType1Font.HELVETICA_BOLD).fontSize(6).color(Color.BLACK).text("10.0").build());
                    }else {
                    	DrawingUtil.drawText(contentStream, PositionedStyledText.builder().x(startX+10).y(startY).font(PDType1Font.HELVETICA_BOLD).fontSize(6).color(Color.BLACK).text("10.0").build());
                    }
                    startY = startY + 5f;
                    // Actual
                    DrawingUtil.drawRectangle(contentStream,
                            PositionedRectangle.builder()
                                    .x(startX)
                                    .y(startY)
                                    .width(rectCellWidth)
                                    .height(rectHeight)
                                    .color(rectangleCellDetails.getColor1Color()).build()
                    );
                    
                    if(rectangleCellDetails.getColor2Percentage() > 0f) {
                    	startY = startY + rectHeight ; //cell.getPaddingBottom() + RectanglesCell.RECTANGLE_HEIGHT * cell.color1Percentage;
                    	rectHeight = calculatedRectangleHeight * rectangleCellDetails.getColor2Percentage();
                    	
                    	System.out.println("RectHeight 2 :" + rectHeight + " startY2 :" + startY);
                    	totalRectHeight += rectHeight;
                    	DrawingUtil.drawRectangle(contentStream,
                                PositionedRectangle.builder()
                                        .x(startX)
                                        .y(startY)
                                        .width(rectCellWidth)
                                        .height(rectHeight)
                                        .color(rectangleCellDetails.getColor2Color()).build()
                        );
                    }
                    
                    if(rectangleCellDetails.getColor3Percentage() > 0f) {
                    	startY = startY + rectHeight;//cell.getPaddingBottom() + RectanglesCell.RECTANGLE_HEIGHT * cell.color2Percentage;
                    	rectHeight = calculatedRectangleHeight * rectangleCellDetails.getColor3Percentage();
                    	
                    	System.out.println("RectHeight 3 :" + rectHeight + " startY3 :" + startY);
                    	totalRectHeight += rectHeight;
                    	 DrawingUtil.drawRectangle(contentStream,
                                 PositionedRectangle.builder()
                                         .x(startX)
                                         .y(startY)
                                         .width(rectCellWidth)
                                         .height(rectHeight)
                                         .color(rectangleCellDetails.getColor3Color()).build()
                         );
                    }
                   
                    System.out.println("Total Rect Height :" + totalRectHeight);
                    System.out.println("START X " + startX);
                    startX = startX + rectCellWidth + 1;
                    startY = rowHeight < cell.getHeight() ? start.y + rowHeight - cell.getHeight() : start.y;
                    
                }
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
    	
    	List<RectangleCellDetails> rectangleCellDetailsRow1Col1 = new ArrayList<>();
    	RectangleCellDetails rectDetails1 = RectangleCellDetails.builder().color1Color(Color.GRAY).color1Percentage(0.2f).color2Color(Color.BLUE).color2Percentage(0.3f).color3Color(Color.RED).color3Percentage(0.5f).build();
    	RectangleCellDetails rectDetails2 = RectangleCellDetails.builder().color1Color(Color.GRAY).color1Percentage(0.8f).color2Color(Color.BLUE).color2Percentage(0.2f).build();
    	rectangleCellDetailsRow1Col1.add(rectDetails1);
    	rectangleCellDetailsRow1Col1.add(rectDetails2);
    	
    	
    	List<RectangleCellDetails> rectangleCellDetailsRow1Col2 = new ArrayList<>();
    	RectangleCellDetails rectDetailsRow1Col2 = RectangleCellDetails.builder().color1Color(Color.GRAY).color1Percentage(0.33f).color2Color(Color.BLUE).color2Percentage(0.33f).color3Color(Color.RED).color3Percentage(0.34f).build();
    	rectangleCellDetailsRow1Col2.add(rectDetailsRow1Col2);
    	
    	List<RectangleCellDetails> rectangleCellDetailsRow1Col3 = new ArrayList<>();
    	RectangleCellDetails rectDetailsRow1Col3 = RectangleCellDetails.builder().color1Color(Color.GRAY).color1Percentage(0.20f).color2Color(Color.BLUE).color2Percentage(0.35f).color3Color(Color.RED).color3Percentage(0.45f).build();
    	rectangleCellDetailsRow1Col3.add(rectDetailsRow1Col3);
    	
    	List<RectangleCellDetails> rectangleCellDetailsRow1Col4 = new ArrayList<>();
    	RectangleCellDetails rectDetailsRow1Col4 = RectangleCellDetails.builder().color1Color(Color.GRAY).color1Percentage(0.45f).color2Color(Color.BLUE).color2Percentage(0.55f).build();
    	rectangleCellDetailsRow1Col4.add(rectDetailsRow1Col4);
    	
    	List<RectangleCellDetails> rectangleCellDetailsRow1Col5 = new ArrayList<>();
    	RectangleCellDetails rectDetailsRow1Col5 = RectangleCellDetails.builder().color1Color(Color.GRAY).color1Percentage(0.45f).color2Color(Color.BLUE).color2Percentage(0.55f).build();
    	rectangleCellDetailsRow1Col5.add(rectDetailsRow1Col5);
    	
    	List<RectangleCellDetails> rectangleCellDetailsRow1Col6 = new ArrayList<>();
    	RectangleCellDetails rectDetailsRow1Col6 = RectangleCellDetails.builder().color1Color(Color.GRAY).color1Percentage(0.45f).color2Color(Color.BLUE).color2Percentage(0.55f).build();
    	rectangleCellDetailsRow1Col6.add(rectDetailsRow1Col6);
    	
    	List<RectangleCellDetails> rectangleCellDetailsRow1Col7 = new ArrayList<>();
    	RectangleCellDetails rectDetailsRow1Col7 = RectangleCellDetails.builder().color1Color(Color.GRAY).color1Percentage(0.2f).color2Color(Color.BLUE).color2Percentage(0.8f).build();
    	rectangleCellDetailsRow1Col7.add(rectDetailsRow1Col7);
    	
    	List<RectangleCellDetails> rectangleCellDetailsRow2Col1 = new ArrayList<>();
    	RectangleCellDetails rectDetailsRow2Col1 = RectangleCellDetails.builder().color1Color(Color.GRAY).color1Percentage(0.4f).color2Color(Color.BLUE).color2Percentage(0.6f).build();
    	rectangleCellDetailsRow2Col1.add(rectDetailsRow2Col1);
    
    	List<RectangleCellDetails> rectangleCellDetailsRow2Col2 = new ArrayList<>();
    	RectangleCellDetails rectDetailsRow2Col2 = RectangleCellDetails.builder().color1Color(Color.GRAY).color1Percentage(0.5f).color2Color(Color.BLUE).color2Percentage(0.5f).build();
    	rectangleCellDetailsRow2Col2.add(rectDetailsRow2Col2);
    	
    	List<RectangleCellDetails> rectangleCellDetailsRow2Col3 = new ArrayList<>();
    	RectangleCellDetails rectDetailsRow2Col3 = RectangleCellDetails.builder().color1Color(Color.GRAY).color1Percentage(0.4f).color2Color(Color.BLUE).color2Percentage(0.6f).build();
    	rectangleCellDetailsRow2Col3.add(rectDetailsRow2Col3);
    	
    	
    	List<RectangleCellDetails> rectangleCellDetailsRow2Col4 = new ArrayList<>();
    	RectangleCellDetails rectDetailsRow2Col4 = RectangleCellDetails.builder().color1Color(Color.GRAY).color1Percentage(0.45f).color2Color(Color.BLUE).color2Percentage(0.55f).build();
    	rectangleCellDetailsRow2Col4.add(rectDetailsRow2Col4);
    	
    	List<RectangleCellDetails> rectangleCellDetailsRow2Col5 = new ArrayList<>();
    	RectangleCellDetails rectDetailsRow2Col5 = RectangleCellDetails.builder().color1Color(Color.GRAY).color1Percentage(0.45f).color2Color(Color.BLUE).color2Percentage(0.55f).build();
    	rectangleCellDetailsRow2Col5.add(rectDetailsRow2Col5);
    	
    	List<RectangleCellDetails> rectangleCellDetailsRow2Col6 = new ArrayList<>();
    	RectangleCellDetails rectDetailsRow2Col6 = RectangleCellDetails.builder().color1Color(Color.GRAY).color1Percentage(0.45f).color2Color(Color.BLUE).color2Percentage(0.55f).build();
    	rectangleCellDetailsRow2Col6.add(rectDetailsRow2Col6);
    	
    	List<RectangleCellDetails> rectangleCellDetailsRow2Col7 = new ArrayList<>();
    	RectangleCellDetails rectDetailsRow2Col7 = RectangleCellDetails.builder().color1Color(Color.GRAY).color1Percentage(1.0f).build();
    	rectangleCellDetailsRow2Col7.add(rectDetailsRow2Col7);
    	
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
                        .add(RectanglesCell.builder().borderWidth(1).isMultiColumn(true).rectangleCellDetails(rectangleCellDetailsRow1Col1).build())
                        .add(RectanglesCell.builder().borderWidth(1).rectangleCellDetails(rectangleCellDetailsRow1Col2).build())
                        .add(RectanglesCell.builder().borderWidth(1).rectangleCellDetails(rectangleCellDetailsRow1Col3).build())
                        .add(RectanglesCell.builder().borderWidth(1).rectangleCellDetails(rectangleCellDetailsRow1Col4).build())
                        .add(RectanglesCell.builder().borderWidth(1).rectangleCellDetails(rectangleCellDetailsRow1Col5).build())
                        .add(RectanglesCell.builder().borderWidth(1).rectangleCellDetails(rectangleCellDetailsRow1Col6).build())
                        .add(RectanglesCell.builder().borderWidth(1).rectangleCellDetails(rectangleCellDetailsRow1Col7).build())
                        .build())
                .addRow(Row.builder().height(60f)
                        .add(RectanglesCell.builder().borderWidth(1).rectangleCellDetails(rectangleCellDetailsRow2Col1).build())
                        .add(RectanglesCell.builder().borderWidth(1).rectangleCellDetails(rectangleCellDetailsRow2Col2).build())
                        .add(RectanglesCell.builder().borderWidth(1).rectangleCellDetails(rectangleCellDetailsRow2Col3).build())
                        .add(RectanglesCell.builder().borderWidth(1).rectangleCellDetails(rectangleCellDetailsRow2Col4).build())
                        .add(RectanglesCell.builder().borderWidth(1).rectangleCellDetails(rectangleCellDetailsRow2Col5).build())
                        .add(RectanglesCell.builder().borderWidth(1).rectangleCellDetails(rectangleCellDetailsRow2Col6).build())
                        .add(RectanglesCell.builder().borderWidth(1).rectangleCellDetails(rectangleCellDetailsRow2Col7).build())
                        .build())
                .build();
    	
    	/*return Table.builder()
                .addColumnsOfWidth(COLUMN_WIDTH)
                .fontSize(8)
                .font(HELVETICA)
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("MON").build())
                        .build())
                .addRow(Row.builder().height(60f)
                       .add(RectanglesCell.builder().borderWidth(1).rectangleCellDetails(rectangleCellDetailsRow1Col7).build())
                        .build())
               .build();*/
    }

}