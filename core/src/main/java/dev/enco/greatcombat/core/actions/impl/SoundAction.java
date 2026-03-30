package dev.enco.greatcombat.core.actions.impl;

import dev.enco.greatcombat.core.actions.Action;
import dev.enco.greatcombat.core.actions.context.SoundContext;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundAction implements Action<SoundContext> {
    @Override
    public void execute(@NotNull Player player, SoundContext context, String... replacement) {
        player.playSound(player.getLocation(), context.sound(), context.volume(), context.pitch());
    }
}