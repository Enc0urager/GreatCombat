package dev.enco.greatcombat.actions.context;

import dev.enco.greatcombat.utils.colorizer.Colorizer;

public record StringContext(
        String string
) implements Context {
    public static StringContext validate(String args) {
        return new StringContext(Colorizer.colorize(args));
    }
}
