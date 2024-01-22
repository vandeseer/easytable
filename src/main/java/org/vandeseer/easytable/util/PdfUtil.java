package org.vandeseer.easytable.util;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.structure.Text;

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
    public static float getFontHeight(final PDFont font, final int fontSize) {
        return font.getFontDescriptor().getCapHeight() * fontSize / 1000F;
    }

    /**
     * Split a text into multiple lines to prevent a text-overflow.
     *
     * One line is represented as List of Text. When there are multiple different colored parts in a line,
     * then the line is split into multiple Texts.
     *
     * "This is an example\nWhere color changes here | and then there is a new line\nAnd another one"
     * This example should be split into 3 lines:
     * 1. "This is an example"
     * 2. "Where color changes here and then there is a new line"
     * 3. "And another one"
     *
     * If Text does not end with a newline, it's possible that the last line of the text is part of a multi-colored line.
     * To determine if this is the case we don't append but save the last line after \n.
     * On the next Text, instead of starting with an empty List of Text, we start with the saved line.
     *
     * @param texts    Texts to split
     * @param font     Used font
     * @param fontSize Used fontSize
     * @param maxWidth Maximal width of resulting text-lines
     * @return A list of lines, where all are smaller than maxWidth
     */
    public static List<List<Text>> getOptimalTextBreakLines(final List<Text> texts, final PDFont font, final int fontSize, final float maxWidth) {
        final List<List<Text>> result = new ArrayList<>();

        List<Text> unfinishedLine = new ArrayList<>();
        for (Text t : texts) {
            List<Text> lines = Arrays.stream(t.getText().split(NEW_LINE_REGEX)).map(x -> new Text(x, t.getColor().orElse(null))).collect(Collectors.toList());
            if (lines.isEmpty() && endsWithNewLine(t.getText())) {
                result.add(List.of(new Text("\n")));
                continue;
            }
            Text lastLine = lines.get(lines.size() - 1);
            if (!endsWithNewLine(t.getText())) {
                lines = lines.subList(0, lines.size() - 1);
            }

            for (Text lineText : lines) {
                List<Text> lineToAppend = new ArrayList<>(unfinishedLine);
                unfinishedLine = new ArrayList<>();
                lineToAppend.add(lineText);
                if (PdfUtil.doesTextLineFit(lineToAppend.stream().map(Text::getText).collect(Collectors.joining()), font, fontSize, maxWidth)) {
                    result.add(lineToAppend);
                } else {
                    result.addAll(PdfUtil.wrapLine(lineToAppend, font, fontSize, maxWidth));
                }
            }

            if (!endsWithNewLine(t.getText())) {
                unfinishedLine.add(lastLine);
            }
        }

        if (!unfinishedLine.isEmpty()) {
            if (PdfUtil.doesTextLineFit(unfinishedLine.stream().map(Text::getText).collect(Collectors.joining()), font, fontSize, maxWidth)) {
                result.add(unfinishedLine);
            } else {
                result.addAll(PdfUtil.wrapLine(unfinishedLine, font, fontSize, maxWidth));
            }
        }

        return result;
    }

    private static boolean endsWithNewLine(final String text) {
        return text.endsWith("\n") || text.endsWith("\r\n");
    }

    private static List<List<Text>> wrapLine(final List<Text> line, final PDFont font, final int fontSize, final float maxWidth) {
        if (PdfUtil.doesTextLineFit(line.stream().map(Text::getText).collect(Collectors.joining()), font, fontSize, maxWidth)) {
            return Collections.singletonList(line);
        }

        List<List<Text>> goodLines = new ArrayList<>();
        Stack<List<Text>> allWords = new Stack<>();

        List<Text> unfinishedWord = new ArrayList<>();
        for (Text t : line) {
            List<Text> words = Arrays.stream(t.getText().split("(?<=[\\\\. ,-])")).map(x -> new Text(x, t.getColor().orElse(null))).collect(Collectors.toList());
            Text lastWord = words.get(words.size() - 1);
            if (!endsWithFullWord(t.getText())) {
                words = words.subList(0, words.size() - 1);
            }

            for (Text word : words) {
                List<Text> wordToAppend = new ArrayList<>(unfinishedWord);
                unfinishedWord = new ArrayList<>();
                wordToAppend.add(word);
                allWords.push(wordToAppend);
            }

            if (!endsWithFullWord(t.getText())) {
                unfinishedWord.add(lastWord);
            }
        }

        if (!unfinishedWord.isEmpty()) {
            allWords.push(unfinishedWord);
        }

        Collections.reverse(allWords);

        while (!allWords.empty()) {
            goodLines.add(buildALine(allWords, font, fontSize, maxWidth));
        }

        return goodLines;
    }

    private static boolean endsWithFullWord(final String text) {
        return text.endsWith("(?<=[\\\\. ,-])");
    }

    private static List<List<Text>> splitBySize(final List<Text> line, final PDFont font, final int fontSize, final float maxWidth) {
        final List<List<Text>> returnList = new ArrayList<>();

        for (int i = line.size() - 1; i >= 0; i--) {
            LinkedList<Text> fittedNewLine = new LinkedList<>(line.subList(0, i));
            LinkedList<Text> remainsLine = new LinkedList<>(line.subList(i + 1, line.size()));
            boolean fitted = false;
            for (int j = line.get(i).getText().length() - 1; j > 0; j--) {
                final String fittedNewText = line.get(i).getText().substring(0, j) + "-";
                final String remains = line.get(i).getText().substring(j);

                fittedNewLine.addLast(new Text(fittedNewText, line.get(i).getColor().orElse(null)));
                remainsLine.addFirst(new Text(remains, line.get(i).getColor().orElse(null)));

                if (PdfUtil.doesTextLineFit(fittedNewLine.stream().map(Text::getText).collect(Collectors.joining()), font, fontSize, maxWidth)) {
                    returnList.add(fittedNewLine);
                    fitted = true;
                    returnList.addAll(PdfUtil.wrapLine(remainsLine, font, fontSize, maxWidth));
                    break;
                } else {
                    fittedNewLine.removeLast();
                    remainsLine.removeFirst();
                }
            }
            if (fitted) {
                break;
            }
        }

        return returnList;
    }


    private static List<Text> buildALine(final Stack<List<Text>> words,
                                     final PDFont font,
                                     final int fontSize,
                                     final float maxWidth) {

        List<Text> line = new ArrayList<>();
        float width = 0;

        while (!words.empty()) {
            List<Text> nextWord = words.peek();
            float nextWordWidth = getStringWidth(nextWord.stream().map(Text::getText).collect(Collectors.joining()), font, fontSize);

            // if a single char on an empty line bigger than the max-size, then there is no split possible.
            if (line.isEmpty() && nextWord.stream().map(Text::getText).collect(Collectors.joining()).length() == 1 && nextWordWidth > maxWidth) {
                return words.pop();
            }

            if (doesTextLineFit(width + nextWordWidth, maxWidth)) {
                line.addAll(new ArrayList<>(words.pop()));
                width += nextWordWidth;
            } else {
                break;
            }
        }

        // no word found -> split by size
        if (width == 0 && !words.empty()) {
            List<List<Text>> cutBySize = splitBySize(words.pop(), font, fontSize, maxWidth);
            Collections.reverse(cutBySize);
            cutBySize.forEach(words::push);

            return buildALine(words, font, fontSize, maxWidth);
        }

        if (!line.isEmpty()) {
            line = mergeSameColors(line);
            String endTrimmed = line.get(line.size() - 1).getText().replaceAll("\\s+$", "");
            line.set(line.size() - 1, new Text(endTrimmed, line.get(line.size() - 1).getColor().orElse(null)));

            String startTrimmed = line.get(0).getText().replaceAll("^\\s+", "");
            line.set(0, new Text(startTrimmed, line.get(0).getColor().orElse(null)));
        }
        return line;
    }

    private static List<Text> mergeSameColors(final List<Text> texts) {
        final List<Text> merged = new ArrayList<>();
        for (Text t : texts) {
            if (merged.isEmpty()) {
                merged.add(t);
            } else {
                Text last = merged.get(merged.size() - 1);
                if (last.getColor().equals(t.getColor())) {
                    merged.set(merged.size() - 1, new Text(last.getText() + t.getText(), last.getColor().orElse(null)));
                } else {
                    merged.add(t);
                }
            }
        }
        return merged;
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
