package dev.enco.greatcombat.actions.impl;

import dev.enco.greatcombat.actions.Action;
import dev.enco.greatcombat.actions.context.StringContext;
import dev.enco.greatcombat.utils.Placeholders;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageAction implements Action<StringContext> {
    @Override
    public void execute(@NotNull Player player, StringContext context, String... replacement) {
        player.sendMessage(Placeholders.replaceInMessage(player, context.string(), replacement));
    }
}
