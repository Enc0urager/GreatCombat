package dev.enco.greatcombat.actions.context;

import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Locale;
import dev.enco.greatcombat.utils.logger.Logger;
import org.bukkit.Material;

import java.text.MessageFormat;

public record MaterialContext(
        Material material
) implements Context {
    public static MaterialContext validate(String s) {
        final Locale locale = ConfigManager.getLocale();
        var args = s.split(";");
        try {
            if (args.length >= 1)
                return new MaterialContext(Material.valueOf(args[0].toUpperCase()));
            Logger.warn(locale.nullMaterial());
        } catch (IllegalArgumentException e) {
            Logger.warn(MessageFormat.format(locale.materialError(), args[0]));
        }
        return null;
    }
}
