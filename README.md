# easytable

This is a (very) small project that builds upon
[Apache's PDFBox](http://pdfbox.apache.org) (>= 2.0.0) and should allow you
to create tables in a fairly simple way.
It emerged from the need in another project. Therefore it also may miss some
crucial features. Nevertheless there is:
* setting font and font size on table level as well as cell level
* setting single cells with bottom-, top-, left- and right-border width separately
* background color on row or cell level
* padding (top, bottom, left, right) on cell level
* border colors (on table, row or cell level)
* support for text alignment (right, left or center)
* row spanning

I would say: it's OK, but don't expect too much ... ;-)

## Example

    // Define the table structure first
    TableBuilder tableBuilder = TableBuilder.newBuilder()
            .addColumnOfWidth(300)
            .addColumnOfWidth(120)
            .addColumnOfWidth(70)
            .setFontSize(8)
            .setFont(PDType1Font.HELVETICA);

    // Header ...
    tableBuilder.addRow(RowBuilder.newBuilder()
            .add(Cell.withText("This is right aligned without a border").setHorizontalAlignment(RIGHT))
            .add(Cell.withText("And this is another cell"))
            .add(Cell.withText("Sum").setBackgroundColor(Color.ORANGE))
            .setBackgroundColor(Color.BLUE)
            .build());

    // ... and some cells
    for (int i = 0; i < 10; i++) {
        tableBuilder.addRow(RowBuilder.newBuilder()
                .add(Cell.withText(i).withAllBorders())
                .add(Cell.withText(i * i).withAllBorders())
                .add(Cell.withText(i + (i * i)).withAllBorders())
                .setBackgroundColor(i % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE)
                .build());
    }

    final PDDocument document = new PDDocument();
    final PDPage page = new PDPage(PDRectangle.A4);
    document.addPage(page);

    final PDPageContentStream contentStream = new PDPageContentStream(document, page);

    // Define the starting point
    final float startY = page.getMediaBox().getHeight() - 50;
    final int startX = 50;

    // Draw!
    (new TableDrawer(contentStream, tableBuilder.build(), startX, startY)).draw();
    contentStream.close();

    document.save("target/sampleWithColorsAndBorders.pdf");
    document.close();

This should produce a whole PDF document with a table that looks like this one:

![easytable table](https://raw.githubusercontent.com/vandeseer/easytable/master/easytable/doc/example.png)

If you run the tests with `mvn clean test` there also three PDF documents created which you can find in the `target` folder.
The corresponding sources (in order to understand how to use the code) can be found in the test package.

## Installation

First check it out and install it locally:

    mvn clean install

Define this in your `pom.xml` in order to use it:

    <dependency>
        <groupId>org.vandeseer.pdfbox</groupId>
        <artifactId>easytable</artifactId>
        <version>0.0.10</version>
    </dependency>

At one point it will hopefully also be available at maven central. 

## Kudos

Thanks @Binghammer for implementing cell coloring and text center alignment!

## Q&A

### Does it work with Java < 8?

Nope. You will need Java 8.

### Does it work with PDFBox 1.8.9?

Well, Using it with PDFBox 1.8.9 requires you to check out version
0.0.7 (tagged as such in git) and install it locally, i.e.:

    git checkout v0.0.7
    mvn clean install

Note though that the API will have changed in the meantime ...

### Cool, I like it, can I buy you a beer?

Yes. Or you can upvote this answer on [stackoverflow](https://stackoverflow.com/questions/28059563/how-to-create-table-using-apache-pdfbox/42612456#42612456).
