package org.vandeseer.easytable.structure.cell;

import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.vandeseer.easytable.drawing.Drawer;
import org.vandeseer.easytable.drawing.cell.LinkedTextCellDrawer;

import java.net.URL;
import java.util.LinkedList;

@Getter
@SuperBuilder(toBuilder = true)
public class LinkedTextCell extends AbstractTextCell {

    @NonNull
    protected LinkedText linkedText;

    @Override
    public String getText() {
        return linkedText.text;
    }

    protected Drawer createDefaultDrawer() {
        return new LinkedTextCellDrawer(this);
    }

    public static class LinkedText {
        public final String text;
        public final LinkedList<Link> links;

        private LinkedText(String text, LinkedList<Link> links) {
            this.text = text;
            this.links = links;
        }

        @Value
        public static final class Link {
            private final int start;
            private final int end;
            private final String text;
            private final URL url;
        }

        public static class LinkedTextBuilder {
            StringBuilder stringBuilder = new StringBuilder();
            LinkedList<Link> links = new LinkedList<>();

            public LinkedTextBuilder append(String text) {
                stringBuilder.append(text);
                return this;
            }

            public LinkedTextBuilder append(String text, URL url) {
                int start = stringBuilder.length();
                stringBuilder.append(text);
                int end = stringBuilder.length();

                links.add(new Link(start, end, text, url));
                return this;
            }

            public LinkedText build() {
                return new LinkedText(stringBuilder.toString(), links);
            }
        }

        public static LinkedTextBuilder builder() {
            return new LinkedTextBuilder();
        }
    }


}
