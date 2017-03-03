package org.vandeseer.pdfbox.easytable;

public class Column {

    private final float width;

    public Column(final float width) {
        if (width < 0) {
            throw new IllegalArgumentException("Column width must be non-negative");
        }
        this.width = width;
    }

    public float getWidth() {
        return width;
    }

}
