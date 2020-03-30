package com.aquent.rambo.easytable.structure.cell.paragraph;

import com.aquent.rambo.easytable.settings.Settings;

import rst.pdfbox.layout.elements.Paragraph;

public interface ParagraphProcessable {

    void process(Paragraph paragraph, Settings settings);

}
