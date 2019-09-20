package org.vandeseer.easytable.util;


import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        List<String> result = new ArrayList<>();
        result.add(line);

        final List<String> splitValues = List.of(" ", ".", ",");

        for (final String splitWith : splitValues) {
            result = result.stream()
                    .flatMap(subLine -> {
                        List<String> newLines = PdfUtil.splitBy(splitWith, subLine, font, fontSize, maxWidth);

                        if (newLines.isEmpty()) {
                            newLines.add(line);
                        }

                        return newLines.stream();
                    })
                    .collect(Collectors.toList());

            if (result.stream().allMatch(subLine -> PdfUtil.doesTextLineFit(subLine, font, fontSize, maxWidth))) {
                break;
            }
        }

        return result.stream().flatMap(subLine -> {
            if (PdfUtil.doesTextLineFit(subLine, font, fontSize, maxWidth)) {
                return Stream.of(subLine);
            } else {
                return PdfUtil.splitBySize(subLine, font, fontSize, maxWidth).stream();
            }
        }).collect(Collectors.toList());


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


    /**
     * Try to find the optimal split so the text is not larger than maxWidth.
     *
     * @param by       Try to split optimal with this value
     * @param line     Raw line, which has to be smaller as maxWidth
     * @param font     Used font (to determine the text-width)
     * @param fontSize Used font-size (to determine the text-width)
     * @param maxWidth Maximum width for the text
     * @return Parts of line that are smaller than maxWidth.
     * Its possible that these parts are larger (so there was not a split possible)
     */
    private static List<String> splitBy(final String by,
                                        final String line,
                                        final PDFont font,
                                        final int fontSize,
                                        final float maxWidth) {

        final List<String> returnList = new ArrayList<>();
        final List<String> splitBy = Arrays.asList(line.split(by));

        for (int i = splitBy.size() - 1; i >= 0; i--) {
            final String fittedNewLine = String.join(by, splitBy.subList(0, i));
            final String remains = String.join(by, splitBy.subList(i, splitBy.size()));

            if (!fittedNewLine.isEmpty() && PdfUtil.doesTextLineFit(fittedNewLine, font, fontSize, maxWidth)) {
                returnList.add(String.format("%s%s", fittedNewLine, by).trim());

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
