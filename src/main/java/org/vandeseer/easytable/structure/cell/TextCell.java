package org.vandeseer.easytable.structure.cell;

import lombok.NonNull;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.cell.TextCellDrawer;
import org.vandeseer.easytable.structure.Text;

import java.util.List;

public class TextCell extends AbstractTextCell {
    @NonNull
    protected List<Text> text;

    protected Drawer createDefaultDrawer() {
        return new TextCellDrawer(this);
    }

    public static abstract class TextCellBuilder<C extends TextCell, B extends TextCell.TextCellBuilder<C, B>> extends AbstractTextCell.AbstractTextCellBuilder<C, B> {
        @java.lang.SuppressWarnings("all")
        private List<Text> text;

        @java.lang.SuppressWarnings("all")
        public B text(@NonNull final String text) {
            if (text == null) {
                throw new java.lang.NullPointerException("text is marked non-null but is null");
            }
            this.text = List.of(new Text(text, this.settings.getTextColor()));
            return self();
        }

        @java.lang.SuppressWarnings("all")
        public B coloredText(@NonNull final List<Text> text) {
            if (text == null) {
                throw new java.lang.NullPointerException("text is marked non-null but is null");
            }
            this.text = text;
            return self();
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        protected B $fillValuesFrom(final C instance) {
            super.$fillValuesFrom(instance);
            TextCell.TextCellBuilder.$fillValuesFromInstanceIntoBuilder(instance, this);
            return self();
        }

        @java.lang.SuppressWarnings("all")
        private static void $fillValuesFromInstanceIntoBuilder(final TextCell instance, final TextCell.TextCellBuilder<?, ?> b) {
            b.coloredText(instance.text);
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        protected abstract B self();

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public abstract C build();

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public java.lang.String toString() {
            return "TextCell.TextCellBuilder(super=" + super.toString() + ", text=" + this.text + ")";
        }
    }


    @java.lang.SuppressWarnings("all")
    private static final class TextCellBuilderImpl extends TextCell.TextCellBuilder<TextCell, TextCell.TextCellBuilderImpl> {
        @java.lang.SuppressWarnings("all")
        private TextCellBuilderImpl() {
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        protected TextCell.TextCellBuilderImpl self() {
            return this;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public TextCell build() {
            return new TextCell(this);
        }
    }

    @java.lang.SuppressWarnings("all")
    protected TextCell(final TextCell.TextCellBuilder<?, ?> b) {
        super(b);
        this.text = b.text;
        if (text == null) {
            throw new java.lang.NullPointerException("text is marked non-null but is null");
        }
    }

    @java.lang.SuppressWarnings("all")
    public static TextCell.TextCellBuilder<?, ?> builder() {
        return new TextCell.TextCellBuilderImpl();
    }

    @java.lang.SuppressWarnings("all")
    public TextCell.TextCellBuilder<?, ?> toBuilder() {
        return new TextCell.TextCellBuilderImpl().$fillValuesFrom(this);
    }

    @NonNull
    @java.lang.SuppressWarnings("all")
    public List<Text> getText() {
        return this.text;
    }
}

