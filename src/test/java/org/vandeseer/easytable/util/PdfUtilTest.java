package org.vandeseer.easytable.util;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;

import java.util.List;

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

        assertThat(PdfUtil.getOptimalTextBreakLines(text, PDType1Font.HELVETICA, 12, maxWidth).size(), is(1));
    }

    @Test
    public void getOptimalTextBreakLines_withNewLinesAndFitsInColumn_shouldReturnMoreThanOneLine() {
        final String text = "this is a small text\nthat has two\nnew lines in it";

        // Since we have new lines
        final float maxWidth = 2f * PdfUtil.getStringWidth(text, PDType1Font.HELVETICA, 12);

        assertThat(PdfUtil.getOptimalTextBreakLines(text, PDType1Font.HELVETICA, 12, maxWidth).size(), is(3));
    }

    @Test
    public void getOptimalTextBreakLines_noSpacesInText_shouldSplitOnDot() {
        final String text = "This.should.be.splitted.on.a.dot.No.spaces.in.here.";

        final float maxWidth = PdfUtil.getStringWidth("This.should.be.splitted.on.a.dot.", PDType1Font.HELVETICA, 12);

        final List<String> lines = PdfUtil.getOptimalTextBreakLines(text, PDType1Font.HELVETICA, 12, maxWidth);

        assertThat(lines.size(), is(2));
        assertThat(lines.get(0), is(equalTo("This.should.be.splitted.on.a.dot.")));
        assertThat(lines.get(1), is(equalTo("No.spaces.in.here.")));
    }

    @Test
    public void getOptimalTextBreakLines_noSpacesNorDotsInText_shouldSplitOnComma() {
        final String text = "This,should,be,splitted,on,a,comma,no,space,nor,dots,in,here,";

        final float maxWidth = PdfUtil.getStringWidth("This,should,be,splitted,on,a,comma,", PDType1Font.HELVETICA, 12);

        final List<String> lines = PdfUtil.getOptimalTextBreakLines(text, PDType1Font.HELVETICA, 12, maxWidth);

        assertThat(lines.size(), is(2));
        assertThat(lines.get(0), is(equalTo("This,should,be,splitted,on,a,comma,")));
        assertThat(lines.get(1), is(equalTo("no,space,nor,dots,in,here,")));
    }

    @Test
    public void getOptimalTextBreakLines_noSpacesNorDotsNorCommasInText_shouldSplitBySize() {
        final String text = "ThisDoesNotHaveAnyCharactersWhereWeCouldBreakMoreEasilySoWeBreakBySize";

        final float maxWidth = PdfUtil.getStringWidth("ThisDoesNotHaveAnyCharacters", PDType1Font.HELVETICA, 12);

        final List<String> lines = PdfUtil.getOptimalTextBreakLines(text, PDType1Font.HELVETICA, 12, maxWidth);

        assertThat(lines.size(), is(3));
        assertThat(lines.get(0), is(equalTo("ThisDoesNotHaveAnyCharacter-")));
        assertThat(lines.get(1), is(equalTo("sWhereWeCouldBreakMoreEasi-")));
        assertThat(lines.get(2), is(equalTo("lySoWeBreakBySize")));
    }

}