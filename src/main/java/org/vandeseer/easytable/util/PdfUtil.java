package org.vandeseer.easytable.util;


import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides some helping functions.
 */
public class PdfUtil {

    private static final String NEW_LINE_REGEX = "\\r?\\n";

    /**
     * The delta that is still acceptable in float comparisons.
     */
    private static final double EPSILON = 0.0001;

    private PdfUtil() {

    }

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
        final StringBuilder printable = new StringBuilder();
        int unprintable = 0;

        final List<String> codePointsAsString = text.codePoints()
                .mapToObj(codePoint -> new String(new int[]{codePoint}, 0, 1))
                .collect(Collectors.toList());

        for (final String codepoint : codePointsAsString) {
            try {
                font.encode(codepoint);
                printable.append(codepoint);
            } catch (final IllegalArgumentException e) {
                unprintable++;
            }
        }

        final float unprintableLength = font.getStringWidth("–") * unprintable * fontSize / 1000F;
        final float printableLength = font.getStringWidth(printable.toString()) * fontSize / 1000F;
        return printableLength + unprintableLength;
    }


    /**
     * Computes the height of a font.
     *
     * @param font     Font
     * @param fontSize FontSize
     * @return Height of font
     */
    public static float getFontHeight(final PDFont font, final int fontSize) {
        return font.getFontDescriptor().getCapHeight() * fontSize / 1000F;
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
        final List<String> result = new LinkedList<>();

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

        final List<String> resultLines = PdfUtil.splitByWords(line, font, fontSize, maxWidth);

        if (resultLines.isEmpty()) {
            resultLines.addAll(PdfUtil.splitBySize(line, font, fontSize, maxWidth));
        }

        return resultLines;
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

    private static List<String> splitByWords(final String line, final PDFont font, final int fontSize, final float maxWidth) {
        final List<String> returnList = new ArrayList<>();
        final List<String> splitBySpace = Arrays.asList(line.split(" "));

        for (int i = splitBySpace.size() - 1; i >= 0; i--) {
            final String fittedNewLine = String.join(" ", splitBySpace.subList(0, i));
            final String remains = String.join(" ", splitBySpace.subList(i, splitBySpace.size()));

            if (!fittedNewLine.isEmpty() && PdfUtil.doesTextLineFit(fittedNewLine, font, fontSize, maxWidth)) {
                returnList.add(fittedNewLine);

                if (!Objects.equals(remains, line)) {
                    returnList.addAll(PdfUtil.wrapLine(remains, font, fontSize, maxWidth));
                }
                break;
            }
        }

        return returnList;
    }

    private static boolean doesTextLineFit(final String textLine, final PDFont font, final int fontSize, final float maxWidth) {
        // Conceptually we want to calculate:
        //
        //     maxWidth >= PdfUtil.getStringWidth(line, font, fontSize)
        //
        // But this does may not work as expected due to floating point arithmetic.
        // Hence we use a delta here that is sufficient for our purposes.

        final float stringWidth = PdfUtil.getStringWidth(textLine, font, fontSize);
        float difference = Math.abs(maxWidth - stringWidth);

        if (difference < EPSILON) {
            return true; // we consider the two numbers being equal
        }

        return maxWidth > stringWidth;
    }


    private static class CouldNotDetermineStringWidthException extends RuntimeException {
        CouldNotDetermineStringWidthException() {
            super();
        }

        CouldNotDetermineStringWidthException(Exception exception) {
            super(exception);
        }
    }

}
