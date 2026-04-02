package dev.enco.greatcombat.core.utils;

import dev.enco.greatcombat.core.utils.colorizer.Colorizer;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utility class for handling placeholder replacement in strings.
 * Supports both built-in placeholders and PlaceholderAPI placeholders.
 */
@UtilityClass
public class Placeholders {
    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat df = new DecimalFormat("#.#", symbols);
    @Setter private static boolean usingPapi;

    /**
     * Replaces placeholders in a string specifically for scoreboard display.
     *
     * @param player The player to use for placeholder replacement
     * @param s The string containing placeholders to replace
     * @return The string with all placeholders replaced and colorized
     */
    public String replaceInBoard(Player player, String s) {
        return replaceInMessage(player, s, player.getName(), df.format(player.getHealth()), player.getPing());
    }

    /**
     * Replaces placeholders in a message string with formatted replacements.
     * First formats the message with replacements, then processes placeholders.
     *
     * @param player The player to use for placeholder replacement
     * @param s The message format string containing placeholders
     * @param replacement Variable arguments for message formatting
     * @return The formatted string with all placeholders replaced and colorized
     */
    public String replaceInMessage(Player player, String s, Object... replacement) {
        return replace(player, parseLocals(s, replacement));
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
        if (usingPapi && PlaceholderAPI.containsPlaceholders(s)) {
            return Colorizer.colorize(PlaceholderAPI.setPlaceholders(player, s));
        }
        return s;
    }

    /**
     * Processes local placeholders in the form of {} or {index}.
     * {} is replaced sequentially, {index} uses the specific argument.
     *
     * @param text The template string
     * @param replacement The arguments to replace
     * @return The string with local placeholders replaced
     */
    public String parseLocals(String text, Object... replacement) {
        if (text == null || replacement == null || replacement.length == 0) return text;

        int length = text.length();
        int rLength = replacement.length;
        if (length < 3) return text;

        int argIndex = 0;
        StringBuilder result = new StringBuilder(length + 16);

        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if (c == '{') {
                int start = i + 1;
                int end = findClosing(text, start);
                if (end == -1) {
                    result.append(c);
                    continue;
                }

                int index = resolveIndex(text, start, end, argIndex);

                if (index < rLength) {
                    result.append(replacement[index]);
                    if (start == end) argIndex++;
                } else {
                    result.append(text, i, end + 1);
                }

                i = end;
                continue;
            }

            result.append(c);
        }

        return result.toString();
    }

    /**
     * Finds the position of the closing brace '}' starting from 'from'.
     *
     * @param text The template string
     * @param from Starting index to search from
     * @return The index of the closing brace, or -1 if not found
     */
    private int findClosing(String text, int from) {
        for (int i = from; i < text.length(); i++) {
            if (text.charAt(i) == '}') return i;
        }
        return -1;
    }

    /**
     * Resolves the argument index from a placeholder.
     * Returns fallback (sequential index) if he placeholder is empty or contains non-digit characters.
     *
     * @param text The template string
     * @param start Start index of the placeholder content
     * @param end End index of the placeholder content
     * @param fallback The sequential index to use for {}
     * @return The resolved argument index
     */
    private int resolveIndex(String text, int start, int end, int fallback) {
        if (start == end) return fallback;

        int value = 0;
        for (int i = start; i < end; i++) {
            char c = text.charAt(i);
            if (c < '0' || c > '9') return fallback;
            value = value * 10 + (c - '0');
        }

        return value;
    }
}
