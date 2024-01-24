package org.vandeseer.easytable.util;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.easytable.structure.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName.HELVETICA;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.vandeseer.easytable.util.FloatUtil.isEqualInEpsilon;

public class PdfUtilTest {

    @Test
    public void getStringWidth_noNewLines() {
        final String text = "this is a small text";

        final float actualSize = PdfUtil.getStringWidth(text, new PDType1Font(HELVETICA), 12);
        final float expectedSize = 94.692F;

        assertThat(isEqualInEpsilon(expectedSize, actualSize), is(true));
    }



    @Test
    public void getStringWidth_withNewLines_shouldReturnWidthOfLongestLine() {
        final String text = "this is a longer text\nthat has two\nnew lines in it";
        final float firstLineWidth = PdfUtil.getStringWidth("this is a longer text", new PDType1Font(HELVETICA), 12);

        assertThat(PdfUtil.getStringWidth(text, new PDType1Font(HELVETICA), 12), equalTo(firstLineWidth));
    }

    @Test
    public void getOptimalTextBreakLines_noNewLinesAndFitsInColumn_shouldReturnOneLine() {
        final String text = "this is a small text";

        // We don't have to break in case we have two times the size ;)
        final float maxWidth = 2f * PdfUtil.getStringWidth(text, new PDType1Font(HELVETICA), 12);

        assertThat(PdfUtil.getOptimalTextBreakLines(List.of(new Text(text)), new PDType1Font(HELVETICA), 12, maxWidth).size(), is(1));
    }

    @Test
    public void getOptimalTextBreakLines_coloredText_noNewLinesAndFitsInColumn_shouldReturnOneLine() {
        final List<Text> text = List.of(new Text("this is ", Color.RED), new Text("a small text", Color.BLUE));

        // We don't have to break in case we have two times the size ;)
        final float maxWidth = 2f * PdfUtil.getStringWidth(text.stream().map(Text::getText).collect(Collectors.joining()), new PDType1Font(HELVETICA), 12);

        List<List<Text>> lineTextBreaks = PdfUtil.getOptimalTextBreakLines(text, new PDType1Font(HELVETICA), 12, maxWidth);
        assertThat(lineTextBreaks.size(), is(1));
        assertThat(lineTextBreaks.get(0).size(), is(2));
    }

    @Test
    public void getOptimalTextBreakLines_withNewLinesAndFitsInColumn_shouldReturnMoreThanOneLine() {
        final String text = "this is a small text\nthat has two\nnew lines in it";

        // Since we have new lines
        final float maxWidth = 2f * PdfUtil.getStringWidth(text, new PDType1Font(HELVETICA), 12);

        assertThat(PdfUtil.getOptimalTextBreakLines(List.of(new Text(text)), new PDType1Font(HELVETICA), 12, maxWidth).size(), is(3));
    }

    @Test
    public void getOptimalTextBreakLines_coloredText_withNewLinesAndFitsInColumn_shouldReturnMoreThanOneLine() {
        final List<Text> text = List.of(
          new Text("this is a small text\n", Color.RED),
          new Text("that has two\n", Color.BLUE),
          new Text("new lines in it", Color.GREEN)
          );

        // Since we have new lines
        final float maxWidth = 2f * PdfUtil.getStringWidth(text.stream().map(Text::getText).collect(Collectors.joining()), new PDType1Font(HELVETICA), 12);

        List<List<Text>> lineTextBreaks = PdfUtil.getOptimalTextBreakLines(text, new PDType1Font(HELVETICA), 12, maxWidth);
        assertThat(lineTextBreaks.size(), is(3));
        lineTextBreaks.forEach(l -> assertThat(l.size(), is(1)));
    }

    @Test
    public void getOptimalTextBreakLines_coloredTextEndsWithNewLine_withNewLinesAndFitsInColumn_shouldReturnMoreThanOneLine() {
        final List<Text> text = List.of(
          new Text("this is a small text\n", Color.RED),
          new Text("that has two\n", Color.BLUE),
          new Text("new lines in it\n", Color.GREEN)
        );

        // Since we have new lines
        final float maxWidth = 2f * PdfUtil.getStringWidth(text.stream().map(Text::getText).collect(Collectors.joining()), new PDType1Font(HELVETICA), 12);

        List<List<Text>> lineTextBreaks = PdfUtil.getOptimalTextBreakLines(text, new PDType1Font(HELVETICA), 12, maxWidth);
        assertThat(lineTextBreaks.size(), is(3));
        lineTextBreaks.forEach(l -> assertThat(l.size(), is(1)));
    }

