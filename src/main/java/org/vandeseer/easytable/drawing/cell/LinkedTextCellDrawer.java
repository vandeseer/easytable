package org.vandeseer.easytable.drawing.cell;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.PositionedStyledText;
import org.vandeseer.easytable.structure.cell.LinkedTextCell;
import org.vandeseer.easytable.structure.cell.LinkedTextCell.LinkedText.Link;
import org.vandeseer.easytable.util.PdfUtil;

import java.io.IOException;
import java.util.LinkedList;

public class LinkedTextCellDrawer extends TextCellDrawer<LinkedTextCell> {

    private int linkedTextStringIndex = 0;

    private LinkedList<Link> links = new LinkedList<>();

    public LinkedTextCellDrawer(LinkedTextCell cell) {
        this.cell = cell;
        links.addAll(((LinkedTextCell) this.cell).getLinkedText().links);
    }

    @Override
    protected void drawText(DrawingContext drawingContext, PositionedStyledText positionedStyledText) throws IOException {

        super.drawText(drawingContext, positionedStyledText);

        final float x = positionedStyledText.getX();
        final float y = positionedStyledText.getY();

        final String lineString = positionedStyledText.getText();

        boolean isMultilineLink = false;
        Link currentLink = null;

        while (!links.isEmpty() && (links.getFirst().getStart() - linkedTextStringIndex) < lineString.length() - 1) {
            currentLink = links.removeFirst();
            PDAnnotationLink pdfLink = createAndGetPdAnnotationLinkFrom(currentLink);

            float offset = PdfUtil.getStringWidth(
                    lineString.substring(0, currentLink.getStart() - linkedTextStringIndex),
                    cell.getFont(), cell.getFontSize()
            );

            PDRectangle linkRectangle = new PDRectangle();
            linkRectangle.setLowerLeftX(x + offset);
            linkRectangle.setLowerLeftY(y);

            float stringWidth = PdfUtil.getStringWidth(currentLink.getText(), cell.getFont(), cell.getFontSize());
            if (stringWidth > (cell.getWidth() - cell.getHorizontalPadding())) {
                linkRectangle.setUpperRightX(x + (cell.getWidth() - cell.getHorizontalPadding()));
                linkRectangle.setUpperRightY(y + PdfUtil.getFontHeight(cell.getFont(), cell.getFontSize()));

                isMultilineLink = true;
            } else {
                linkRectangle.setUpperRightX(x + offset + stringWidth);
                linkRectangle.setUpperRightY(y + PdfUtil.getFontHeight(cell.getFont(), cell.getFontSize()));
            }

            pdfLink.setRectangle(linkRectangle);
            pdfLink.constructAppearances();

            drawingContext.getPage().getAnnotations().add(pdfLink);
        }

        if (isMultilineLink) {
            int newStart = linkedTextStringIndex + positionedStyledText.getText().length() + 2; // ?!

            links.addFirst(
                    new Link(
                            newStart,
                            currentLink.getEnd(),
                            currentLink.getText().substring(positionedStyledText.getText().length() -2 /* ?!! */, currentLink.getText().length() - 1),
                            currentLink.getUrl()
                    )
            );

            linkedTextStringIndex++; // We have an extra sign for splitting ("-")!
        }

        linkedTextStringIndex += lineString.length() + "\n".length();
    }

    private PDAnnotationLink createAndGetPdAnnotationLinkFrom(Link currentLink) {
        PDBorderStyleDictionary underline = new PDBorderStyleDictionary();
        underline.setStyle(PDBorderStyleDictionary.STYLE_UNDERLINE);
        PDAnnotationLink pdfLink = new PDAnnotationLink();
        pdfLink.setBorderStyle(underline);
        //pdfLink.setColor(new PDColor(new float[] { 0.6f, 1f, 0.6f }, PDDeviceRGB.INSTANCE));

        // add an action
        PDActionURI action = new PDActionURI();
        action.setURI(currentLink.getUrl().toString());
        pdfLink.setAction(action);
        return pdfLink;
    }

}
