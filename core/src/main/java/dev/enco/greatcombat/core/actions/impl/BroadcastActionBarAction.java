package dev.enco.greatcombat.core.actions.impl;

import dev.enco.greatcombat.core.actions.Action;
import dev.enco.greatcombat.core.actions.context.StringContext;
import dev.enco.greatcombat.core.utils.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BroadcastActionBarAction implements Action<StringContext> {
    @Override
    public void execute(@NotNull Player player, StringContext context, String... replacement) {
        String message = Placeholders.replaceInMessage(player, context.string(), replacement);
        for (var p : Bukkit.getOnlinePlayers()) {
            p.sendActionBar(message);
        }
    }
}
