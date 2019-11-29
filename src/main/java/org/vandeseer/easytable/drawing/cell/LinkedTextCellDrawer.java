package org.vandeseer.easytable.drawing.cell;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.PositionedStyledText;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.cell.LinkedTextCell;
import org.vandeseer.easytable.structure.cell.LinkedTextCell.LinkedText.Link;
import org.vandeseer.easytable.util.PdfUtil;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Can create links in cells (even multiple links per cell).
 */
public class LinkedTextCellDrawer extends TextCellDrawer<LinkedTextCell> {

    // Please note that this whole implementation is quite hacky due to
    // the fact that links have to be calculated in terms of an area that
    // is set on the document. There is no notion of "markup" in PDF.

    private int linkedTextStringIndex = 0;

    private LinkedList<Link> links = new LinkedList<>();

    private final List<String> lines;
    private int lineCounter = 0;

    public LinkedTextCellDrawer(LinkedTextCell cell) {
        this.cell = cell;
        links.addAll(((LinkedTextCell) this.cell).getLinkedText().links);
        lines = calculateAndGetLines(cell.getFont(), cell.getFontSize(), cell.getWidthOfText());
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

            final String stringBeforeLink = lineString.substring(0, currentLink.getStart() - linkedTextStringIndex);
            float offset = PdfUtil.getStringWidth(
                    stringBeforeLink,
                    cell.getFont(), cell.getFontSize()
            );

            if (cell.isHorizontallyAligned(HorizontalAlignment.JUSTIFY) && TextCellDrawer.isNotLastLine(lines, lineCounter)) {
                offset += (stringBeforeLink.length() * calculateCharSpacingFor(positionedStyledText.getText()));
            }

            final PDRectangle linkRectangle = new PDRectangle();
            linkRectangle.setLowerLeftX(x + offset);
            linkRectangle.setLowerLeftY(y - 0.15f * cell.getFontSize());

            float stringWidth = PdfUtil.getStringWidth(stringBeforeLink + currentLink.getText(), cell.getFont(), cell.getFontSize());
            float linkWidth = PdfUtil.getStringWidth(currentLink.getText(), cell.getFont(), cell.getFontSize());

            if (stringWidth > (cell.getWidth() - cell.getHorizontalPadding())) {
                isMultilineLink = true;

                float shortenedLinkLength = PdfUtil.getStringWidth(lineString.substring(stringBeforeLink.length()), cell.getFont(), cell.getFontSize());
                linkRectangle.setUpperRightX(x + offset + shortenedLinkLength);
                linkRectangle.setUpperRightY(y + PdfUtil.getFontHeight(cell.getFont(), cell.getFontSize()));
            } else {
                if (cell.isHorizontallyAligned(HorizontalAlignment.JUSTIFY) && TextCellDrawer.isNotLastLine(lines, lineCounter)) {
                    linkWidth += (currentLink.getText().length() - 1) * calculateCharSpacingFor(positionedStyledText.getText());
                }
                linkRectangle.setUpperRightX(x + offset + linkWidth);
                linkRectangle.setUpperRightY(y + PdfUtil.getFontHeight(cell.getFont(), cell.getFontSize()));
            }

            pdfLink.setRectangle(linkRectangle);
            pdfLink.constructAppearances();

            drawingContext.getPage().getAnnotations().add(pdfLink);
        }

        lineCounter++;
        linkedTextStringIndex += lineString.length() + 1;

        if (isMultilineLink) {
            int newStart = linkedTextStringIndex;
            links.addFirst(
                    new Link(
                            newStart,
                            currentLink.getEnd(),
                            cell.getText().substring(newStart, currentLink.getEnd()),
                            currentLink.getUrl()
                    )
            );
        }
    }

    private PDAnnotationLink createAndGetPdAnnotationLinkFrom(Link currentLink) {
        PDBorderStyleDictionary underline = new PDBorderStyleDictionary();
        underline.setStyle(PDBorderStyleDictionary.STYLE_UNDERLINE);
        PDAnnotationLink pdfLink = new PDAnnotationLink();
        pdfLink.setBorderStyle(underline);

        // For coloring one could use something like the following, but unfortunately
        // that only colors the border, not the text. For coloring the text a lot of this library
        // would need to be changed though ...
        //
        // pdfLink.setColor(new PDColor(new float[] { 0.6f, 1f, 0.6f }, PDDeviceRGB.INSTANCE));

        // add an action
        PDActionURI action = new PDActionURI();
        action.setURI(currentLink.getUrl().toString());
        pdfLink.setAction(action);
        return pdfLink;
    }

}
