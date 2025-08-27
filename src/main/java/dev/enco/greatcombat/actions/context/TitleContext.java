package dev.enco.greatcombat.actions.context;

import dev.enco.greatcombat.utils.colorizer.Colorizer;

/**
 * Context implementation for title display actions.
 * Contains all parameters required to display a title and subtitle.
 *
 * @param title The main title text
 * @param subtitle The subtitle text
 * @param fadeIn The fade-in time in ticks
 * @param stayIn The stay time in ticks
 * @param fadeOut The fade-out time in ticks
 */
public record TitleContext(
        String title,
        String subtitle,
        int fadeIn,
        int stayIn,
        int fadeOut
) implements Context {
    /**
     * Validates and parses a TitleContext from a formatted string.
     * Expected format: "title;subtitle;fadeIn;stayIn;fadeOut"
     * All parts are optional except the title. Missing parts use default values.
     * Applies colorization to title and subtitle texts.
     *
     * @param s The formatted string to parse
     * @return New TitleContext with parsed parameters
     */
    public static TitleContext validate(String s) {
        s = Colorizer.colorize(s);
        var args = s.split(";");
        var title = args.length > 0 ? args[0] : "";
        var subTitle = args.length > 1 ? args[1] : "";
        int fadeIn = args.length > 2 ? Integer.valueOf(args[2]) : 10;
        int stayIn = args.length > 3 ? Integer.valueOf(args[3]) : 70;
        int fadeOut = args.length > 4 ? Integer.valueOf(args[4]) : 20;
        return new TitleContext(title, subTitle, fadeIn, stayIn, fadeOut);
    }
}
