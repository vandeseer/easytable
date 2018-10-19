package org.vandeseer.pdfbox.easytable;


import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides some helping functions.
 */
public class PdfUtil {

    /**
     * Computes the width of a String (in points).
     *
     * @param text     Text
     * @param font     Font of Text
     * @param fontSize FontSize of String
     * @return Width (in points)
     */
    public static float getStringWidth(final String text, final PDFont font, final int fontSize) {
        try {
            return font.getStringWidth(text) * fontSize / 1000F;
        } catch (final Exception ex) {
            throw new RuntimeException("error while get strings width", ex);
        }
    }


    /**
     * Computes the height of a font.
     *
     * @param font     Font
     * @param fontSize FontSize
     * @return Height of font
     */
    public static float getFontHeight(final PDFont font, final int fontSize) {
        return font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
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
    public static List<String> getOptimalTextBreak(final String text, final PDFont font, final int fontSize, final float maxWidth) {

        if (PdfUtil.isLineFine(text, font, fontSize, maxWidth)) {
            return Collections.singletonList(text);
        } else {
            return PdfUtil.wrapLine(text, font, fontSize, maxWidth);
        }

    }

    private static List<String> wrapLine(final String line, final PDFont font, final int fontSize, final float maxWidth) {
        if (PdfUtil.isLineFine(line, font, fontSize, maxWidth)) {
            return Collections.singletonList(line);
        }

        final List<String> returnList = new ArrayList<>();

        // first step - try split by space
        final List<String> splitBySpace = Arrays.asList(line.split(" "));

        for (int i = splitBySpace.size() - 1; i >= 0; i--) {
            final String fittedNewLine = StringUtils.join(splitBySpace.subList(0, i), " ");
            final String remains = StringUtils.join(splitBySpace.subList(i, splitBySpace.size()), " ");

            if (PdfUtil.isLineFine(fittedNewLine, font, fontSize, maxWidth)) {
                returnList.add(fittedNewLine);
                returnList.addAll(PdfUtil.wrapLine(remains, font, fontSize, maxWidth));
                break;
            }
        }

        return returnList;
    }


    private static boolean isLineFine(final String line, final PDFont font, final int fontSize, final float maxWidth) {
        try {
            return maxWidth >= PdfUtil.getStringWidth(line, font, fontSize);
        } catch (final Exception ex) {
            throw new RuntimeException("error while check size of string", ex);
        }
    }


}
