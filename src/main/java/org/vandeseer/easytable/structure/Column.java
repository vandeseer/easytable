package org.vandeseer.easytable.structure;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.Settings;
import org.vandeseer.easytable.settings.VerticalAlignment;

import java.awt.*;

@Getter
@Setter(AccessLevel.PACKAGE)
public class Column {

    private Table table;

    private Column next;

    private float width;

    @Setter(AccessLevel.NONE)
    private Settings settings;

    Column(final float width) {
        if (width < 0) {
            throw new IllegalArgumentException("Column width must be non-negative");
        }
        this.width = width;
    }

    private Column(final float width, final Settings settings) {
        this(width);
        this.settings = settings;
    }

    public static ColumnBuilder builder() {
        return new ColumnBuilder();
    }

    boolean hasNext() {
        return next != null;
    }


    public static class ColumnBuilder {
        private float width;
        private Settings settings = Settings.builder().build();

        ColumnBuilder() {
        }

        public ColumnBuilder width(float width) {
            this.width = width;
            return this;
        }

        private ColumnBuilder settings(Settings settings) {
            this.settings = settings;
            return this;
        }

        public ColumnBuilder font(final PDFont font) {
            settings.setFont(font);
            return this;
        }

        public ColumnBuilder fontSize(final Integer fontSize) {
            settings.setFontSize(fontSize);
            return this;
        }

        public ColumnBuilder textColor(final Color textColor) {
            settings.setTextColor(textColor);
            return this;
        }

        public ColumnBuilder backgroundColor(final Color backgroundColor) {
            settings.setBackgroundColor(backgroundColor);
            return this;
        }

        public ColumnBuilder padding(final float padding) {
            settings.setPaddingTop(padding);
            settings.setPaddingBottom(padding);
            settings.setPaddingLeft(padding);
            settings.setPaddingRight(padding);
            return this;
        }

//        public ColumnBuilder borderWidth(final float borderWidth) {
////            settings.setBorderWidthTop(borderWidth);
////            settings.setBorderWidthBottom(borderWidth);
//            settings.setBorderWidthLeft(borderWidth);
//            settings.setBorderWidthRight(borderWidth);
//            return this;
//        }

//        public ColumnBuilder borderStyle(final BorderStyleInterface borderStyle) {
////            settings.setBorderStyleTop(borderStyle);
////            settings.setBorderStyleBottom(borderStyle);
//            settings.setBorderStyleLeft(borderStyle);
//            settings.setBorderStyleRight(borderStyle);
//            return this;
//        }

//        public ColumnBuilder borderColor(final Color borderColor) {
////            settings.setBorderColorBottom(borderColor);
////            settings.setBorderColorTop(borderColor);
//            settings.setBorderColorLeft(borderColor);
//            settings.setBorderColorRight(borderColor);
//            return this;
//        }

        public ColumnBuilder horizontalAlignment(HorizontalAlignment alignment) {
            settings.setHorizontalAlignment(alignment);
            return this;
        }

        public ColumnBuilder verticalAlignment(VerticalAlignment alignment) {
            settings.setVerticalAlignment(alignment);
            return this;
        }

        public ColumnBuilder wordBreak(Boolean wordBreak) {
            settings.setWordBreak(wordBreak);
            return this;
        }

        public Column build() {
            return new Column(width, settings);
        }
    }

}
