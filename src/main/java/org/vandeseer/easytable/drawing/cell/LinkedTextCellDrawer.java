package org.vandeseer.easytable.drawing.cell;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.PositionedStyledText;
import org.vandeseer.easytable.structure.cell.LinkedTextCell;
import org.vandeseer.easytable.util.PdfUtil;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

public class LinkedTextCellDrawer extends TextCellDrawer<LinkedTextCell> {

    private int linkedTextStringIndex = 0;

    private LinkedList<Map.Entry<Integer, LinkedTextCell.LinkedText.Link>> links = new LinkedList<>();

    public LinkedTextCellDrawer(LinkedTextCell cell) {
        this.cell = cell;
        links.addAll(((LinkedTextCell) this.cell).getLinkedText().links.entrySet());
    }

    @Override
    protected void drawText(DrawingContext drawingContext, PositionedStyledText positionedStyledText) throws IOException {

        super.drawText(drawingContext, positionedStyledText);

        final float x = positionedStyledText.getX();
        final float y = positionedStyledText.getY();

        final String lineString = positionedStyledText.getText();
        System.out.println("'"+lineString+"'");

        while (!links.isEmpty() && (links.getFirst().getKey() - linkedTextStringIndex) < lineString.length() - 1) {
            // We have a link here ...
            // TODO also check linebreaks!
            LinkedTextCell.LinkedText.Link currentLink = links.removeFirst().getValue();

            // handle the annotations ...
            PDBorderStyleDictionary underline = new PDBorderStyleDictionary();
            underline.setStyle(PDBorderStyleDictionary.STYLE_UNDERLINE);
            PDAnnotationLink pdfLink = new PDAnnotationLink();
            pdfLink.setBorderStyle(underline);
            //pdfLink.setColor(new PDColor(new float[] { 0.6f, 1f, 0.6f }, PDDeviceRGB.INSTANCE));

            // add an action
            PDActionURI action = new PDActionURI();
            action.setURI(currentLink.getUrl().toString());
            pdfLink.setAction(action);

            PDRectangle position = new PDRectangle();

            // TODO linebreaks
            float offset = PdfUtil.getStringWidth(lineString.substring(0, currentLink.getStart() - linkedTextStringIndex), cell.getFont(), cell.getFontSize());

            position.setLowerLeftX(x + offset);
            position.setLowerLeftY(y);

            float stringWidth = PdfUtil.getStringWidth(currentLink.getText(), cell.getFont(), cell.getFontSize());
            position.setUpperRightX(x + offset + stringWidth);
            position.setUpperRightY(y + PdfUtil.getFontHeight(cell.getFont(), cell.getFontSize()));

            pdfLink.setRectangle(position);
            pdfLink.constructAppearances();
            drawingContext.getPage().getAnnotations().add(pdfLink);
        }

        // new lines are stripped, hence "- sizeof(\n)" which is 2
        linkedTextStringIndex += (lineString.length() - 1) + 2;
    }
}
