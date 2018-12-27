package org.vandeseer.easytable.structure.cell;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vandeseer.easytable.structure.Column;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.TableNotYetBuiltException;
import org.vandeseer.easytable.util.PdfUtil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CellTextTest {

    @Mock
    private Row row;

    @Mock
    private Column column;

    @Mock
    private Table table;

    private final PDFont font = PDType1Font.HELVETICA;

    private final int fontSize = 10;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getWidth_regularCellWithNoWrappingText() {
        final String text = "abc";

        final CellText cell = prepareForTest(
                CellText.builder()
                .font(font)
                .fontSize(fontSize)
                .text(text)
                .build()
        );

        assertThat(cell.getWidthOfText(), equalTo(PdfUtil.getStringWidth(text, font, fontSize)));
    }

    @Test
    public void getWidth_cellWithWrappingText_columnSizeOk() {
        setColumnWidthTo(9f); // smaller than the size of the text "abc"

        final CellText cell = prepareForTest(
                CellText.builder()
                .font(font)
                .fontSize(fontSize)
                .text("abc abc abc abc")
                .paddingLeft(0)
                .paddingRight(0)
                .wordBreak(true)
                .build()
        );

        assertThat(cell.getWidthOfText() + cell.getHorizontalPadding(), equalTo(PdfUtil.getStringWidth("a-", font, fontSize)));
    }

    @Test
    public void getWidth_cellWithWrappingText_columnTooSmall() {
        setColumnWidthTo(5f);

        final CellText cell = prepareForTest(
                CellText.builder()
                        .font(font)
                        .fontSize(fontSize)
                        .text("a")
                        .paddingLeft(0)
                        .paddingRight(0)
                        .wordBreak(true)
                        .build()
        );

        assertThat(cell.getWidthOfText() + cell.getHorizontalPadding(), equalTo(PdfUtil.getStringWidth("a", font, fontSize)));
    }

    @Test
    public void getWidth_cellWithSpanningNoWrapping() {
        prepareTwoSpanningColumnsOfSize(20f, 20f);

        final String text = "abc";

        final CellText cell = prepareForTest(
                CellText.builder()
                .font(font)
                .fontSize(fontSize)
                .text(text)
                .span(2)
                .build()
        );

        assertThat(cell.getWidthOfText(), equalTo(PdfUtil.getStringWidth(text, font, fontSize)));
    }

    @Test
    public void getWidth_cellWithSpanningWithWrapping() {
        prepareTwoSpanningColumnsOfSize(35f, 15f);

        final String text = "abc abc abc abc abc abc abc abc abc abc";
        final CellText cell = prepareForTest(
                CellText.builder()
                .text(text)
                .font(font)
                .fontSize(fontSize)
                .span(2)
                .paddingLeft(5)
                .paddingRight(5)
                .wordBreak(true)
                .build()
        );

        // The two columns will have a width of 35 + 15 = 50; the size of the text "abc abc" has 35.02 and the padding
        // is 10 in sum. Therefore the text will be split in pieces of "abc abc".
        assertThat(cell.getWidthOfText(), equalTo(PdfUtil.getStringWidth("abc abc", font, fontSize)));
    }
    
    @Test
    public void getCell_toBuilderFeature() {
    	// Create two cells without border
    	CellText originalCell1 = CellText.builder().text("11").paddingTop(35).paddingBottom(15).build();
        CellText originalCell2 = CellText.builder().text("12").paddingTop(15).paddingBottom(25).build();
        final Row row = Row.builder()
                .add(originalCell1.toBuilder().borderWidthBottom(1F).build()) // add the border
                .add(originalCell2.toBuilder().borderWidthBottom(1F).build()) // add the border
                .build();
        for(CellBaseData cell : row.getCells())
        {
        	assertEquals(1F,cell.getBorderWidthBottom(),0F); // test if border exists
        }
    }

    @Test
    public void getHeight_shouldThrowExceptionIfTableNotYetBuilt() {
        CellBaseData cell = CellText.builder().text("abc").paddingTop(35).paddingBottom(15).build();

        exception.expect(TableNotYetBuiltException.class);
        cell.getHeight();
    }

    private void prepareTwoSpanningColumnsOfSize(float sizeColumn1, float sizeColumn2) {
        when(column.getWidth()).thenReturn(sizeColumn1);
        Column nextColumn = mock(Column.class);
        when(nextColumn.getWidth()).thenReturn(sizeColumn2);
        when(column.getNext()).thenReturn(nextColumn);
    }

    private CellText prepareForTest(CellText cell) {
        when(row.getTable()).thenReturn(table);
        cell.setRow(row);
        cell.setColumn(column);
        return cell;
    }

    private void setColumnWidthTo(float width) {
        when(column.getWidth()).thenReturn(width);
    }

}