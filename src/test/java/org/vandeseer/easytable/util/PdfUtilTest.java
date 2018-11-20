package org.vandeseer.easytable.util;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PdfUtilTest {

    @Test
    public void getStringWidth_noNewLines() {
        final String text = "this is a small text";

        assertThat(PdfUtil.getStringWidth(text, PDType1Font.HELVETICA, 12), equalTo(94.692F));
    }

    @Test
    public void getStringWidth_withNewLines_shouldReturnWidthOfLongestLine() {
        final String text = "this is a longer text\nthat has two\nnew lines in it";
        final float firstLineWidth = PdfUtil.getStringWidth("this is a longer text", PDType1Font.HELVETICA, 12);

        assertThat(PdfUtil.getStringWidth(text, PDType1Font.HELVETICA, 12), equalTo(firstLineWidth));
    }

    @Test
    public void getOptimalTextBreakLines_noNewLinesAndFitsInColumn_shouldReturnOneLine() {
        final String text = "this is a small text";

        // We don't have to break in case we have two times the size ;)
        final float maxWidth = 2f * PdfUtil.getStringWidth(text, PDType1Font.HELVETICA, 12);

        assertThat(PdfUtil.getOptimalTextBreakLines(text, PDType1Font.HELVETICA, 12, 100).size(), is(1));
    }

    @Test
    public void getOptimalTextBreakLines_withNewLinesAndFitsInColumn_shouldReturnMoreThanOneLine() {
        final String text = "this is a small text\nthat has two\nnew lines in it";

        // Since we have new lines
        final float maxWidth = 2f * PdfUtil.getStringWidth(text, PDType1Font.HELVETICA, 12);

        assertThat(PdfUtil.getOptimalTextBreakLines(text, PDType1Font.HELVETICA, 12, 100).size(), is(3));
    }

}