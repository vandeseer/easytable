# easytable

This is a (very) small project that builds upon pdfbox (1.8.9) and should help
filling the gap for table creation. It's OK, but don't expect too much.

## Example

    final PDDocument document = new PDDocument();
    final PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);
    document.addPage(page);

    final float startY = page.findMediaBox().getHeight() - 150;
    final int startX = 56;

    final PDPageContentStream contentStream = new PDPageContentStream(document, page);

    TableBuilder tableBuilder = new TableBuilder()
            .addColumn(new Column(290))
            .addColumn(new Column(120))
            .addColumn(new Column(70))
            .setFontSize(8)
            .setFont(PDType1Font.HELVETICA);

    Color lightBlue = new Color(194, 232, 233);

    // Header
    tableBuilder
            .addRow(
                    new RowBuilder()
                            .add(Cell.of("This is right aligned without a border")
                                    .setBackgroundColor(lightBlue)
                                    .setHorizontalAlignment(HorizontalAlignment.RIGHT))
                            .add(Cell.of("And this is another cell")
                                    .setBackgroundColor(lightBlue))
                            .add(Cell.of("Sum")
                                    .setBackgroundColor(Color.ORANGE))
                            .build());

    for (int i = 0; i < 12; i++) {
        Color backgroundColor = i % 2 == 0 ? new Color(240, 240, 240) : Color.WHITE;
        tableBuilder.addRow(
                new RowBuilder()
                        .add(Cell.of(i).withAllBorders().setBackgroundColor(backgroundColor))
                        .add(Cell.of(i * i).withAllBorders().setBackgroundColor(backgroundColor))
                        .add(Cell.of(i + (i * i)).withAllBorders().setBackgroundColor(backgroundColor))
                        .build());
    }

    (new TableDrawer(contentStream, tableBuilder.build(), startX, startY)).draw();
    contentStream.close();

    document.save("target/sampleWithColorsAndBorders.pdf");
    document.close();

This should produce a whole PDF document with a table that looks like this one:

[[https://raw.githubusercontent.com/vandeseer/easytable/master/easytable/doc/example.png|alt=Table]]

If you run the tests with `mvn clean test` there also three PDF documents created which you can find in the `target` folder.
The corresponding sources (in order to understand how to use the code) can be found in the test package.

## Installation

This should do the trick:

    mvn clean install

## Hints

You will need Java 8 ;)
