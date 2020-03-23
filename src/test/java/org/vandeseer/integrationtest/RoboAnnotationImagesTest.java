package org.vandeseer.integrationtest;

import static org.vandeseer.easytable.settings.HorizontalAlignment.RIGHT;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.json.JSONObject;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.RepeatedHeaderTableDrawer;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.AnnotationComment;
import org.vandeseer.easytable.structure.cell.AnnotationCommentToTableData;
import org.vandeseer.easytable.structure.cell.ImageCell;
import org.vandeseer.easytable.structure.cell.ImageCell.ImageCellBuilder;
import org.vandeseer.easytable.structure.cell.ReviewComment;
import org.vandeseer.easytable.structure.cell.RoboAnnotationImageCell;
import org.vandeseer.easytable.structure.cell.RoboImageCell;
import org.vandeseer.easytable.structure.cell.TextCell;
import org.vandeseer.easytable.structure.cell.TextCell.TextCellBuilder;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell;
import org.vandeseer.easytable.structure.cell.paragraph.ParagraphCell.Paragraph;
import org.vandeseer.easytable.structure.cell.paragraph.StyledText;

public class RoboAnnotationImagesTest {
	public static final float BORDER_WIDTH_0_7 = 0.7f;
	public static final int PADDING_5 = 5;
	public static final int FONT_SIZE_10 = 10;
	public static final int FONT_SIZE_11 = 11;
	public static final float FONT_SIZE_11_F = 11f;
	public static final int FONT_SIZE_12 = 12;
	public static final int FONT_SIZE_13 = 13;
	public static final int FONT_SIZE_14 = 14;
	public static final float FONT_SIZE_14_F = 14f;
	public static final int FONT_SIZE_15 = 15;

	private static final String NUMBER_OF_COMMENTS_TITLE = "Number of comments: ";
	private static final String REVIEWER_STATUS_TITLE = "Reviewer Status: ";
	private static final String CREATED_TITLE = "Created: ";
	private static final String VERSION_TITLE = "Version: ";

	@Test
	public void testRoboAnnotationImage() throws Exception {

		testRoboHeadAnnotationPage();
		//testRoboHeadImageDetails();
	}
	
	
	private void testRoboHeadImageDetails() throws IOException {
		final Table.TableBuilder tableBuilder = Table.builder().addColumnsOfWidth(50, 50);
		final byte[] bytes1 = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("pic1.jpg"));
		final PDImageXObject image1 = PDImageXObject.createFromByteArray(new PDDocument(), bytes1, "test1");

