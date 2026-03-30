package dev.enco.greatcombat.core.actions.context;

import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.utils.colorizer.Colorizer;

/**
 * Context implementation for string-based actions.
 *
 * @param string The colorized string content
 */
public record StringContext(
        String string
) implements Context {
    /**
     * Validates and creates a StringContext from a raw string.
     * Applies colorization to the input string.
     *
     * @param args The raw string to validate and colorize
     * @return New StringContext with colorized content
     */
    public static StringContext validate(String args, Locale locale) {
        return new StringContext(Colorizer.colorize(args));
    }
}
