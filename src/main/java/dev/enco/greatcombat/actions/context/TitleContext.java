package dev.enco.greatcombat.actions.context;

import dev.enco.greatcombat.utils.colorizer.Colorizer;

public record TitleContext(
        String title,
        String subtitle,
        int fadeIn,
        int stayIn,
        int fadeOut
) implements Context {
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
