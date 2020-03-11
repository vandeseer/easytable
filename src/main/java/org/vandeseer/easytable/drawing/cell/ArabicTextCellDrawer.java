package org.vandeseer.easytable.drawing.cell;

import lombok.NoArgsConstructor;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.structure.cell.ArabicTextCell;
import org.vandeseer.easytable.util.PdfUtil;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class ArabicTextCellDrawer extends TextCellDrawer<ArabicTextCell> {

    public ArabicTextCellDrawer(ArabicTextCell arabicTextCell) {
        this.cell = arabicTextCell;
    }

    @Override
    protected List<String> calculateAndGetLines(PDFont currentFont, int currentFontSize, float maxWidth) {
        return cell.isWordBreak()
                ? getReversedLatinLines(cell.getText(), currentFont, currentFontSize, maxWidth)
                : Collections.singletonList(cell.getText());
    }

    private List<String> getReversedLatinLines(String text, PDFont currentFont, int currentFontSize, float maxWidth) {
        List<String> latinLines = PdfUtil.getOptimalTextBreakLines(cell.getText(), currentFont, currentFontSize, maxWidth);
        Collections.reverse(latinLines);
        return latinLines;
    }

}
