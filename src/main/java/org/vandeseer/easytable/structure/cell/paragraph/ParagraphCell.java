package org.vandeseer.easytable.structure.cell.paragraph;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.cell.ParagraphCellDrawer;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
public class ParagraphCell extends AbstractCell {

    @Builder.Default
    protected float lineSpacing = 1f;

    private Paragraph paragraph;

    @Override
    public void setWidth(float width) {
        super.setWidth(width);

        // Clear the paragraph just in case ...
        while(getParagraph().getWrappedParagraph().removeLast() != null) {}

        getParagraph().getProcessables().forEach(
                processable -> processable.process(getParagraph().getWrappedParagraph(), getSettings())
        );

        rst.pdfbox.layout.elements.Paragraph wrappedParagraph = paragraph.getWrappedParagraph();
        wrappedParagraph.setLineSpacing(getLineSpacing());
        wrappedParagraph.setApplyLineSpacingToFirstLine(false);
        wrappedParagraph.setMaxWidth(width - getHorizontalPadding());
    }

    @Override
    protected Drawer createDefaultDrawer() {
        return new ParagraphCellDrawer(this);
    }

    @SneakyThrows
    @Override
    public float getMinHeight() {
        float height = paragraph.getWrappedParagraph().getHeight() + getVerticalPadding();
        return height > super.getMinHeight()
                ? height
                : super.getMinHeight();
    }

    public static class Paragraph {

        @Getter(AccessLevel.PACKAGE)
        private final List<ParagraphProcessable> processables;

        @Getter
        private rst.pdfbox.layout.elements.Paragraph wrappedParagraph = new rst.pdfbox.layout.elements.Paragraph();

        public Paragraph(List<ParagraphProcessable> processables) {
            this.processables = processables;
        }

        public static class ParagraphBuilder {

            // TODO naming ;-)
            private List<ParagraphProcessable> processables = new LinkedList<>();

            private ParagraphBuilder() {
            }

            @SneakyThrows
            public ParagraphBuilder append(StyledText styledText) {
                processables.add(styledText);
                return this;
            }

            @SneakyThrows
            public ParagraphBuilder append(Hyperlink hyperlink) {
                processables.add(hyperlink);
                return this;
            }

            @SneakyThrows
            public ParagraphBuilder append(Markup markup) {
                processables.add(markup);
                return this;
            }

            public ParagraphBuilder appendNewLine() {
                processables.add(new NewLine());
                return this;
            }

            public ParagraphBuilder appendNewLine(float fontSize) {
                processables.add(new NewLine(fontSize));
                return this;
            }

            public Paragraph build() {
                return new Paragraph(processables);
            }
        }

        public static ParagraphBuilder builder() {
            return new ParagraphBuilder();
        }
    }

    // Adaption for Lombok
    public abstract static class ParagraphCellBuilder<C extends ParagraphCell, B extends ParagraphCell.ParagraphCellBuilder<C, B>> extends AbstractCellBuilder<C, B> {

        public B font(final PDFont font) {
            settings.setFont(font);
            return this.self();
        }

        public B fontSize(final Integer fontSize) {
            settings.setFontSize(fontSize);
            return this.self();
        }

        public B textColor(final Color textColor) {
            settings.setTextColor(textColor);
            return this.self();
        }

    }

}
