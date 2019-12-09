package org.vandeseer.easytable.structure.cell.paragraph;

import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.cell.ParagraphCellDrawer;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.util.LinkedList;
import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
public class ParagraphCell extends AbstractCell {

    @Builder.Default
    protected float lineSpacing = 1f;

    private Paragraph paragraph;

    // Custom builder (Lombok)
    public static ParagraphCellBuilder<?, ?> builder() {
        return new CustomParagraphCellBuilderImpl();
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
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

        @Getter
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

            public Paragraph build() {
                return new Paragraph(processables);
            }
        }

        public static ParagraphBuilder builder() {
            return new ParagraphBuilder();
        }
    }

    private static final class CustomParagraphCellBuilderImpl extends ParagraphCell.ParagraphCellBuilder<ParagraphCell, ParagraphCell.CustomParagraphCellBuilderImpl> {
        private CustomParagraphCellBuilderImpl() {
        }

        @Override
        protected CustomParagraphCellBuilderImpl self() {
            return this;
        }

        @Override
        public ParagraphCell build() {
            ParagraphCell paragraphCell = new ParagraphCell(this);
            paragraphCell.buildWrappedParagraph();
            return paragraphCell;
        }
    }

    private void buildWrappedParagraph() {
        this.paragraph.getProcessables().forEach(__ -> __.process(this.paragraph.getWrappedParagraph(), this.getSettings()));
    }

}
