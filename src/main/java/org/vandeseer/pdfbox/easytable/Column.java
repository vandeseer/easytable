package org.vandeseer.pdfbox.easytable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class Column {

    private Table table;

    private Column next;

    private final float width;

    public Column(final float width) {
        if (width < 0) {
            throw new IllegalArgumentException("Column width must be non-negative");
        }
        this.width = width;
    }

    boolean hasNext() {
        return next != null;
    }

}
