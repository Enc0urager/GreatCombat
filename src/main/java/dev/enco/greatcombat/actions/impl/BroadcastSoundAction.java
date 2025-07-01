package dev.enco.greatcombat.actions.impl;

import dev.enco.greatcombat.actions.Action;
import dev.enco.greatcombat.actions.context.SoundContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BroadcastSoundAction implements Action<SoundContext> {
    @Override
    public void execute(@NotNull Player player, SoundContext context, String... replacement) {
        for (var p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), context.sound(), context.volume(), context.pitch());
        }
    }
}
