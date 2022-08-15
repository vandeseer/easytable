package org.vandeseer.easytable.util;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;

import java.util.*;
import java.util.stream.Collectors;

import static org.vandeseer.easytable.util.FloatUtil.isEqualInEpsilon;

/**
 * Provides some helping functions.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PdfUtil {

    public static final String NEW_LINE_REGEX = "\\r?\\n";


    /**
     * Computes the width of a String (in points).
     *
     * @param text     Text
     * @param font     Font of Text
     * @param fontSize FontSize of String
     * @return Width (in points)
     */
    public static float getStringWidth(final String text, final PDFont font, final int fontSize) {
        return Arrays.stream(text.split(NEW_LINE_REGEX))
                .max(Comparator.comparing(String::length))
                .map(x -> getWidthOfStringWithoutNewlines(x, font, fontSize))
                .orElseThrow(CouldNotDetermineStringWidthException::new);
    }

    @SneakyThrows
    private static float getWidthOfStringWithoutNewlines(String text, PDFont font, int fontSize) {

        final List<String> codePointsAsString = text.codePoints()
                .mapToObj(codePoint -> new String(new int[]{codePoint}, 0, 1))
                .collect(Collectors.toList());

        List<Float> widths = new ArrayList<>();

        for (final String codepoint : codePointsAsString) {
            try {
                widths.add(font.getStringWidth(codepoint) * fontSize / 1000F);
            } catch (final IllegalArgumentException e) {
                widths.add(font.getStringWidth("–") * fontSize / 1000F);
            }
        }

        return widths.stream().reduce(0.0f, Float::sum);
    }


    /**
     * Computes the height of a font.
     *
     * @param font     Font
     * @param fontSize FontSize
     * @return Height of font
     */
    static public float getFontHeight(final PDFont font, final int fontSize) {
        return getFontHeight(font, fontSize, false);
    }

    static public float getFontHeight(final PDFont font, final int fontSize, boolean backwardsCompatible) {
        if (backwardsCompatible) {
            return font.getFontDescriptor().getCapHeight() * fontSize / 1000F;
        } else {
            final PDFontDescriptor fontDescriptor = font.getFontDescriptor();
            return (fontDescriptor.getFontBoundingBox().getHeight() - fontDescriptor.getLeading()) / 1000.0f * fontSize;
        }
    }

    /**
     * Split a text into multiple lines to prevent a text-overflow.
     *
     * @param text     Text
     * @param font     Used font
     * @param fontSize Used fontSize
     * @param maxWidth Maximal width of resulting text-lines
     * @return A list of lines, where all are smaller than maxWidth
     */
    public static List<String> getOptimalTextBreakLines(final String text, final PDFont font, final int fontSize, final float maxWidth) {
        final List<String> result = new ArrayList<>();

        for (final String line : text.split(NEW_LINE_REGEX)) {
            if (PdfUtil.doesTextLineFit(line, font, fontSize, maxWidth)) {
                result.add(line);
            } else {
                result.addAll(PdfUtil.wrapLine(line, font, fontSize, maxWidth));
            }
        }

        return result;
    }

    private static List<String> wrapLine(final String line, final PDFont font, final int fontSize, final float maxWidth) {
        if (PdfUtil.doesTextLineFit(line, font, fontSize, maxWidth)) {
            return Collections.singletonList(line);
        }

        List<String> goodLines = new ArrayList<>();
        Stack<String> allWords = new Stack<>();
        Arrays.asList(line.split("(?<=[\\\\. ,-])")).forEach(allWords::push);
        Collections.reverse(allWords);

        while (!allWords.empty()) {
            goodLines.add(buildALine(allWords, font, fontSize, maxWidth));
        }

        return goodLines;
    }

    private static List<String> splitBySize(final String line, final PDFont font, final int fontSize, final float maxWidth) {
        final List<String> returnList = new ArrayList<>();

        for (int i = line.length() - 1; i > 0; i--) {
            final String fittedNewLine = line.substring(0, i) + "-";
            final String remains = line.substring(i);

            if (PdfUtil.doesTextLineFit(fittedNewLine, font, fontSize, maxWidth)) {
                returnList.add(fittedNewLine);
                returnList.addAll(PdfUtil.wrapLine(remains, font, fontSize, maxWidth));

                break;
            }
        }

        return returnList;
    }


    private static String buildALine(final Stack<String> words,
                                     final PDFont font,
                                     final int fontSize,
                                     final float maxWidth) {

        final StringBuilder line = new StringBuilder();
        float width = 0;

        while (!words.empty()) {
            float nextWordWidth = getStringWidth(words.peek(), font, fontSize);

            // if a single char on an empty line bigger than the max-size, then there is no split possible.
            if (line.length() == 0 && words.peek().length() == 1 && nextWordWidth > maxWidth) {
                return words.pop();
            }

            if (doesTextLineFit(width + nextWordWidth, maxWidth)) {
                line.append(words.pop());
                width += nextWordWidth;
            } else {
                break;
            }
        }

        // no word found -> split by size
        if (width == 0 && !words.empty()) {
            List<String> cutBySize = splitBySize(words.pop(), font, fontSize, maxWidth);
            Collections.reverse(cutBySize);
            cutBySize.forEach(words::push);

            return buildALine(words, font, fontSize, maxWidth);
        }

        return line.toString().trim();
    }

    private static boolean doesTextLineFit(final String textLine, final PDFont font, final int fontSize, final float maxWidth) {
        return doesTextLineFit(PdfUtil.getStringWidth(textLine, font, fontSize), maxWidth);
    }

    private static boolean doesTextLineFit(final float stringWidth, final float maxWidth) {
        // Conceptually we want to calculate:
        //
        //     maxWidth >= PdfUtil.getStringWidth(line, font, fontSize)
        //
        // But this does may not work as expected due to floating point arithmetic.
        // Hence we use a delta here that is sufficient for our purposes.

        //noinspection SuspiciousNameCombination
        if (isEqualInEpsilon(stringWidth, maxWidth)) return true; // we consider the two numbers being equal

        return maxWidth > stringWidth;
    }


    private static class CouldNotDetermineStringWidthException extends RuntimeException {
        CouldNotDetermineStringWidthException() {
            super();
        }
    }

}
