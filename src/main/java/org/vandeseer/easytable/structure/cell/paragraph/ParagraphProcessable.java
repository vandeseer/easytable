package org.vandeseer.easytable.structure.cell.paragraph;

import org.vandeseer.easytable.settings.Settings;
import rst.pdfbox.layout.elements.Paragraph;

public interface ParagraphProcessable {

    void process(Paragraph paragraph, Settings settings);

}
