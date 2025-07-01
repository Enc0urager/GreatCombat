package dev.enco.greatcombat.actions.impl;

import dev.enco.greatcombat.actions.Action;
import dev.enco.greatcombat.actions.context.TitleContext;
import dev.enco.greatcombat.utils.Placeholders;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TitleAction implements Action<TitleContext> {
    @Override
    public void execute(@NotNull Player player, TitleContext context, String... replacement) {
        player.sendTitle(Placeholders.replaceInMessage(player, context.title(), replacement),
                Placeholders.replaceInMessage(player, context.subtitle(), replacement),
                context.fadeIn(), context.fadeIn(), context.fadeOut());
    }
}
