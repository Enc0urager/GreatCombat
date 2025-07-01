package dev.enco.greatcombat.actions.context;

import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Locale;
import dev.enco.greatcombat.utils.logger.Logger;
import org.bukkit.Sound;

import java.text.MessageFormat;

public record SoundContext(
        Sound sound,
        float volume,
        float pitch
) implements Context {
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
