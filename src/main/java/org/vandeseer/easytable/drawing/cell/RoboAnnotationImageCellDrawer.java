package org.vandeseer.easytable.drawing.cell;

import java.awt.Color;
import java.awt.geom.Point2D;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.vandeseer.easytable.drawing.DrawingContext;
import org.vandeseer.easytable.drawing.DrawingUtil;
import org.vandeseer.easytable.drawing.PositionedStyledText;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.cell.AnnotationComment;
import org.vandeseer.easytable.structure.cell.RoboAnnotationImageCell;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import rst.pdfbox.layout.shape.RoundRect;
import rst.pdfbox.layout.shape.Shape;
import rst.pdfbox.layout.shape.Stroke;
import rst.pdfbox.layout.text.Position;

@NoArgsConstructor
public class RoboAnnotationImageCellDrawer extends AbstractCellDrawer<RoboAnnotationImageCell> {

	public static final float PDF_REVIEW_COMMENT_CIRCLE_RADIUS = 4f;
	public static final float PDF_REVIEW_PIXEL_TO_USER_POINT_CONVERSION_RATE = 0.75f;
	// Markup type
	public static final int MARKUP_TYPE_PIN = 1;
	public static final int MARKUP_TYPE_AREA_SELECTION = 2;
	public static final int MARKUP_TYPE_HIGHLIGHTED_AREA_SELECTION = 3;
	public static final int MARKUP_TYPE_STAMP = 4;

	public RoboAnnotationImageCellDrawer(RoboAnnotationImageCell cell) {
		this.cell = cell;
	}

