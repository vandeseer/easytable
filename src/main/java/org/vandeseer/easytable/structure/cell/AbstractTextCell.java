package org.vandeseer.easytable.structure.cell;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;

@Getter
@SuperBuilder(toBuilder = true)
public abstract class AbstractTextCell extends AbstractCell {

    @NonNull
    protected String text;

    protected Color textColor;

    @Builder.Default
    protected float lineSpacing = 1f;

    public PDFont getFont() {
        return settings.getFont();
    }

    public Integer getFontSize() {
        return settings.getFontSize();
    }

    public Color getTextColor() {
        return settings.getTextColor();
    }


    // Adaption for Lombok
    public abstract static class AbstractTextCellBuilder<C extends AbstractTextCell, B extends AbstractTextCell.AbstractTextCellBuilder<C, B>> extends AbstractCellBuilder<C, B> {

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