		final byte[] bytes3 = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("user_no_image.png"));
		final PDImageXObject image3 = PDImageXObject.createFromByteArray(new PDDocument(), bytes3, "noImage");

		tableBuilder.addRow(Row.builder().add(TextCell.builder().text("first").build())
				.add(TextCell.builder().text("second").horizontalAlignment(RIGHT).build()).build());

		tableBuilder
				.addRow(Row.builder().add(RoboImageCell.builder().image(image3).imageText("3").borderWidth(0).build())
						.add(TextCell.builder().text("third").build()).build());

		tableBuilder
				.addRow(Row.builder().add(RoboImageCell.builder().image(image3).imageText("40").borderWidth(0).build())
						.add(TextCell.builder().text("four").build()).build());

		final Table.TableBuilder tableBuilder2 = Table.builder().addColumnsOfWidth(500);

		final byte[] bytes4 = IOUtils
				.toByteArray(getClass().getClassLoader().getResourceAsStream("highlight_background.png"));
		final PDImageXObject highlightedAreaImage = PDImageXObject.createFromByteArray(new PDDocument(), bytes4,
				"highback");

		List<AnnotationComment> annotationsComments = new ArrayList<>();
		AnnotationComment annotationComment1 = new AnnotationComment();
		annotationComment1.setMarkupTypeId(1);
		annotationComment1
				.setMarkUpJson(new JSONObject("{\"xPosition\":278.58231707317077,\"yPosition\":1036.5853658536587}"));
		annotationComment1.setCommentNumber("7");

		AnnotationComment annotationComment2 = new AnnotationComment();
		annotationComment2.setMarkupTypeId(3);
		annotationComment2.setHighlightedAreaImage(highlightedAreaImage);
		annotationComment2.setMarkUpJson(new JSONObject(
				"{\"xPosition\":1249.578101503337,\"yPosition\":864.9923113957504,\"width\":312.8834355828221,\"height\":187.73008124228636}"));
		annotationComment2.setCommentNumber("8");

		AnnotationComment annotationComment3 = new AnnotationComment();
		annotationComment3.setMarkupTypeId(2);
		annotationComment3.setHighlightedAreaImage(highlightedAreaImage);
		annotationComment3.setMarkUpJson(new JSONObject(
				"{\"xPosition\":1819.8551433842358,\"yPosition\":810.4802224694229,\"width\":168.4451219512195,\"height\":204.72555044220715}"));
		annotationComment3.setCommentNumber("14");

		AnnotationComment annotationComment4 = new AnnotationComment();
		annotationComment4.setMarkupTypeId(4);
		annotationComment4.setHighlightedAreaImage(highlightedAreaImage);
		annotationComment4
				.setMarkUpJson(new JSONObject("{\"xPosition\":1648.8185975609756,\"yPosition\":197.59908536585365}"));
		annotationComment4.setCommentNumber("9");
		annotationComment4.setStampLabelText("Off Brand");
		annotationComment4.setStampLabelColor("#000000");
		annotationComment4.setStampBackgroundColor("#FFFF00");

		AnnotationComment annotationComment5 = new AnnotationComment();
		annotationComment5.setMarkupTypeId(4);
		annotationComment5.setHighlightedAreaImage(highlightedAreaImage);
		annotationComment5
				.setMarkUpJson(new JSONObject("{\"xPosition\":813.0716463414634,\"yPosition\":1201.7911585365855}"));
		annotationComment5.setCommentNumber("10");
		annotationComment5.setStampLabelText("Remove");
		annotationComment5.setStampLabelColor("#000000");
		annotationComment5.setStampBackgroundColor("#FF0000");

		AnnotationComment annotationComment6 = new AnnotationComment();
		annotationComment6.setMarkupTypeId(4);
		annotationComment6.setHighlightedAreaImage(highlightedAreaImage);
		annotationComment6
				.setMarkUpJson(new JSONObject("{\"xPosition\":955.0716463414634,\"yPosition\":1401.7911585365855}"));
		annotationComment6.setCommentNumber("11");
		annotationComment6.setStampLabelText("For Position Only");
		annotationComment6.setStampLabelColor("#000000");
		annotationComment6.setStampBackgroundColor("#FFCC99");

		AnnotationComment annotationComment7 = new AnnotationComment();
		annotationComment7.setMarkupTypeId(2);
		annotationComment7.setMarkUpJson(new JSONObject(
				"{\"xPosition\":2182.6602191459842,\"yPosition\":382.88875905478875,\"width\":103.65853658536585,\"height\":181.40238959614825}"));
		annotationComment7.setCommentNumber("2");

		annotationsComments.add(annotationComment1);
		annotationsComments.add(annotationComment2);
		annotationsComments.add(annotationComment3);
		annotationsComments.add(annotationComment4);
		annotationsComments.add(annotationComment5);
		annotationsComments.add(annotationComment6);
		annotationsComments.add(annotationComment7);

		tableBuilder2
				.addRow(Row.builder()
						.add(RoboAnnotationImageCell.builder().image(image1).imageFooterText("1").originalHeight(2170)
								.originalWidth(2270).annotationsComments(annotationsComments).borderWidth(0).build())
						.build());

		TestUtils.createAndSaveDocumentWithTables("roboannotationimages.pdf", tableBuilder.build(),
				tableBuilder2.build());
	}

	private void testRoboHeadAnnotationPage() throws IOException {
		Table.TableBuilder reviewDetailsTableBuilder = Table.builder().addColumnsOfWidth(50, 225, 400).borderWidth(1);

		ParagraphCell reviewVersionTitle = ParagraphCell.builder().colSpan(3).paddingBottom(3)
				.horizontalAlignment(HorizontalAlignment.LEFT)
				.paragraph(
						Paragraph.builder()
								.append(StyledText.builder().text("Vikram Testing Version Title")
										.font(PDType1Font.HELVETICA_BOLD).fontSize(FONT_SIZE_14_F).build())
								.build())
				.build();

		reviewDetailsTableBuilder.addRow(Row.builder().add(reviewVersionTitle).build());

		ParagraphCell reviewVersionNo = ParagraphCell.builder().colSpan(3).paddingBottom(3)
				.horizontalAlignment(HorizontalAlignment.LEFT)
				.paragraph(Paragraph.builder()
						.append(StyledText.builder().text(VERSION_TITLE).font(PDType1Font.HELVETICA_BOLD)
								.fontSize(FONT_SIZE_14_F).build())
						.append(StyledText.builder().text(String.valueOf(1)).font(PDType1Font.HELVETICA_BOLD)
								.fontSize(FONT_SIZE_14_F).build())
						.build())
				.build();

		reviewDetailsTableBuilder.addRow(Row.builder().add(reviewVersionNo).build());

		ParagraphCell reviewVersionFileName = ParagraphCell.builder().colSpan(3).paddingBottom(3)
				.horizontalAlignment(HorizontalAlignment.LEFT)
				.paragraph(Paragraph.builder()
						.append(StyledText.builder().text("Filename: ").font(PDType1Font.HELVETICA_BOLD)
								.fontSize(FONT_SIZE_11_F).build())
						.append(StyledText.builder().text("Vikram Test File").font(PDType1Font.HELVETICA)
								.fontSize(FONT_SIZE_11_F).build())
						.build())
				.build();

		reviewDetailsTableBuilder.addRow(Row.builder().add(reviewVersionFileName).build());

		ParagraphCell reviewVersionFileDesc = ParagraphCell.builder().colSpan(3).paddingBottom(3)
				.horizontalAlignment(HorizontalAlignment.LEFT)
				.paragraph(Paragraph.builder()
						.append(StyledText.builder().text("Description: ").font(PDType1Font.HELVETICA_BOLD)
								.fontSize(FONT_SIZE_11_F).build())
						.append(StyledText.builder().text("Vikram Version Description").font(PDType1Font.HELVETICA)
								.fontSize(FONT_SIZE_11_F).build())
						.build())
				.build();

		reviewDetailsTableBuilder.addRow(Row.builder().add(reviewVersionFileDesc).build());

		String createdOn = "20-02-2020";
		ParagraphCell reviewVersionCreated = ParagraphCell.builder().colSpan(3).paddingBottom(5)
				.horizontalAlignment(HorizontalAlignment.LEFT)
				.paragraph(Paragraph.builder()
						.append(StyledText.builder().text(CREATED_TITLE).font(PDType1Font.HELVETICA_BOLD)
								.fontSize(FONT_SIZE_11_F).build())
						.append(StyledText.builder().text(createdOn).font(PDType1Font.HELVETICA)
								.fontSize(FONT_SIZE_11_F).build())
						.build())
				.build();

		reviewDetailsTableBuilder.addRow(Row.builder().add(reviewVersionCreated).build());

		ParagraphCell reviewVersionReviewerStatus = ParagraphCell.builder().colSpan(3).paddingBottom(3)
				.horizontalAlignment(HorizontalAlignment.LEFT)
				.paragraph(Paragraph.builder()
						.append(StyledText.builder().text(REVIEWER_STATUS_TITLE).font(PDType1Font.HELVETICA_BOLD)
								.fontSize(FONT_SIZE_11_F).build())
						.append(StyledText.builder().text("Vikram Test Status String").font(PDType1Font.HELVETICA)
								.fontSize(FONT_SIZE_11_F).build())
						.build())
				.build();

		reviewDetailsTableBuilder.addRow(Row.builder().add(reviewVersionReviewerStatus).build());

		ParagraphCell reviewVersionNoOfComments = ParagraphCell.builder().colSpan(3).paddingBottom(3)
				.horizontalAlignment(HorizontalAlignment.LEFT)
				.paragraph(Paragraph.builder()
						.append(StyledText.builder().text(NUMBER_OF_COMMENTS_TITLE).font(PDType1Font.HELVETICA_BOLD)
								.fontSize(FONT_SIZE_11_F).build())
						.append(StyledText.builder().text(String.valueOf(0)).font(PDType1Font.HELVETICA)
								.fontSize(FONT_SIZE_11_F).build())
						.build())
				.build();

		reviewDetailsTableBuilder.addRow(Row.builder().add(reviewVersionNoOfComments).build());

		
		final byte[] annotationBytes = IOUtils
				.toByteArray(getClass().getClassLoader().getResourceAsStream("annotationCommentIcon.png"));
		final PDImageXObject noteImage = PDImageXObject.createFromByteArray(new PDDocument(), annotationBytes,
				"annotationImage");

		ImageCellBuilder imageCell = ImageCell.builder().verticalAlignment(VerticalAlignment.MIDDLE)
				.horizontalAlignment(HorizontalAlignment.RIGHT).borderWidthBottom(BORDER_WIDTH_0_7).paddingLeft(13f)
				.paddingTop(3.5f).borderWidthTop(BORDER_WIDTH_0_7).image(noteImage).scale(0.80f).maxHeight(40f)
				.minHeight(40f);

		TextCell reviewNotesCell = TextCell.builder().colSpan(2).minHeight(40f)
				.horizontalAlignment(HorizontalAlignment.LEFT).verticalAlignment(VerticalAlignment.MIDDLE)
				.text("ANNOTATIONS").borderWidthBottom(BORDER_WIDTH_0_7).borderWidthTop(BORDER_WIDTH_0_7)
				.paddingBottom(PADDING_5).font(PDType1Font.HELVETICA_BOLD).fontSize(FONT_SIZE_15)
				.textColor(Color.DARK_GRAY).backgroundColor(Color.WHITE).build();
		reviewDetailsTableBuilder.addRow(Row.builder().add(imageCell.build()).add(reviewNotesCell).build());

		final byte[] bytes4 = IOUtils
				.toByteArray(getClass().getClassLoader().getResourceAsStream("highlight_background.png"));
		final PDImageXObject highlightedAreaImage = PDImageXObject.createFromByteArray(new PDDocument(), bytes4,
				"highback");

		List<AnnotationComment> annotationsComments = new ArrayList<>();
		AnnotationComment annotationComment1 = new AnnotationComment();
		annotationComment1.setMarkupTypeId(1);
		annotationComment1
				.setMarkUpJson(new JSONObject("{\"xPosition\":278.58231707317077,\"yPosition\":1036.5853658536587}"));
		annotationComment1.setCommentNumber("7");

		AnnotationComment annotationComment2 = new AnnotationComment();
		annotationComment2.setMarkupTypeId(3);
		annotationComment2.setHighlightedAreaImage(highlightedAreaImage);
		annotationComment2.setMarkUpJson(new JSONObject(
				"{\"xPosition\":1249.578101503337,\"yPosition\":864.9923113957504,\"width\":312.8834355828221,\"height\":187.73008124228636}"));
		annotationComment2.setCommentNumber("8");

		AnnotationComment annotationComment3 = new AnnotationComment();
		annotationComment3.setMarkupTypeId(2);
		annotationComment3.setHighlightedAreaImage(highlightedAreaImage);
		annotationComment3.setMarkUpJson(new JSONObject(
				"{\"xPosition\":1819.8551433842358,\"yPosition\":810.4802224694229,\"width\":168.4451219512195,\"height\":204.72555044220715}"));
		annotationComment3.setCommentNumber("14");

		annotationsComments.add(annotationComment1);
		annotationsComments.add(annotationComment2);
		annotationsComments.add(annotationComment3);

		List<ReviewComment> annotationPageComments = new ArrayList<>();

		ReviewComment reviewComment1 = new ReviewComment();
		reviewComment1.setComments("Vikram Test Comment 1");
		reviewComment1.setPostedOn("Posted on March 17, 2020 at 10:21 AM");
		reviewComment1.setUserDisplayName("Vikram Tambe");
		reviewComment1.setCommentNumber(1);

		ReviewComment reviewComment2 = new ReviewComment();
		reviewComment2.setComments("Vikram Test Comment 2");
		reviewComment2.setPostedOn("Posted on March 17, 2020 at 10:22 AM");
		reviewComment2.setUserDisplayName("Vikram Tambe");
		reviewComment2.setCommentNumber(2);

		ReviewComment reviewComment3 = new ReviewComment();
		reviewComment3.setComments("Vikram Test Comment 3");
		reviewComment3.setPostedOn("Posted on March 17, 2020 at 10:23 AM");
		reviewComment3.setUserDisplayName("Vikram Tambe");
		reviewComment3.setCommentNumber(3);

		ReviewComment reviewComment4 = new ReviewComment();
		reviewComment4.setComments("Vikram Test Comment 4");
		reviewComment4.setPostedOn("Posted on March 17, 2020 at 10:24 AM");
		reviewComment4.setUserDisplayName("Vikram Tambe");
		reviewComment4.setCommentNumber(4);

		ReviewComment reviewComment5 = new ReviewComment();
		reviewComment5.setComments("Vikram Test Comment 5");
		reviewComment5.setPostedOn("Posted on March 17, 2020 at 10:25 AM");
		reviewComment5.setUserDisplayName("Vikram Tambe");
		reviewComment5.setCommentNumber(5);

		annotationPageComments.add(reviewComment1);
		annotationPageComments.add(reviewComment2);
		annotationPageComments.add(reviewComment3);
		annotationPageComments.add(reviewComment4);
		annotationPageComments.add(reviewComment5);

		ReviewComment reviewComment6 = new ReviewComment();
		reviewComment6.setComments("Vikram Test Comment 6");
		reviewComment6.setPostedOn("Posted on March 17, 2020 at 10:26 AM");
		reviewComment6.setUserDisplayName("Vikram Tambe");
		reviewComment6.setCommentNumber(6);

		ReviewComment reviewComment7 = new ReviewComment();
		reviewComment7.setComments("Vikram Test Comment 7");
		reviewComment7.setPostedOn("Posted on March 17, 2020 at 10:27 AM");
		reviewComment7.setUserDisplayName("Vikram Tambe");
		reviewComment7.setCommentNumber(7);

		ReviewComment reviewComment8 = new ReviewComment();
		reviewComment8.setComments("Vikram Test Comment 8");
		reviewComment8.setPostedOn("Posted on March 17, 2020 at 10:28 AM");
		reviewComment8.setUserDisplayName("Vikram Tambe");
		reviewComment8.setCommentNumber(8);

		ReviewComment reviewComment9 = new ReviewComment();
		reviewComment9.setComments("Vikram Test Comment 9");
		reviewComment9.setPostedOn("Posted on March 17, 2020 at 10:29 AM");
		reviewComment9.setUserDisplayName("Vikram Tambe");
		reviewComment9.setCommentNumber(9);

		ReviewComment reviewComment10 = new ReviewComment();
		reviewComment10.setComments("Vikram Test Comment 10");
		reviewComment10.setPostedOn("Posted on March 17, 2020 at 10:30 AM");
		reviewComment10.setUserDisplayName("Vikram Tambe");
		reviewComment10.setCommentNumber(10);

		annotationPageComments.add(reviewComment6);
		annotationPageComments.add(reviewComment7);
		annotationPageComments.add(reviewComment8);
		annotationPageComments.add(reviewComment9);
		annotationPageComments.add(reviewComment10);

		ReviewComment reviewComment11 = new ReviewComment();
		reviewComment11.setComments("Vikram Test Comment 11");
		reviewComment11.setPostedOn("Posted on March 17, 2020 at 10:31 AM");
		reviewComment11.setUserDisplayName("Vikram Tambe");
		reviewComment11.setCommentNumber(11);

		ReviewComment reviewComment12 = new ReviewComment();
		reviewComment12.setComments("Vikram Test Comment 12");
		reviewComment12.setPostedOn("Posted on March 17, 2020 at 10:32 AM");
		reviewComment12.setUserDisplayName("Vikram Tambe");
		reviewComment12.setCommentNumber(12);

		ReviewComment reviewComment13 = new ReviewComment();
		reviewComment13.setComments("Vikram Test Comment 13");
		reviewComment13.setPostedOn("Posted on March 17, 2020 at 10:33 AM");
		reviewComment13.setUserDisplayName("Vikram Tambe");
		reviewComment13.setCommentNumber(13);

		ReviewComment reviewComment14 = new ReviewComment();
		reviewComment14.setComments("Vikram Test Comment 14");
		reviewComment14.setPostedOn("Posted on March 17, 2020 at 10:34 AM");
		reviewComment14.setUserDisplayName("Vikram Tambe");
		reviewComment14.setCommentNumber(14);

		ReviewComment reviewComment15 = new ReviewComment();
		reviewComment15.setComments("Vikram Test Comment 15");
		reviewComment15.setPostedOn("Posted on March 17, 2020 at 10:35 AM");
		reviewComment15.setUserDisplayName("Vikram Tambe");
		reviewComment15.setCommentNumber(15);

		annotationPageComments.add(reviewComment11);
		annotationPageComments.add(reviewComment12);
		annotationPageComments.add(reviewComment13);
		annotationPageComments.add(reviewComment14);
		annotationPageComments.add(reviewComment15);
		
		ReviewComment reviewComment16 = new ReviewComment();
		reviewComment16.setComments("Vikram Test Comment 16");
		reviewComment16.setPostedOn("Posted on March 17, 2020 at 10:36 AM");
		reviewComment16.setUserDisplayName("Vikram Tambe");
		reviewComment16.setCommentNumber(16);

		ReviewComment reviewComment17 = new ReviewComment();
		reviewComment17.setComments("Vikram Test Comment 17");
		reviewComment17.setPostedOn("Posted on March 17, 2020 at 10:37 AM");
		reviewComment17.setUserDisplayName("Vikram Tambe");
		reviewComment17.setCommentNumber(17);

		ReviewComment reviewComment18 = new ReviewComment();
		reviewComment18.setComments("Vikram Test Comment 18");
		reviewComment18.setPostedOn("Posted on March 17, 2020 at 10:38 AM");
		reviewComment18.setUserDisplayName("Vikram Tambe");
		reviewComment18.setCommentNumber(18);

		ReviewComment reviewComment19 = new ReviewComment();
		reviewComment19.setComments("Vikram Test Comment 19");
		reviewComment19.setPostedOn("Posted on March 17, 2020 at 10:39 AM");
		reviewComment19.setUserDisplayName("Vikram Tambe");
		reviewComment19.setCommentNumber(19);

		ReviewComment reviewComment20 = new ReviewComment();
		reviewComment20.setComments("Vikram Test Comment 20");
		reviewComment20.setPostedOn("Posted on March 17, 2020 at 10:40 AM");
		reviewComment20.setUserDisplayName("Vikram Tambe");
		reviewComment20.setCommentNumber(20);

		annotationPageComments.add(reviewComment16);
		annotationPageComments.add(reviewComment17);
		annotationPageComments.add(reviewComment18);
		annotationPageComments.add(reviewComment19);
		annotationPageComments.add(reviewComment20);

		final byte[] annotationImageBytes = IOUtils
				.toByteArray(getClass().getClassLoader().getResourceAsStream("341-ctb-actual.jpg"));

		final PDImageXObject reviewMultiPageImage = PDImageXObject.createFromByteArray(new PDDocument(),
				annotationImageBytes, "annotationImage");

		ImageCellBuilder reviewMultiPageCell = RoboAnnotationImageCell.builder()
				.verticalAlignment(VerticalAlignment.TOP).originalHeight(400f).originalWidth(400f)
				.horizontalAlignment(HorizontalAlignment.RIGHT).imageFooterText(String.valueOf(1))
				.annotationsComments(annotationsComments).borderWidth(1).image(reviewMultiPageImage).maxHeight(300f)
				.width(200f);

		int noOfAnnotationPageComments = annotationPageComments.size();
		int rowCounter = 0;
		int rowSpan = 6;
		boolean isReminderCovered = false;
		int quotient = noOfAnnotationPageComments / rowSpan;
		int lagerDivisibleNumber = rowSpan * quotient;
		for (ReviewComment reviewComment : annotationPageComments) {
			rowCounter++;
			AnnotationCommentToTableData data = addAnnotationCommentToTable(reviewDetailsTableBuilder, reviewComment,
					null, 1, reviewMultiPageCell, rowCounter, noOfAnnotationPageComments, lagerDivisibleNumber,
					isReminderCovered, rowSpan);
			rowCounter = data.getCurrentCnt();
			isReminderCovered = data.isReminderCovered();
		}

		List<Integer> headerRowsIndexs = new ArrayList<>();
		headerRowsIndexs.add(0);
		headerRowsIndexs.add(1);
		headerRowsIndexs.add(7);

		float startX = 18f;
		float startY = 55f;

		float pageWidth = 800f;
		float pageHeight = 700f;
		final PDPage pdPage = new PDPage(new PDRectangle(pageWidth, pageHeight));
		float upperRightY = pdPage.getMediaBox().getUpperRightY();

		final PDDocument pdfDocument = new PDDocument();
		
		RepeatedHeaderTableDrawer.builder().headerRowsIndexs(headerRowsIndexs).table(reviewDetailsTableBuilder.build())
		.startX(startX).startY(upperRightY - startY).endY(55f).build()
		.draw(() -> pdfDocument, () -> new PDPage(new PDRectangle(pageWidth, pageHeight)), 60f);
		
		pdfDocument.save("target" + "/" + "roboannotationImageMultipages.pdf");
		pdfDocument.close();

	}

	private AnnotationCommentToTableData addAnnotationCommentToTable(Table.TableBuilder annotationCommentTable,
			ReviewComment reviewComment, ReviewComment parentReviewComment, int commentDepth,
			ImageCellBuilder reviewMultiPageCell, int rowCounter, int noOfAnnotationComments, int lagerDivisibleNumber,
			boolean isReminderCovered, int rowSpan) {

		int res = rowCounter % rowSpan;

		try {
			int defaultRows = 3;

			int actualRowSpan = (defaultRows * (noOfAnnotationComments >= rowSpan ? rowSpan : noOfAnnotationComments))
					+ 1;
			if (rowCounter == 1) {
				annotationCommentTable.addRow(
						Row.builder().add(TextCell.builder().text("").build()).add(TextCell.builder().text("").build())
								.add(reviewMultiPageCell.rowSpan(actualRowSpan).build()).build());
			}

			if (res == 0 && rowCounter != noOfAnnotationComments && (noOfAnnotationComments - rowCounter >= rowSpan)) {
				annotationCommentTable.addRow(
						Row.builder().add(TextCell.builder().text("").build()).add(TextCell.builder().text("").build())
								.add(reviewMultiPageCell.rowSpan(actualRowSpan).build()).build());
			}
			if (lagerDivisibleNumber > 0 && rowCounter > lagerDivisibleNumber && res < rowSpan
					&& isReminderCovered == Boolean.FALSE) {
				isReminderCovered = true;
				actualRowSpan = (defaultRows * (noOfAnnotationComments - lagerDivisibleNumber)) + 1;
				annotationCommentTable.addRow(
						Row.builder().add(TextCell.builder().text("").build()).add(TextCell.builder().text("").build())
								.add(reviewMultiPageCell.rowSpan(actualRowSpan).build()).build());
			}

			ImageCellBuilder noteImageCell = null;
			StringBuilder userNameDetails = new StringBuilder();
			userNameDetails.append(reviewComment.getUserDisplayName());
			if (parentReviewComment != null) {
				final byte[] bytes3 = IOUtils
						.toByteArray(getClass().getClassLoader().getResourceAsStream("user_no_image.png"));
				final PDImageXObject noteImage = PDImageXObject.createFromByteArray(new PDDocument(), bytes3,
						"noImage");

				noteImageCell = ImageCell.builder().verticalAlignment(VerticalAlignment.TOP)
						.horizontalAlignment(HorizontalAlignment.LEFT).borderWidth(1).image(noteImage).scale(0.40f)
						.maxHeight(20f).minHeight(20f).width(20f);

				if (reviewComment.getReviewComment() != null) {
					userNameDetails.append(" -> ").append(reviewComment.getReviewComment().getUserDisplayName());
				} else {
					userNameDetails.append(" -> ").append(parentReviewComment.getUserDisplayName());
				}
			}
			float paddingLeft = 0f;
			TextCellBuilder createdByUserTextCell = TextCell.builder().horizontalAlignment(HorizontalAlignment.LEFT)
					.paddingLeft(paddingLeft).verticalAlignment(VerticalAlignment.TOP).text(userNameDetails.toString())
					.font(PDType1Font.HELVETICA_BOLD).fontSize(FONT_SIZE_11).textColor(new Color(0, 183, 246))
					.backgroundColor(Color.WHITE);

			if (reviewComment.getCommentNumber() != null && commentDepth == 1) {

				final byte[] bytes3 = IOUtils
						.toByteArray(getClass().getClassLoader().getResourceAsStream("user_no_image.png"));
				final PDImageXObject userImage = PDImageXObject.createFromByteArray(new PDDocument(), bytes3,
						"noImage");

				ImageCellBuilder userImageCell = RoboImageCell.builder().verticalAlignment(VerticalAlignment.TOP)
						.horizontalAlignment(HorizontalAlignment.CENTER)
						.imageText(String.valueOf(reviewComment.getCommentNumber().intValue())).borderWidth(1)
						.image(userImage).scale(0.90f).maxHeight(30f).minHeight(30f).width(50f).paddingBottom(5f);

				annotationCommentTable.addRow(
						Row.builder().add(userImageCell.rowSpan(3).build()).add(createdByUserTextCell.build()).build());
			} else if (commentDepth == 2) {
				paddingLeft = 8f;
				annotationCommentTable.addRow(Row.builder().add(TextCell.builder().text("").rowSpan(3).build())
						.add(createdByUserTextCell.paddingLeft(paddingLeft).build()).build());
			} else if (commentDepth > 2) {
				paddingLeft = 12f;
				annotationCommentTable.addRow(Row.builder().add(TextCell.builder().text("").rowSpan(3).build())
						.add(createdByUserTextCell.paddingLeft(paddingLeft).build()).build());
			}

			String postedOnDetails = reviewComment.getPostedOn();

			TextCell postedOnTextCell = TextCell.builder().colSpan(1).horizontalAlignment(HorizontalAlignment.LEFT)
					.paddingLeft(paddingLeft).verticalAlignment(VerticalAlignment.TOP).text(postedOnDetails)
					.font(PDType1Font.HELVETICA_BOLD).fontSize(FONT_SIZE_11).textColor(Color.BLACK)
					.backgroundColor(Color.WHITE).build();

			TextCell noteDescTextCell = TextCell.builder().colSpan(1).horizontalAlignment(HorizontalAlignment.LEFT)
					.minHeight(30f).paddingLeft(paddingLeft).verticalAlignment(VerticalAlignment.TOP)
					.text(reviewComment.getComments()).font(PDType1Font.HELVETICA).fontSize(FONT_SIZE_11)
					.textColor(Color.BLACK).backgroundColor(Color.WHITE).build();

			annotationCommentTable.addRow(Row.builder().add(postedOnTextCell).build());
			annotationCommentTable.addRow(Row.builder().add(noteDescTextCell).build());

			// Child comments
			for (ReviewComment childComment : reviewComment.getReviewComments()) {
				rowCounter++;
				AnnotationCommentToTableData data = addAnnotationCommentToTable(annotationCommentTable, childComment,
						reviewComment, commentDepth + 1, reviewMultiPageCell, rowCounter, noOfAnnotationComments,
						lagerDivisibleNumber, isReminderCovered, rowSpan);
				rowCounter = data.getCurrentCnt();
				isReminderCovered = data.isReminderCovered();
			}

		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		}

		return new AnnotationCommentToTableData(rowCounter, isReminderCovered);
	}

}
