package org.vandeseer.integrationtest;

import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.Before;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.ImageCell;
import org.vandeseer.easytable.structure.cell.TextCell;
import org.vandeseer.easytable.structure.cell.VerticalTextCell;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static junit.framework.TestCase.assertTrue;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
import static org.vandeseer.TestUtils.getActualPdfFor;
import static org.vandeseer.TestUtils.getExpectedPdfFor;
import static org.vandeseer.easytable.settings.HorizontalAlignment.*;
import static org.vandeseer.easytable.settings.VerticalAlignment.*;

public class VerticalTextCellTest {

    private static final Color LIGHT_GREEN = new Color(221, 255, 217);
    private static final String FILE_NAME = "cellVerticalText.pdf";

    private PDFont ownFont;
    private PDImageXObject checkImage;

    @Before
    public void before() throws IOException {
        PDDocument document = new PDDocument();

        // Load a custom font
        final InputStream resourceAsStream = this.getClass()
                                                .getClassLoader()
                                                .getResourceAsStream("OpenSansCondensed-Light.ttf");

        ownFont = PDType0Font.load(document, resourceAsStream);

        // Load custom image
        final byte[] sampleBytes = IOUtils.toByteArray(Objects.requireNonNull(this.getClass().getClassLoader()
                                                .getResourceAsStream("check.png")));
        checkImage = PDImageXObject.createFromByteArray(document, sampleBytes, "check");
    }

    @Test
    public void testVerticalTextCell() throws IOException {
        TestUtils.createAndSaveDocumentWithTables(FILE_NAME,
                createSimpleTable(),
                createKnowledgeBaseExampleTable(),
                createAlignmentTable()
        );


        CompareResult compareResult = new PdfComparator<>(getExpectedPdfFor(FILE_NAME), getActualPdfFor(FILE_NAME)).compare();
        assertTrue(compareResult.isEqual());
    }

    private static Table createSimpleTable() {
        final Table.TableBuilder tableBuilder = Table.builder()
                .backwardsCompatibleFontHeight(true)
                .addColumnsOfWidth(100, 100, 100, 100)
                .fontSize(8)
                .font(HELVETICA);

        tableBuilder
                .addRow(Row.builder()
                        .add(VerticalTextCell.builder().minHeight(80f).borderWidth(1).text("This is a super long text that does not fit in one line").build())
                        .add(VerticalTextCell.builder().borderWidth(1).text("Two").build())
                        .add(VerticalTextCell.builder().rowSpan(2).borderWidth(1).text("This is again a very long text that will break at one point :)").build())
                        .add(VerticalTextCell.builder().borderWidth(1).text("Four").build())
                        .build())
                .addRow(Row.builder()
                        .add(TextCell.builder().borderWidth(1).text("One 1\nFubarbar").build())
                        .add(TextCell.builder().borderWidth(1).text("Abc").build())
                        .add(VerticalTextCell.builder().borderWidth(1).text("Four").build())
                        .build());

        return tableBuilder.build();
    }


    private Table createKnowledgeBaseExampleTable() {

        final Table.TableBuilder tableBuilder = Table.builder()
                .backwardsCompatibleFontHeight(true)
                .addColumnsOfWidth(200, 20, 20, 20, 20, 20)
                .fontSize(8)
                .font(ownFont)
                .addRow(Row.builder()
                        .add(createEmptySpacingCell())
                        .add(createPersonCell("Ralph"))
                        .add(createPersonCell("Homer"))
                        .add(createPersonCell("Bart"))
                        .add(createPersonCell("Moe"))
                        .add(createPersonCell("Krusty"))
                        .build())
                .addRow(Row.builder()
                        .add(createTechCell("Machine Learning"))
                        .add(createCheckCell())
                        .add(createCheckCell())
                        .add(createCheckCell())
                        .add(createEmptyCellWithBorders())
                        .add(createCheckCell())
                        .backgroundColor(LIGHT_GREEN)
                        .build())
                .addRow(Row.builder()
                        .add(createTechCell("Software Engineering"))
                        .add(createCheckCell())
                        .add(createEmptyCellWithBorders())
                        .add(createCheckCell())
                        .add(createCheckCell())
                        .add(createEmptyCellWithBorders())
                        .build())
                .addRow(Row.builder()
                        .add(createTechCell("Databases"))
                        .add(createCheckCell())
                        .add(createCheckCell())
                        .add(createCheckCell())
                        .add(createEmptyCellWithBorders())
                        .add(createCheckCell())
                        .backgroundColor(LIGHT_GREEN)
                        .build())
                .addRow(Row.builder()
                        .add(createTechCell("Network Technology"))
                        .add(createCheckCell())
                        .add(createEmptyCellWithBorders())
                        .add(createCheckCell())
                        .add(createEmptyCellWithBorders())
                        .add(createCheckCell())
                        .build())
                .addRow(Row.builder()
                        .add(createTechCell("Security"))
                        .add(createCheckCell())
                        .add(createCheckCell())
                        .add(createEmptyCellWithBorders())
                        .add(createEmptyCellWithBorders())
                        .add(createEmptyCellWithBorders())
                        .backgroundColor(LIGHT_GREEN)
                        .build());

        return tableBuilder.build();
    }

    private Table createAlignmentTable() {
        return Table.builder()
                .backwardsCompatibleFontHeight(true)
                .addColumnsOfWidth(80, 80, 80)
                .fontSize(8)
                .font(ownFont)
                .addRow(Row.builder()
                        .height(100f)
                        .add(VerticalTextCell.builder().borderWidth(1).text("Bottom").verticalAlignment(BOTTOM).build())
                        .add(VerticalTextCell.builder().borderWidth(1).text("Middle").verticalAlignment(MIDDLE).build())
                        .add(VerticalTextCell.builder().borderWidth(1).text("Top").verticalAlignment(TOP).build())
                        .build())
                .addRow(Row.builder()
                        .height(100f)
                        .add(VerticalTextCell.builder().borderWidth(1).text("Left").horizontalAlignment(LEFT).build())
                        .add(VerticalTextCell.builder().borderWidth(1).text("Center").horizontalAlignment(CENTER).build())
                        .add(VerticalTextCell.builder().borderWidth(1).text("Right").horizontalAlignment(RIGHT).build())
                        .build())
                .addRow(Row.builder()
                        .height(100f)
                        .add(VerticalTextCell.builder().borderWidth(1).text("Bottom Left")
                                .verticalAlignment(BOTTOM).horizontalAlignment(LEFT).build())
                        .add(VerticalTextCell.builder().borderWidth(1).text("Middle Center")
                                .verticalAlignment(MIDDLE).horizontalAlignment(CENTER).build())
                        .add(VerticalTextCell.builder().borderWidth(1).text("Top Right")
                                .verticalAlignment(TOP).horizontalAlignment(RIGHT).build())
                        .build())
                .build();
    }

    private AbstractCell createEmptySpacingCell() {
        return TextCell.builder().minHeight(50f).text("").build();
    }

    private AbstractCell createPersonCell(String ralph) {
        return VerticalTextCell.builder().borderWidth(1).text(ralph).build();
    }

    private AbstractCell createTechCell(String tech) {
        return TextCell.builder().borderWidth(1).text(tech).verticalAlignment(MIDDLE).build();
    }

    private AbstractCell createCheckCell() {
        return ImageCell.builder().borderWidth(1).image(checkImage).build();
    }

    private AbstractCell createEmptyCellWithBorders() {
        return TextCell.builder().text("").borderWidth(1).build();
    }

}
