package org.vandeseer.easytable.drawing;

import lombok.Builder;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.structure.Text;
import java.util.List;

import java.awt.*;

@Builder(toBuilder = true)
@Getter
public class PositionedStyledText {

    private final float x;
    private final float y;
    private final List<Text> text;
    private final PDFont font;
    private final int fontSize;
    private final Color color;

}
