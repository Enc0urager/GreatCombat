package dev.enco.greatcombat.utils;

import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.utils.colorizer.Colorizer;
import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.util.Locale;

/**
 * Utility class for handling placeholder replacement in strings.
 * Supports both built-in placeholders and PlaceholderAPI placeholders.
 */
@UtilityClass
public class Placeholders {
    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat df = new DecimalFormat("#.#", symbols);

    /**
     * Replaces placeholders in a string specifically for scoreboard display.
     *
     * @param player The player to use for placeholder replacement
     * @param s The string containing placeholders to replace
     * @return The string with all placeholders replaced and colorized
     */
    public String replaceInBoard(Player player, String s) {
        s = s.replace("{player}", player.getName())
                .replace("{health}", df.format(player.getHealth()))
                .replace("{ping}", String.valueOf(player.getPing()));
        return replace(player, s);
    }

    /**
     * Replaces placeholders in a message string with formatted replacements.
     * First formats the message with replacements, then processes placeholders.
     *
     * @param player The player to use for placeholder replacement
     * @param s The message format string containing placeholders
     * @param replacement Variable arguments for message formatting
     * @return The formatted string with all placeholders replaced and colorized
     *
     * @see MessageFormat#format(String, Object...)
     */
    public String replaceInMessage(Player player, String s, String... replacement) {
        return replace(player, MessageFormat.format(s, replacement));
    }

    /**
     * Replacement method that handles PlaceholderAPI placeholders.
     * Detects and uses PlaceholderAPI if enabled and placeholders are present.
     *
     * @param player The player to use for placeholder replacement
     * @param s The string containing placeholders to replace
     * @return The string with all placeholders replaced and colorized
     */
    public String replace(Player player, String s) {
        if (ConfigManager.isUsingPapi() && PlaceholderAPI.containsPlaceholders(s)) {
            return Colorizer.colorize(PlaceholderAPI.setPlaceholders(player, s));
        }
        return s;
    }
}
