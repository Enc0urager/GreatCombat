package dev.enco.greatcombat.actions.impl;

import dev.enco.greatcombat.actions.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BroadcastActionBarAction implements Action {
    @Override
    public void execute(@NotNull Player player, String context) {
        for (var p : Bukkit.getOnlinePlayers()) {
            p.sendActionBar(context);
        }
    }
}