    @Test
    public void getOptimalTextBreakLines_multiColorLines_withNewLinesAndFitsInColumn_shouldReturnMoreThanOneLine() {
        final List<Text> text = List.of(
          new Text("this is", Color.BLACK), new Text("a small text\n", Color.RED),
          new Text("that has two\n", Color.BLUE),
          new Text("new lines", Color.RED), new Text("in it\n", Color.GREEN),
          new Text("\n", Color.RED),
          new Text("", Color.GREEN)
        );

        // Since we have new lines
        final float maxWidth = 2f * PdfUtil.getStringWidth(text.stream().map(Text::getText).collect(Collectors.joining()), new PDType1Font(HELVETICA), 12);

        List<List<Text>> lineTextBreaks = PdfUtil.getOptimalTextBreakLines(text, new PDType1Font(HELVETICA), 12, maxWidth);
        assertThat(lineTextBreaks.size(), is(4));
        assertThat(lineTextBreaks.get(0).size(), is(2));
        assertThat(lineTextBreaks.get(1).size(), is(1));
        assertThat(lineTextBreaks.get(2).size(), is(2));
        assertThat(lineTextBreaks.get(3).size(), is(1));
    }

    @Test
    public void getOptimalTextBreakLines_onlyLineBreak_shouldNotThrow() {
        final List<Text> text = List.of(
          new Text("\n", Color.RED),
          new Text("\r\n", Color.RED)
        );

        final float maxWidth = 2f * PdfUtil.getStringWidth(text.stream().map(Text::getText).collect(Collectors.joining()), new PDType1Font(HELVETICA), 12);

        List<List<Text>> lineTextBreaks = PdfUtil.getOptimalTextBreakLines(text, new PDType1Font(HELVETICA), 12, maxWidth);
        assertThat(lineTextBreaks.size(), is(0));
    }

    @Test
    public void getOptimalTextBreakLines_colorSpanningManyLines_withNewLinesAndFitsInColumn_shouldReturnMoreThanOneLine() {
        final List<Text> text = List.of(
          new Text("this is a small text\n that has", Color.RED),
          new Text("two\n new lines in it", Color.BLUE)
        );

        // Since we have new lines
        final float maxWidth = 2f * PdfUtil.getStringWidth(text.stream().map(Text::getText).collect(Collectors.joining()), new PDType1Font(HELVETICA), 12);

        List<List<Text>> lineTextBreaks = PdfUtil.getOptimalTextBreakLines(text, new PDType1Font(HELVETICA), 12, maxWidth);
        assertThat(lineTextBreaks.size(), is(3));
        assertThat(lineTextBreaks.get(0).size(), is(1));
        assertThat(lineTextBreaks.get(1).size(), is(2));
        assertThat(lineTextBreaks.get(2).size(), is(1));
    }

    @Test
    public void getOptimalTextBreakLines_noSpacesInText_shouldSplitOnDot() {
        final String text = "This.should.be.splitted.on.a.dot.No.spaces.in.here.";

        final float maxWidth = PdfUtil.getStringWidth("This.should.be.splitted.on.a.dot.", new PDType1Font(HELVETICA), 12);

        final List<List<Text>> lines = PdfUtil.getOptimalTextBreakLines(List.of(new Text(text)), new PDType1Font(HELVETICA), 12, maxWidth);

        assertThat(lines.size(), is(2));
        assertThat(concatLine(lines.get(0)), is(equalTo("This.should.be.splitted.on.a.dot.")));
        assertThat(concatLine(lines.get(1)), is(equalTo("No.spaces.in.here.")));
    }

    @Test
    public void getOptimalTextBreakLines_multiColor_noSpacesInText_shouldSplitOnDot() {
        final List<Text> text = List.of(
          new Text("This.should.be.splitted", Color.RED),
          new Text(".on.a.dot.No.spaces", Color.BLUE),
          new Text(".in.here.", Color.GREEN));

        final float maxWidth = PdfUtil.getStringWidth("This.should.be.splitted.on.a.dot.", new PDType1Font(HELVETICA), 12);

        final List<List<Text>> lines = PdfUtil.getOptimalTextBreakLines(text, new PDType1Font(HELVETICA), 12, maxWidth);

        assertThat(lines.size(), is(2));
        assertThat(lines.get(0).size(), is(2));
        assertThat(lines.get(1).size(), is(2));
        assertThat(lines.get(0).get(0).getColor(), is(Optional.of(Color.RED)));
        assertThat(lines.get(0).get(1).getColor(), is(Optional.of(Color.BLUE)));
        assertThat(lines.get(1).get(0).getColor(), is(Optional.of(Color.BLUE)));
        assertThat(lines.get(1).get(1).getColor(), is(Optional.of(Color.GREEN)));
        assertThat(concatLine(lines.get(0)), is(equalTo("This.should.be.splitted.on.a.dot.")));
        assertThat(concatLine(lines.get(1)), is(equalTo("No.spaces.in.here.")));
    }

