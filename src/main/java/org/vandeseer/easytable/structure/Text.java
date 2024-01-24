package org.vandeseer.easytable.structure;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.Optional;

@Setter
public class Text {

    @Getter
    private final String text;
    private final Color color;

    public Text(String text, Color color) {
        this.text = text;
        this.color = color;
    }

    public Text(String text) {
        this.text = text;
        this.color = null;
    }

    public Optional<Color> getColor() {
        return Optional.ofNullable(color);
    }

}
