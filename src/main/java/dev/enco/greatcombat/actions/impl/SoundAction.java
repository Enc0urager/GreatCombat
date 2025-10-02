package dev.enco.greatcombat.actions.impl;

import dev.enco.greatcombat.actions.Action;
import dev.enco.greatcombat.actions.context.SoundContext;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundAction implements Action<SoundContext> {
    @Override
    public void execute(@NotNull Player player, SoundContext context, String... replacement) {
        player.playSound(player.getLocation(), context.sound(), context.volume(), context.pitch());
    }
}