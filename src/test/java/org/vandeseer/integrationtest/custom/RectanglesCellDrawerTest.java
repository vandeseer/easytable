package org.vandeseer.integrationtest.custom;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.RectanglesCell;
import org.vandeseer.easytable.structure.cell.TextCell;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;

public class RectanglesCellDrawerTest {
	public static final float COLUMN_WIDTH = 50f;
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

            document.save("target/customCellDrawerWithRectangles.pdf");
        }
	}
	
	private static Table createSimpleTable() {
        return Table.builder()
                .addColumnsOfWidth(COLUMN_WIDTH, COLUMN_WIDTH, COLUMN_WIDTH, COLUMN_WIDTH, COLUMN_WIDTH, COLUMN_WIDTH, COLUMN_WIDTH)
                .fontSize(8)
                .font(HELVETICA).borderWidth(1)
                .addRow(Row.builder().height(40f)
                        .add(TextCell.builder().borderWidth(1).text("MON").build())
                        .add(TextCell.builder().borderWidth(1).text("TUE").build())
                        .add(TextCell.builder().borderWidth(1).text("WED").build())
                        .add(TextCell.builder().borderWidth(1).text("THU").build())
                        .add(TextCell.builder().borderWidth(1).text("FRI").build())
                        .add(TextCell.builder().borderWidth(1).text("SAT").build())
                        .add(TextCell.builder().borderWidth(1).text("SUN").build())
                        .build())
                .addRow(Row.builder().height(40f)
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.7f).build())
                        .build())
                .addRow(Row.builder().height(40f)
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.4f).color2Color(Color.BLUE).color2Percentage(0.4f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.5f).color2Color(Color.BLUE).color2Percentage(0.3f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.6f).color2Color(Color.BLUE).color2Percentage(0.2f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.7f).color2Color(Color.BLUE).color2Percentage(0.1f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.7f).color2Color(Color.BLUE).color2Percentage(0.1f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.8f).color2Color(Color.BLUE).color2Percentage(0.1f).color3Color(Color.RED).color3Percentage(0.1f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.2f).color2Color(Color.BLUE).color2Percentage(0.8f).build())
                        .build())
                .addRow(Row.builder().height(40f)
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.7f).build())
                        .build())
                .addRow(Row.builder().height(40f)
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.30f).color2Color(Color.BLUE).color2Percentage(0.30f).color3Color(Color.RED).color3Percentage(0.40f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.40f).color2Color(Color.BLUE).color2Percentage(0.15f).color3Color(Color.RED).color3Percentage(0.45f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.61f).color2Color(Color.BLUE).color2Percentage(0.25f).color3Color(Color.RED).color3Percentage(0.14f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.7f).color2Color(Color.BLUE).color2Percentage(0.1f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.7f).color2Color(Color.BLUE).color2Percentage(0.1f).color3Color(Color.RED).color3Percentage(0.2f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.5f).color2Color(Color.BLUE).color2Percentage(0.35f).color3Color(Color.RED).color3Percentage(0.15f).build())
                		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.2f).color2Color(Color.BLUE).color2Percentage(0.8f).build())
                        .build())
                .build();
        
        /*.addRow(Row.builder().height(40f)
        		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
        		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
        		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
        		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
        		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
        		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.5f).color3Color(Color.RED).color3Percentage(0.2f).build())
        		.add(RectanglesCell.builder().borderWidth(1).color1Color(Color.GRAY).color1Percentage(0.3f).color2Color(Color.BLUE).color2Percentage(0.7f).build())
                .build())*/
    }

}
