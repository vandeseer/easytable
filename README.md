# easytable

This is a small project that builds upon
[Apache's PDFBox](http://pdfbox.apache.org) and should allow you
to create tables in a fairly simple way.
It emerged from the need in another project. Therefore, it also may miss some
crucial features. Nevertheless, there is:

* setting font and font size, table, row, column and cell level
* line breaking and line spacing
* background color and style on table, row, column and cell level
* padding (top, bottom, left, right) on table, row and cell level
* border color, width and style (on table, row or cell level)
* support for text alignment (right, left, center, justified)
* vertical text alignment (top, middle, bottom)
* column spanning and row spanning 
* images in cells
* allowing for a lot of customizations
* experimental: vertical text, drawing a large table's 
overflow on the same page

One can also override classes that are responsible for table/cell drawing, i.e. 
their drawing behaviour can be customized to a pretty high extent.

It is also possible to draw a table over multiple pages (even with the 
header row being repeated on every new page) or to draw a large table's overflow 
next to the already existing table on the same page (see below for examples).

## Installation

Add this to your `pom.xml`:

    <dependency>
        <groupId>com.github.vandeseer</groupId>
        <artifactId>easytable</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>

Or checkout the repository and install it locally with maven (e.g. for the`develop` branch):

    mvn clean install -DskipTests -Dgpg.skip -Ddependency-check.skip=true

## Examples

There is a [minimal full working example](src/test/java/org/vandeseer/MinimumWorkingExample.java) 
which should help you getting started.  

For a bit more involved tables have a look at [this code](src/test/java/org/vandeseer/integrationtest/ExcelLikeExampleTest.java) 
which is needed for creating a PDF document with the following two tables:

![easytable table](doc/example.png)

![easytable table](doc/example2.png)

For the next example have a look at the [SettingsTest.java](src/test/java/org/vandeseer/integrationtest/settings/SettingsTest.java):

![easytable table](doc/example3.png)

The last one illustrates the use of vertical text in text cells. The code for 
it can be found [here](src/test/java/org/vandeseer/integrationtest/VerticalTextCellTest.java):

![easytable table](doc/example4.png)

Drawing the overflow of a large table on the same page is also [possible](src/test/java/org/vandeseer/integrationtest/OverflowOnSamePageTableDrawerTest.java): 

![easytable table](doc/example_overflow_on_same_page.png)

If you run the tests with `mvn clean test` there also some PDF documents created which you can find in the `target` folder.
The corresponding sources (in order to understand how to use the code) can be found in the test package.

## Kudos

*   to [Binghammer](https://github.com/Binghammer) for implementing cell coloring and text center alignment
*   to [Sebastian Göhring](https://github.com/TheSilentHorizon) for finding and fixing a bug (column spanning)
*   to [AndreKoepke](https://github.com/AndreKoepke) for the line breaking feature, some bigger nice refactorings and 
improvements
*   to [Wolfgang Apolinarski](https://github.com/wapolinar) for the printing over pages and bugfixes
*   to [AdrianMiska](https://github.com/AdrianMiska) for finding and fixing an issue with cell height
*   to [TheRealSourceSeeker](https://github.com/TheRealSourceSeeker) for finding a bug caused by using `float`s
*   to [Drummond Dawson](https://github.com/drumonii) for code changes that allowed removing a dependency
*   to [styssi](https://github.com/styssi) for allowing several multipage tables being drawn on the same page
*   to [Richard Mealing](https://github.com/mealingr) for adding the license section to the `pom.xml`
*   to [msww](https://github.com/msww) for finding a [small issue](https://github.com/vandeseer/easytable/issues/85)
*   to [VakhoQ](https://github.com/VakhoQ) for implementing border styles
*   to [Miloš Čadek](https://github.com/c4da) for implementing alignment of vertical text cells

## Q&A

### Can I customize the drawers for my own specific needs?

Yep, you can customize the cell drawers itself or (depending on your use case)
you can just create a custom cell. 

For using a customized cell drawer, have a look at 
[CustomCellDrawerTest](src/test/java/org/vandeseer/integrationtest/custom/CustomCellDrawerTest.java).

In case you want to create your own type of cell (which shouldn't really be necessary since the 
drawing can be completely adapted) you will need to use [Lombok](https://projectlombok.org/)'s `@SuperBuilder`
annotation. Again, just have a look at the code: 
[CustomCellWithCustomDrawerUsingLombokTest](src/test/java/org/vandeseer/integrationtest/custom/CustomCellWithCustomDrawerUsingLombokTest.java)

### Can I draw a table over multiple pages?

Yes, have a look at [TableOverSeveralPagesTest.java](src/test/java/org/vandeseer/integrationtest/TableOverSeveralPagesTest.java).
Just use `startY(...)`  and `endY(..)` in order to restrict the vertical part of the page 
where the table should be drawn: 

    RepeatedHeaderTableDrawer.builder()
        .table(createTable())
        .startX(50)
        .startY(100F)
        .endY(50F) // <-- If the table is bigger, a new page is started
        .build()

### Is there a way to repeat the header on every page?

Depending on whether you want to repeat the header row or not you 
should use `RepeatedHeaderTableDrawer` or `TableDrawer` respectively.

### Can I get the `y` coordinate of the end of a drawn table?

Yes. Just use the `.getFinalY()` method. Also see [FinalYTest.java](src/test/java/org/vandeseer/integrationtest/FinalYTest.java).

### Cool, I like it, can I buy you a beer?

Yes. Or you can upvote this answer on [stackoverflow](https://stackoverflow.com/questions/28059563/how-to-create-table-using-apache-pdfbox/42612456#42612456). Or:

<a href="https://paypal.me/SDeser/5">
  <img src="https://raw.githubusercontent.com/stefan-niedermann/paypal-donate-button/master/paypal-donate-button.png" alt="Donate with PayPal" width="300px"/>
</a>
