package dev.enco.greatcombat.utils;

import dev.enco.greatcombat.config.ConfigManager;
import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@UtilityClass
public class Placeholders {
    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat df = new DecimalFormat("#.#", symbols);

    public String replaceInBoard(Player player, String s) {
        s = s.replace("{player}", player.getName())
                .replace("{health}", df.format(player.getHealth()))
                .replace("{ping}", String.valueOf(player.getPing()));
        return replace(player, s);
    }

    public String replaceInMessage(Player player, String s, String first, String second) {
        s = s.replace("{0}", first).replace("{1}", second);
        return replace(player, s);
    }

    public String replace(Player player, String s) {
        if (ConfigManager.isUsingPapi() && PlaceholderAPI.containsPlaceholders(s)) {
            return Colorizer.colorize(PlaceholderAPI.setPlaceholders(player, s));
        }
        return s;
    }
}
