package org.vandeseer.easytable.structure.cell;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vandeseer.easytable.structure.Column;
import org.vandeseer.easytable.util.PdfUtil;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
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

        assertThat(cell.getWidth(), equalTo(PdfUtil.getStringWidth(text, font, fontSize) + cell.getHorizontalPadding()));
    }

    // TODO We have a StackOverflowError in case the column width is less then the text width of "a-" !
    // TODO We will get a RuntimeException in case the column width minus the horizontal padding is smaller than the text width of "a-"!
    @Test
    public void getWidth_cellWithWrappingText() {
        enableWordBreaking();
        setColumnWidthTo(9f); // smaller than the size of the text "abc"

        final CellText cell = prepareForTest(
                CellText.builder()
                .font(font)
                .fontSize(fontSize)
                .text("abc abc abc abc")
                .paddingLeft(0)
                .paddingRight(0)
                .build()
        );

        assertThat(cell.getWidth(), equalTo(PdfUtil.getStringWidth("a-", font, fontSize)));
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

        assertThat(cell.getWidth(), equalTo(PdfUtil.getStringWidth(text, font, fontSize) + cell.getHorizontalPadding()));
    }

    @Test
    public void getWidth_cellWithSpanningWithWrapping() {
        enableWordBreaking();
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
                .build()
        );

        // The two columns will have a width of 35 + 15 = 50; the size of the text "abc abc" has 35.02 and the padding
        // is 10 in sum. Therefore the text will be split in pieces of "abc abc".
        assertThat(cell.getWidth(), equalTo(PdfUtil.getStringWidth("abc abc", font, fontSize) + cell.getHorizontalPadding()));
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

    private void enableWordBreaking() {
        when(table.isWordBreak()).thenReturn(true);
    }

    private void setColumnWidthTo(float width) {
        when(column.getWidth()).thenReturn(width);
    }

}