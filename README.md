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
* vertical text alignment
* row spanning
* line breaking and line spacing
* images in cells

## Example

In order to produce a whole PDF document with a table that looks like this one:

![easytable table](doc/example.png)

You need the following code:

    // Some data
    final Object[][] data = {
            {"Whisky", 134.4, 145.98},
            {"Beer", 768.2, 677.9},
            {"Gin", 456.45, 612.0},
            {"Vodka", 302.71, 465.2}
    };

    // Define the table structure first
    final TableBuilder tableBuilder = TableBuilder.newBuilder()
            .addColumnOfWidth(100)
            .addColumnOfWidth(50)
            .addColumnOfWidth(50)
            .addColumnOfWidth(50)
            .setFontSize(8)
            .setFont(HELVETICA)
            .setBorderColor(Color.WHITE);

    // Add the header row ...
    final Row headerRow = RowBuilder.newBuilder()
            .add(CellText.builder().text("Product").build().withAllBorders())
            .add(CellText.builder().text("2018").horizontalAlignment(CENTER).build().withAllBorders())
            .add(CellText.builder().text("2019").horizontalAlignment(CENTER).build().withAllBorders())
            .add(CellText.builder().text("Total").horizontalAlignment(CENTER).build().withAllBorders())
            .setBackgroundColor(TableDrawerIntegrationTest.BLUE_DARK)
            .withTextColor(Color.WHITE)
            .setFont(PDType1Font.HELVETICA_BOLD)
            .setFontSize(9)
            .build();

    tableBuilder.addRow(headerRow);

    // ... and some data rows
    double grandTotal = 0;
    for (int i = 0; i < data.length; i++) {
        final Object[] dataRow = data[i];
        final double total = (double) dataRow[1] + (double) dataRow[2];
        grandTotal += total;

        tableBuilder.addRow(RowBuilder.newBuilder()
                .add(CellText.builder().text(String.valueOf(dataRow[0])).build().withAllBorders())
                .add(CellText.builder().text(dataRow[1] + " €").horizontalAlignment(RIGHT).build().withAllBorders())
                .add(CellText.builder().text(dataRow[2] + " €").horizontalAlignment(RIGHT).build().withAllBorders())
                .add(CellText.builder().text(total + " €").horizontalAlignment(RIGHT).build().withAllBorders())
                .setBackgroundColor(i % 2 == 0 ? TableDrawerIntegrationTest.BLUE_LIGHT_1 : TableDrawerIntegrationTest.BLUE_LIGHT_2)
                .build())
        .setWordBreaking();
    }

    // Add a final row
    tableBuilder.addRow(RowBuilder.newBuilder()
            .add(CellText.builder().text("This spans over 3 cells, is right aligned and its text is so long that it even breaks. " +
                    "Also it shows the grand total in the next cell and furthermore vertical alignment is shown:")
                    .span(3)
                    .lineSpacing(1f)
                    .borderWidthTop(1)
                    .textColor(WHITE)
                    .horizontalAlignment(RIGHT)
                    .backgroundColor(TableDrawerIntegrationTest.BLUE_DARK)
                    .fontSize(6)
                    .font(HELVETICA_OBLIQUE)
                    .build()
                    .withAllBorders())
            .add(CellText.builder().text(grandTotal + " €").backgroundColor(LIGHT_GRAY)
                    .font(HELVETICA_BOLD_OBLIQUE)
                    .horizontalAlignment(RIGHT)
                    .verticalAlignment(TOP)
                    .build()
                    .withAllBorders())
            .build());
            
     final PDDocument document = new PDDocument();
     final PDPage page = new PDPage(PDRectangle.A4);
     document.addPage(page);

     final PDPageContentStream contentStream = new PDPageContentStream(document, page);

     // Define the starting point
     final float startY = page.getMediaBox().getHeight() - 50;
     final int startX = 50;

     // Draw!
     TableDrawer.TableDrawerBuilder.newBuilder()
             .withContentStream(contentStream)
             .withTable(table)
             .startX(startX)
             .startY(startY)
             .build()
             .draw();
     contentStream.close();

     document.save(fileToSaveTo);
     document.close();

If you run the tests with `mvn clean test` there also some PDF documents created which you can find in the `target` folder.
The corresponding sources (in order to understand how to use the code) can be found in the test package.

## Installation

First check it out and install it locally:

    git clone https://github.com/vandeseer/easytable.git
    cd easytable
    git checkout v0.1.0
    mvn clean install

Define this in your `pom.xml` in order to use it:

    <dependency>
        <groupId>org.vandeseer.pdfbox</groupId>
        <artifactId>easytable</artifactId>
        <version>0.1.0</version>
    </dependency>

At one point it will hopefully also be available at maven central. 

## Kudos

- to [Binghammer](https://github.com/Binghammer) for implementing cell coloring and text center alignment
- to [Sebastian Göhring](https://github.com/TheSilentHorizon) for finding and fixing a bug (column spanning)
- to [AndreKoepke](https://github.com/AndreKoepke) for the line breaking feature, some bigger nice refactorings and 
improvements

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
