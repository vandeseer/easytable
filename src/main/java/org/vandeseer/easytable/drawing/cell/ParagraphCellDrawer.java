package org.vandeseer.easytable.drawing.cell;

import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.text.Alignment;
import rst.pdfbox.layout.text.DrawContext;
import rst.pdfbox.layout.text.Position;
import rst.pdfbox.layout.text.annotations.AnnotationDrawListener;

import java.util.EnumMap;
import java.util.Map;

public class ParagraphCellDrawer extends AbstractCellDrawer<ParagraphCell> {

    private static final Map<HorizontalAlignment, Alignment> ALIGNMENT_MAP = new EnumMap<>(HorizontalAlignment.class);
    static {
        ALIGNMENT_MAP.put(HorizontalAlignment.LEFT, Alignment.Left);
        ALIGNMENT_MAP.put(HorizontalAlignment.RIGHT, Alignment.Right);
        ALIGNMENT_MAP.put(HorizontalAlignment.CENTER, Alignment.Center);
        ALIGNMENT_MAP.put(HorizontalAlignment.JUSTIFY, Alignment.Justify);
    }

    public ParagraphCellDrawer(ParagraphCell cell) {
        this.cell = cell;
    }

    @SneakyThrows
    @Override
    public void drawContent(DrawingContext drawingContext) {
        Paragraph paragraph = cell.getParagraph().getWrappedParagraph();

        AnnotationDrawListener annotationDrawListener = createAndGetAnnotationDrawListenerWith(drawingContext);

        float x = drawingContext.getStartingPoint().x + cell.getPaddingLeft();
        float y = drawingContext.getStartingPoint().y + getAdaptionForVerticalAlignment();

        paragraph.drawText(
                drawingContext.getContentStream(),
                new Position(x, y),
                ALIGNMENT_MAP.getOrDefault(cell.getSettings().getHorizontalAlignment(), Alignment.Left),
                annotationDrawListener
        );

        annotationDrawListener.afterRender();
        drawingContext.getPage().getAnnotations().forEach(PDAnnotation::constructAppearances);
    }

    @SneakyThrows
    @Override
    protected float calculateInnerHeight() {
        return cell.getParagraph().getWrappedParagraph().getHeight();
    }

    private AnnotationDrawListener createAndGetAnnotationDrawListenerWith(DrawingContext drawingContext) {
        return new AnnotationDrawListener(new DrawContext() {
                @Override
                public PDDocument getPdDocument() {
                    return null;
                }

                @Override
                public PDPage getCurrentPage() {
                    return drawingContext.getPage();
                }

                @Override
                public PDPageContentStream getCurrentPageContentStream() {
                    return drawingContext.getContentStream();
                }
            });
    }

}
