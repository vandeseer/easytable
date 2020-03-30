package com.aquent.rambo.easytable.structure.cell.paragraph;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.apache.pdfbox.pdmodel.font.PDFont;

import com.aquent.rambo.easytable.settings.Settings;

import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.text.annotations.AnnotatedStyledText;
import rst.pdfbox.layout.text.annotations.Annotations;

import java.awt.*;
import java.util.Collections;

@Builder
@Getter
public class Hyperlink implements ParagraphProcessable {

    @NonNull
    private String text;

    @NonNull
    private String url;

    private PDFont font;

    private Float fontSize;

    @Builder.Default
    private Color color = Color.BLUE;

    @Builder.Default
    private float baselineOffset = 1f;

    @Override
    public void process(Paragraph paragraph, Settings settings) {
        Annotations.HyperlinkAnnotation hyperlink =
                new Annotations.HyperlinkAnnotation(
                        getUrl(),
                        Annotations.HyperlinkAnnotation.LinkStyle.ul
                );

        paragraph.add(
                new AnnotatedStyledText(
                        getText(),
                        getFontSize() != null ? getFontSize() : settings.getFontSize(),
                        getFont() != null ? getFont() : settings.getFont(),
                        getColor(),
                        getBaselineOffset(),
                        Collections.singleton(hyperlink)
                )
        );
    }

}