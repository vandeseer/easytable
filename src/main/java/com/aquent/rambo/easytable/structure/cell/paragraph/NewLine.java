package com.aquent.rambo.easytable.structure.cell.paragraph;

import com.aquent.rambo.easytable.settings.Settings;

import rst.pdfbox.layout.elements.Paragraph;

public class NewLine implements ParagraphProcessable {

    Float fontSize;

    NewLine(float fontSize) {
        this.fontSize = fontSize;
    }

    NewLine() {
    }

    @Override
    public void process(Paragraph paragraph, Settings settings) {
        float actualFontSize = fontSize != null ? fontSize : settings.getFontSize();
        paragraph.add(new rst.pdfbox.layout.text.NewLine(actualFontSize));
    }

}
