package dev.enco.greatcombat.actions.impl;

import dev.enco.greatcombat.actions.Action;
import dev.enco.greatcombat.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BroadcastSoundAction implements Action {
    @Override
    public void execute(@NotNull Player player, String context) {
        var args = context.split(";");
        Sound sound = null;

        try {
            if (args.length >= 1) {
                sound = Sound.valueOf(args[0].toUpperCase());
            } else {
                Logger.warn("Sound is null");
            }
        } catch (IllegalArgumentException e) {
            Logger.warn("Sound " + args[0] + " is not available");
        }

        float volume = 0;
        float pitch = 0;

        try {
            volume = args.length > 1 ? Float.parseFloat(args[1]) : 1;
            pitch = args.length > 2 ? Float.parseFloat(args[2]) : 1;
        } catch (NumberFormatException e) {
            Logger.warn("Volume and pitch must be a number");
        }

        for (var p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), sound, volume, pitch);
        }
    }
}
