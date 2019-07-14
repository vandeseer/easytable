package org.vandeseer.easytable.structure.cell;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.vandeseer.easytable.structure.TableNotYetBuiltException;

import java.io.IOException;

public class ImageCellTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getHeight_shouldThrowExceptionIfTableNotYetBuilt() throws IOException {
        final byte[] bytes = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("pic1.jpg"));
        final PDImageXObject image = PDImageXObject.createFromByteArray(new PDDocument(), bytes, "test1");

        AbstractCell cell = ImageCell.builder().image(image).build();

        exception.expect(TableNotYetBuiltException.class);
        cell.getHeight();
    }

}
