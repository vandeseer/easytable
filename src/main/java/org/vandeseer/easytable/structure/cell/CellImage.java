package org.vandeseer.easytable.structure.cell;


import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.geom.Point2D;

@Getter
@SuperBuilder
public class CellImage extends CellBaseData {

    @NonNull
    private PDImageXObject image;

    private float maxHeight;

    @Builder.Default
    private final float scale = 1.0f;


    @Override
    public float getHeight() {
        return getFitSize().y + getVerticalPadding();
    }


    public Point2D.Float getFitSize() {
        final Point2D.Float sizes = new Point2D.Float();
        float scaledWidth = image.getWidth() * getScale();
        float scaledHeight = image.getHeight() * getScale();
        final float resultingWidth = getColumn().getWidth() - getHorizontalPadding();

        // maybe reduce the image to fit in column
        if (scaledWidth > resultingWidth) {
            scaledHeight = (resultingWidth / scaledWidth) * scaledHeight;
            scaledWidth = resultingWidth;
        }

        if (maxHeight > 0.0f && scaledHeight > maxHeight) {
            scaledWidth = (maxHeight / scaledHeight) * scaledWidth;
            scaledHeight = maxHeight;
        }

        sizes.x = scaledWidth;
        sizes.y = scaledHeight;

        return sizes;

    }
}