    @Test
    public void getOptimalTextBreakLines_noSpacesNorDotsInText_shouldSplitOnComma() {
        final String text = "This,should,be,splitted,on,a,comma,no,space,nor,dots,in,here,";

        final float maxWidth = PdfUtil.getStringWidth("This,should,be,splitted,on,a,comma,", new PDType1Font(HELVETICA), 12);

        final List<List<Text>> lines = PdfUtil.getOptimalTextBreakLines(List.of(new Text(text)), new PDType1Font(HELVETICA), 12, maxWidth);

        assertThat(lines.size(), is(2));
        assertThat(concatLine(lines.get(0)), is(equalTo("This,should,be,splitted,on,a,comma,")));
        assertThat(concatLine(lines.get(1)), is(equalTo("no,space,nor,dots,in,here,")));
    }

    @Test
    public void getOptimalTextBreakLines_noSpacesNorDotsNorCommasInText_shouldSplitBySize() {
        final String text = "ThisDoesNotHaveAnyCharactersWhereWeCouldBreakMoreEasilySoWeBreakBySize";

        final float maxWidth = PdfUtil.getStringWidth("ThisDoesNotHaveAnyCharacters", new PDType1Font(HELVETICA), 12);
        final List<List<Text>> lines = PdfUtil.getOptimalTextBreakLines(List.of(new Text(text)), new PDType1Font(HELVETICA), 12, maxWidth);

        assertThat(lines.size(), is(3));
        assertThat(concatLine(lines.get(0)), is(equalTo("ThisDoesNotHaveAnyCharacter-")));
        assertThat(concatLine(lines.get(1)), is(equalTo("sWhereWeCouldBreakMoreEasi-")));
        assertThat(concatLine(lines.get(2)), is(equalTo("lySoWeBreakBySize")));
    }

    @Test
    public void getOptimalTextBreakLines_noSpacesNorDotsNorCommasInColoredText_shouldSplitBySize() {
        final List<Text> text = List.of(
          new Text("ThisDoesNotHaveAnyCharacters", Color.RED),
          new Text("WhereWeCouldBreakMoreEasily", Color.BLUE),
          new Text("SoWeBreakBySize", Color.GREEN)
        );

        final float maxWidth = PdfUtil.getStringWidth("ThisDoesNotHaveAnyCharacters", new PDType1Font(HELVETICA), 12);
        final List<List<Text>> lines = PdfUtil.getOptimalTextBreakLines(text, new PDType1Font(HELVETICA), 12, maxWidth);

        assertThat(lines.size(), is(3));
        assertThat(concatLine(lines.get(0)), is(equalTo("ThisDoesNotHaveAnyCharacter-")));
        assertThat(lines.get(0).get(0).getColor(), is(Optional.of(Color.RED)));
        assertThat(concatLine(lines.get(1)), is(equalTo("sWhereWeCouldBreakMoreEasi-")));
        assertThat(lines.get(1).get(0).getColor(), is(Optional.of(Color.RED)));
        assertThat(lines.get(1).get(1).getColor(), is(Optional.of(Color.BLUE)));
        assertThat(concatLine(lines.get(2)), is(equalTo("lySoWeBreakBySize")));
        assertThat(lines.get(2).get(0).getColor(), is(Optional.of(Color.BLUE)));
        assertThat(lines.get(2).get(1).getColor(), is(Optional.of(Color.GREEN)));
    }

    @Test(timeout = 5000L)
    public void testVeryBigText() {
        final StringBuilder builder = new StringBuilder();
        final List<String> expectedOutput = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            builder.append("https://averylonginternetdnsnamewhich-maybe-breaks-easytable.com ");

            // optimal text-break
            expectedOutput.add("https://averylonginternetdns-");
            expectedOutput.add("namewhich-maybe-breaks-");
            expectedOutput.add("easytable.com");
        }

        final List<List<Text>> actualOutput = PdfUtil.getOptimalTextBreakLines(List.of(new Text(builder.toString())), new PDType1Font(HELVETICA), 8, 102);
        final List<String> linesAsStrings = actualOutput.stream().map(this::concatLine).collect(Collectors.toList());

        for (int i = 0; i < expectedOutput.size(); i++) {
            assertThat(linesAsStrings.get(i), equalTo(expectedOutput.get(i)));
        }
    }

    @Test(timeout = 5000L)
    public void testVeryBigColoredText() {
        List<Text> text = new ArrayList<>();
        final List<List<Text>> expectedOutput = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            text.add(new Text("https://averylonginternetdnsname", Color.BLUE));
            text.add(new Text("which-maybe-breaks-easytable.com ", Color.RED));

            // optimal text-break
            expectedOutput.addAll(List.of(
              List.of(new Text("https://averylonginternetdns-", Color.BLUE)),
              List.of(new Text("name", Color.BLUE), new Text("which-maybe-breaks-", Color.RED)),
              List.of(new Text("easytable.com", Color.RED))
            ));
        }

        final List<List<Text>> actualOutput = PdfUtil.getOptimalTextBreakLines(text, new PDType1Font(HELVETICA), 8, 102);

        assertThat(actualOutput.size(), equalTo(expectedOutput.size()));
    }

    private String concatLine(List<Text> line) {
        return line.stream().map(Text::getText).collect(Collectors.joining());
    }

}
