package dev.enco.greatcombat.actions.context;

import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Locale;
import dev.enco.greatcombat.utils.logger.Logger;
import org.bukkit.Sound;

import java.text.MessageFormat;

/**
 * Context implementation for sound playback actions.
 * Contains all parameters required to play a sound effect.
 *
 * @param sound Bukkit Sound enum value to play
 * @param volume The volume of the sound
 * @param pitch The pitch of the sound
 */
public record SoundContext(
        Sound sound,
        float volume,
        float pitch
) implements Context {
    /**
     * Validates and parses a SoundContext from a formatted string.
     * Expected format: "sound_name;volume;pitch"
     * Volume and pitch are optional and default to 1.0 if not specified.
     *
     * @param s The formatted string to parse
     * @return New SoundContext with parsed parameters, or null if validation fails
     */
    public static SoundContext validate(String s) {
        final Locale locale = ConfigManager.getLocale();
        var args = s.split(";");
        Sound sound = null;
        try {
            if (args.length >= 1) {
                sound = Sound.valueOf(args[0].toUpperCase());
            } else {
                Logger.warn(locale.nullSound());
                return null;
            }
        } catch (IllegalArgumentException e) {
            Logger.warn(MessageFormat.format(locale.soundDoesNotExist(), args[0]));
        }
        try {
            float volume = args.length > 1 ? Float.parseFloat(args[1]) : 1;
            float pitch = args.length > 2 ? Float.parseFloat(args[2]) : 1;
            return new SoundContext(sound, volume, pitch);
        } catch (NumberFormatException e) {
            Logger.warn(locale.volumeAndPitchError());
        }
        return null;
    }
}