	@Override
	@SneakyThrows
	public void drawContent(DrawingContext drawingContext) {
		final PDPageContentStream contentStream = drawingContext.getContentStream();

		final float moveX = drawingContext.getStartingPoint().x;

		final Point2D.Float size = cell.getFitSize();
		final Point2D.Float drawAt = new Point2D.Float();

		// Handle horizontal alignment by adjusting the xOffset
		float xOffset = moveX + cell.getPaddingLeft();
		if (cell.getSettings().getHorizontalAlignment() == HorizontalAlignment.RIGHT) {
			xOffset = moveX + (cell.getWidth() - (size.x + cell.getPaddingRight()));

		} else if (cell.getSettings().getHorizontalAlignment() == HorizontalAlignment.CENTER) {
			final float diff = (cell.getWidth() - size.x) / 2;
			xOffset = moveX + diff;

		}

		drawAt.x = xOffset;
		drawAt.y = drawingContext.getStartingPoint().y + getAdaptionForVerticalAlignment() - size.y;

		contentStream.drawImage(cell.getImage(), drawAt.x, drawAt.y, size.x, size.y);

		if (cell.getImageFooterText() != null && cell.getImageFooterText().length() > 0) {
			String imageFooterText = "Page " + cell.getImageFooterText();
			float textStartX = drawAt.x + (cell.getWidth() / 2);
			float startY = drawAt.y - (cell.getPaddingBottom() + cell.getPaddingTop() + 5f);

			PDFont imageFooterTextFont = cell.getImageFooterTextFont() != null ? cell.getImageFooterTextFont()
					: PDType1Font.HELVETICA_BOLD;
			int fontSize = cell.getImageFooterTextFontSize() > 0 ? cell.getImageFooterTextFontSize() : 9;
			Color imageFooterTextColor = cell.getImageFooterTextColor() != null ? cell.getImageFooterTextColor()
					: Color.BLACK;
			DrawingUtil.drawText(contentStream,
					PositionedStyledText.builder().x(textStartX).y(startY).font(imageFooterTextFont).fontSize(fontSize)
							.color(imageFooterTextColor).text(imageFooterText).build());
		}

		if (cell.getAnnotationsComments() != null && cell.getAnnotationsComments().isEmpty() == Boolean.FALSE) {

			for (AnnotationComment annotationComment : cell.getAnnotationsComments()) {
				float xPosition;
				float yPosition;
				try {
					xPosition = (float) annotationComment.getMarkUpJson().getDouble("xPosition");
					yPosition = (float) annotationComment.getMarkUpJson().getDouble("yPosition");
				} catch (Exception e) {// x or y position not in markup json then assign default to zero position for
										// draw annotation image.
					xPosition = 0.0f;
					yPosition = 0.0f;
				}
				float imageScaleWidth = size.x;
				float imageScaleHeight = size.y;
				float[] cordinates = getRelevantPositionCordinates(xPosition, yPosition, imageScaleWidth,
						imageScaleHeight, cell.getOriginalWidth(), cell.getOriginalHeight());
				if (annotationComment.getMarkupTypeId() != null
						&& (annotationComment.getMarkupTypeId() == MARKUP_TYPE_AREA_SELECTION
								|| annotationComment.getMarkupTypeId() == MARKUP_TYPE_HIGHLIGHTED_AREA_SELECTION)) {

					float width = annotationComment.getMarkUpJson().isNull("width") ? 0
							: (float) annotationComment.getMarkUpJson().getDouble("width");

					float height = annotationComment.getMarkUpJson().isNull("height") ? 0
							: (float) annotationComment.getMarkUpJson().getDouble("height");

					float[] heightWidth = getRelevantPositionCordinates(width, height, imageScaleWidth,
							imageScaleHeight, cell.getOriginalWidth(), cell.getOriginalHeight());

					float cx = drawAt.x + cordinates[0];
					float cy = drawAt.y + imageScaleHeight - cordinates[1];

					if (annotationComment.getMarkupTypeId() == MARKUP_TYPE_HIGHLIGHTED_AREA_SELECTION) {
						float xStart = drawAt.x + cordinates[0];
						float yStart = drawAt.y + imageScaleHeight - heightWidth[1] - cordinates[1];
						contentStream.drawImage(annotationComment.getHighlightedAreaImage(), xStart, yStart,
								heightWidth[0], heightWidth[1]);

						float scale = 1.0f;
						float linewidth = heightWidth[0] * scale; // width of image
						float lineheight = heightWidth[1] * scale; // height of image

						contentStream.setStrokingColor(Color.YELLOW);
						// start to draw border
						contentStream.drawLine(xStart, yStart, xStart + linewidth, yStart); // bottom
						contentStream.drawLine(xStart, yStart + lineheight, xStart + linewidth, yStart + lineheight); // top
						contentStream.drawLine(xStart, yStart, xStart, yStart + lineheight); // left
						contentStream.drawLine(xStart + linewidth, yStart, xStart + linewidth, yStart + lineheight); // right
						contentStream.setStrokingColor(Color.BLACK);

					} else {
						Shape shape = new RoundRect(1);
						shape.draw(null, contentStream, new Position(cx, cy), heightWidth[0], heightWidth[1],
								Color.YELLOW, new Stroke(1), null);
					}

					float r = 6f;
					DrawingUtil.drawCircle(contentStream, cx, cy, r, Color.RED);
					String imageText = annotationComment.getCommentNumber() != null
							? annotationComment.getCommentNumber()
							: "";
					if (imageText != null && imageText.length() > 0) {
						imageText = imageText.length() == 1 ? " " + imageText : imageText;
						float textStartX = cx - 5.5f;
						float textStartY = cy - 3.5f;

						DrawingUtil.drawText(contentStream,
								PositionedStyledText.builder().x(textStartX).y(textStartY).font(PDType1Font.HELVETICA_BOLD)
										.fontSize(10).color(Color.WHITE).text(imageText).build());
					}
				} else if (annotationComment.getMarkupTypeId() != null
						&& annotationComment.getMarkupTypeId() == MARKUP_TYPE_STAMP) {
					String bkgColorStr = annotationComment.getStampBackgroundColor();
					String stampLabelText = annotationComment.getStampLabelText();
					String labelColorStr = annotationComment.getStampLabelColor();
					
					float cx = drawAt.x + cordinates[0];
					float cy = drawAt.y + imageScaleHeight - 2f - cordinates[1];
					float r = 6f;

					float width;
					
					if(stampLabelText.length() <= 6) {
						width = stampLabelText.length() * 6f;
					}else {
						width = stampLabelText.length() * 4.5f;
					}
							
					
					Shape shape = new RoundRect(1);
					
					contentStream.setStrokingColor(new Color(Integer.valueOf(bkgColorStr.substring(1, 3), 16),
							Integer.valueOf(bkgColorStr.substring(3, 5), 16),
							Integer.valueOf(bkgColorStr.substring(5, 7), 16)));
					
					shape.draw(null, contentStream, new Position(cx, cy), width, 8,
							new Color(Integer.valueOf(bkgColorStr.substring(1, 3), 16),
									Integer.valueOf(bkgColorStr.substring(3, 5), 16),
									Integer.valueOf(bkgColorStr.substring(5, 7), 16)),
							new Stroke(8), null);
					
					new Stroke(1).applyTo(contentStream);
					contentStream.setStrokingColor(Color.BLACK);
					
					
					
					if (stampLabelText != null && stampLabelText.length() > 0) {
						float textStartX = cx + 4.8f;
						float textStartY = cy - 5.8f;

						DrawingUtil.drawText(contentStream,
								PositionedStyledText.builder().x(textStartX).y(textStartY).font(PDType1Font.HELVETICA_BOLD)
										.fontSize(7)
										.color(new Color(Integer.valueOf(labelColorStr.substring(1, 3), 16),
												Integer.valueOf(labelColorStr.substring(3, 5), 16),
												Integer.valueOf(labelColorStr.substring(5, 7), 16)))
										.text(stampLabelText).build());
					}

					String commentText = annotationComment.getCommentNumber() != null
							? annotationComment.getCommentNumber()
							: "";
					
					float circleX = cx - 1.8f;
					float circleY = cy + 3.5f;			
					DrawingUtil.drawCircle(contentStream, circleX, circleY, r, Color.RED);
					if (commentText != null && commentText.length() > 0) {
						commentText = commentText.length() == 1 ? " " + commentText : commentText;
						float textStartX = circleX - 5.5f;
						float textStartY = circleY - 3.5f;

						DrawingUtil.drawText(contentStream,
								PositionedStyledText.builder().x(textStartX).y(textStartY).font(PDType1Font.HELVETICA_BOLD)
										.fontSize(10).color(Color.WHITE).text(commentText).build());
					}

				} else {
					String imageText = annotationComment.getCommentNumber() != null
							? annotationComment.getCommentNumber()
							: "";
					if (imageText != null && imageText.length() > 0) {
						imageText = imageText.length() == 1 ? " " + imageText : imageText;

						float cx = drawAt.x + cordinates[0] + 5f;
						float cy = drawAt.y + imageScaleHeight - cordinates[1] - 2.5f;
						float r = 6f;
						DrawingUtil.drawCircle(contentStream, cx, cy, r, Color.RED);

						float textStartX = cx - 5.5f;
						float textStartY = cy - 3.5f;
						DrawingUtil.drawText(contentStream,
								PositionedStyledText.builder().x(textStartX).y(textStartY).font(PDType1Font.HELVETICA_BOLD)
										.fontSize(10).color(Color.WHITE).text(imageText).build());

					}

				}

			}
		}
	}

	@Override
	protected float calculateInnerHeight() {
		return (float) cell.getFitSize().getY();
	}

	private float[] getRelevantPositionCordinates(float xPosition, float yPosition, float imageWidth, float imageHeight,
			float originalImageWidth, float originalImageHieght) {
		// convert pixels to point
		xPosition = xPosition * PDF_REVIEW_PIXEL_TO_USER_POINT_CONVERSION_RATE;
		yPosition = yPosition * PDF_REVIEW_PIXEL_TO_USER_POINT_CONVERSION_RATE;
		originalImageWidth = originalImageWidth * PDF_REVIEW_PIXEL_TO_USER_POINT_CONVERSION_RATE;
		originalImageHieght = originalImageHieght * PDF_REVIEW_PIXEL_TO_USER_POINT_CONVERSION_RATE;

		float calculatedXPosition = (imageWidth * xPosition) / originalImageWidth;
		float calculatedYPosition = (imageHeight * yPosition) / originalImageHieght;
		return new float[] { calculatedXPosition, calculatedYPosition };
	}
}
