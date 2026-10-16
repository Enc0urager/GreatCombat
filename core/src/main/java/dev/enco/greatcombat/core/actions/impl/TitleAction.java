package dev.enco.greatcombat.core.actions.impl;

import dev.enco.greatcombat.core.actions.Action;
import dev.enco.greatcombat.core.actions.context.TitleContext;
import dev.enco.greatcombat.core.utils.Placeholders;
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
