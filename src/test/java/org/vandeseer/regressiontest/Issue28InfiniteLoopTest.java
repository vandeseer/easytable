package org.vandeseer.regressiontest;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.Test;
import org.vandeseer.TestUtils;
import org.vandeseer.easytable.RowIsToHighException;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;
import java.io.IOException;

public class Issue28InfiniteLoopTest {

    @Test(expected = RowIsToHighException.class)
    public void createTwoPageTable() throws IOException {
        final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnOfWidth(200)
                .addColumnOfWidth(200);

        TextCell dummyHeaderCell = TextCell.builder()
                .text("Header dummy")
                .backgroundColor(Color.BLUE)
                .textColor(Color.WHITE)
                .borderWidth(1F)
                .build();

        String longText = "Sed turpis nisl, ullamcorper vitae ornare eget, porta ac odio. Nunc lacinia convallis urna " +
                "sit amet scelerisque. Morbi neque est, tempor sit amet sagittis in, luctus et nisl. " +
                "Phasellus ut mollis felis. Sed viverra tortor in ligula ultricies, quis faucibus turpis varius. " +
                "Duis ante leo, ornare sed lectus in, finibus placerat est. Pellentesque habitant morbi " +
                "tristique senectus et netus et malesuada fames ac turpis egestas. Phasellus rhoncus felis " +
                "sit amet ex consequat aliquam. Ut euismod odio iaculis vulputate malesuada. " +
                "Donec euismod ipsum id erat ullamcorper elementum. Aenean malesuada mattis libero ac pretium. " +
                "Nam non tempor risus, in congue turpis. Nam mi dolor, gravida ac imperdiet quis, pulvinar sed metus. " +
                "Sed tempor orci magna, non aliquet nisl posuere vitae. Cras erat felis, euismod et sodales cursus, " +
                "fermentum at arcu. Aenean maximus magna vel dignissim ullamcorper.Sed turpis nisl, ullamcorper " +
                "vitae ornare eget, porta ac odio. Nunc lacinia convallis urna sit amet scelerisque. " +
                "Morbi neque est, tempor sit amet sagittis in, luctus et nisl. Phasellus ut mollis felis. " +
                "Sed viverra tortor in ligula ultricies, quis faucibus turpis varius. Duis ante leo, ornare sed lectus in, " +
                "finibus placerat est. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac " +
                "turpis egestas. Phasellus rhoncus felis sit amet ex consequat aliquam. Ut euismod odio iaculis " +
                "vulputate malesuada. Donec euismod ipsum id erat ullamcorper elementum." +
                "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac " +
                "turpis egestas. Phasellus rhoncus felis sit amet ex consequat aliquam. Ut euismod odio iaculis " +
                "vulputate malesuada. Donec euismod ipsum id erat ullamcorper elementum.";

        TextCell dummyCell = TextCell.builder()
                .text("dummy")
                .borderWidth(1F)
                .build();


        TextCell bigCell = TextCell.builder()
                .text(longText)
                .borderWidth(1F)
                .build();

        tableBuilder.addRow(
                Row.builder()
                        .add(dummyHeaderCell)
                        .add(dummyHeaderCell)
                        .build());


        tableBuilder.addRow(
                Row.builder()
                        .add(dummyCell)
                        .add(bigCell)
                        .build());


        final PDDocument document = new PDDocument();

        TableDrawer drawer = TableDrawer.builder()
                .table(tableBuilder.build())
                .startX(50F)
                .startY(50F)
                .endY(50F) // note: if not set, table is drawn over the end of the page
                .build();

        drawer.draw(() -> document, () -> new PDPage(PDRectangle.A4), 50f);

        document.save(TestUtils.TARGET_FOLDER + TestUtils.TARGET_SUBFOLDER_REGRESSION + "/twoPageTable.pdf");
        document.close();
    }

}