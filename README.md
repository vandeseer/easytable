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
* line breaking

I would say: it's OK, but don't expect too much ... ;-)

## Example

In order to produce a whole PDF document with a table that looks like this one:

![easytable table](doc/example.png)

You need the following code:

    // Some data
    Object[][] data = {
            { "Whisky"  , 134.4 , 145.98 },
            { "Beer"    , 768.2 , 677.9  },
            { "Gin"     , 456.45, 612.0  },
            { "Vodka"   , 302.71 , 465.2 }
    };

    // Define the table structure first
    TableBuilder tableBuilder = TableBuilder.newBuilder()
            .addColumnOfWidth(100)
            .addColumnOfWidth(50)
            .addColumnOfWidth(50)
            .addColumnOfWidth(50)
            .setFontSize(8)
            .setFont(HELVETICA)
            .setBorderColor(Color.WHITE);

    // Add the header row ...
    Row headerRow = RowBuilder.newBuilder()
            .add(Cell.withText("Product").withAllBorders())
            .add(Cell.withText("2018").withAllBorders().setHorizontalAlignment(CENTER))
            .add(Cell.withText("2019").withAllBorders().setHorizontalAlignment(CENTER))
            .add(Cell.withText("Total").withAllBorders().setHorizontalAlignment(CENTER))
            .setBackgroundColor(BLUE_DARK)
            .withTextColor(Color.WHITE)
            .setFont(PDType1Font.HELVETICA_BOLD)
            .setFontSize(9)
            .build();

    tableBuilder.addRow(headerRow);

    // ... and some data rows
    double grandTotal = 0;
    for (int i = 0; i < data.length; i++) {
        Object[] dataRow = data[i];
        double total = (double) dataRow[1] + (double) dataRow[2];
        grandTotal += total;

        tableBuilder.addRow(RowBuilder.newBuilder()
                .add(Cell.withText(dataRow[0]).withAllBorders())
                .add(Cell.withText(dataRow[1] + " €").withAllBorders().setHorizontalAlignment(RIGHT))
                .add(Cell.withText(dataRow[2] + " €").withAllBorders().setHorizontalAlignment(RIGHT))
                .add(Cell.withText(total + " €").withAllBorders().setHorizontalAlignment(RIGHT))
                .setBackgroundColor(i % 2 == 0 ? BLUE_LIGHT_1 : BLUE_LIGHT_2)
                .build());
    }

    // Add a final row
    tableBuilder.addRow(RowBuilder.newBuilder()
            .add(Cell.withText("This spans over 3 cells and shows the grand total in the next cell:")
                    .span(3)
                    .setBorderWidthTop(1)
                    .withTextColor(WHITE)
                    .withAllBorders()
                    .setHorizontalAlignment(RIGHT)
                    .setBackgroundColor(BLUE_DARK)
                    .withFontSize(6)
                    .withFont(HELVETICA_OBLIQUE))
            .add(Cell.withText(grandTotal + " €").setBackgroundColor(LIGHT_GRAY)
                .withFont(HELVETICA_BOLD_OBLIQUE)
                .setHorizontalAlignment(RIGHT)
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
    git checkout v0.0.13
    mvn clean install

Define this in your `pom.xml` in order to use it:

    <dependency>
        <groupId>org.vandeseer.pdfbox</groupId>
        <artifactId>easytable</artifactId>
        <version>0.0.13</version>
    </dependency>

At one point it will hopefully also be available at maven central. 

## Kudos

- to [Binghammer](https://github.com/Binghammer) for implementing cell coloring and text center alignment
- to [Sebastian Göhring](https://github.com/TheSilentHorizon) for finding and fixing a bug (column spanning)
- to [AndreKoepke](https://github.com/AndreKoepke) for the line breaking feature, some refactorings and improvements

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
