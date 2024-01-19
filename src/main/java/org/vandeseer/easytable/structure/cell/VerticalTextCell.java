package org.vandeseer.easytable.structure.cell;

import lombok.NonNull;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.cell.VerticalTextCellDrawer;
import org.vandeseer.easytable.structure.Text;

import java.util.List;

public class VerticalTextCell extends AbstractTextCell {
    @NonNull
    private List<Text> text;

    protected Drawer createDefaultDrawer() {
        return new VerticalTextCellDrawer(this);
    }


    public static abstract class AbstractTextCellBuilder<C extends VerticalTextCell, B extends AbstractTextCellBuilder<C, B>> extends AbstractCellBuilder<C, B> {
        private List<Text> text;

        public B coloredText(List<Text> text) {
            this.text = text;
            return self();
        }

        public B text(String text) {
            this.text = List.of(new Text(text, this.settings.getTextColor()));
            return self();
        }
    }


    @java.lang.SuppressWarnings("all")
    public static abstract class VerticalTextCellBuilder<C extends VerticalTextCell, B extends VerticalTextCell.VerticalTextCellBuilder<C, B>> extends AbstractTextCell.AbstractTextCellBuilder<C, B> {
        @java.lang.SuppressWarnings("all")
        private List<Text> text;

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        protected B $fillValuesFrom(final C instance) {
            super.$fillValuesFrom(instance);
            VerticalTextCell.VerticalTextCellBuilder.$fillValuesFromInstanceIntoBuilder(instance, this);
            return self();
        }

        @java.lang.SuppressWarnings("all")
        private static void $fillValuesFromInstanceIntoBuilder(final VerticalTextCell instance, final VerticalTextCell.VerticalTextCellBuilder<?, ?> b) {
            b.coloredText(instance.text);
        }

        /**
         * @return {@code this}.
         */
        @java.lang.SuppressWarnings("all")
        public B text(@NonNull final String text) {
            if (text == null) {
                throw new java.lang.NullPointerException("text is marked non-null but is null");
            }
            this.text = List.of(new Text(text, this.settings.getTextColor()));
            return self();
        }

        public B coloredText(@NonNull final List<Text> text) {
            if (text == null) {
                throw new java.lang.NullPointerException("text is marked non-null but is null");
            }
            this.text = text;
            return self();
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
            return "VerticalTextCell.VerticalTextCellBuilder(super=" + super.toString() + ", text=" + this.text + ")";
        }
    }


    @java.lang.SuppressWarnings("all")
    private static final class VerticalTextCellBuilderImpl extends VerticalTextCell.VerticalTextCellBuilder<VerticalTextCell, VerticalTextCell.VerticalTextCellBuilderImpl> {
        @java.lang.SuppressWarnings("all")
        private VerticalTextCellBuilderImpl() {
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        protected VerticalTextCell.VerticalTextCellBuilderImpl self() {
            return this;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public VerticalTextCell build() {
            return new VerticalTextCell(this);
        }
    }

    @java.lang.SuppressWarnings("all")
    protected VerticalTextCell(final VerticalTextCell.VerticalTextCellBuilder<?, ?> b) {
        super(b);
        this.text = b.text;
        if (text == null) {
            throw new java.lang.NullPointerException("text is marked non-null but is null");
        }
    }

    @java.lang.SuppressWarnings("all")
    public static VerticalTextCell.VerticalTextCellBuilder<?, ?> builder() {
        return new VerticalTextCell.VerticalTextCellBuilderImpl();
    }

    @java.lang.SuppressWarnings("all")
    public VerticalTextCell.VerticalTextCellBuilder<?, ?> toBuilder() {
        return new VerticalTextCell.VerticalTextCellBuilderImpl().$fillValuesFrom(this);
    }

    @NonNull
    @java.lang.SuppressWarnings("all")
    public List<Text> getText() {
        return this.text;
    }
}

