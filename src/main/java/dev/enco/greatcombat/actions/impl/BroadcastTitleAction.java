package dev.enco.greatcombat.actions.impl;

import dev.enco.greatcombat.actions.Action;
import dev.enco.greatcombat.actions.context.TitleContext;
import dev.enco.greatcombat.utils.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BroadcastTitleAction implements Action<TitleContext> {
    @Override
    public void execute(@NotNull Player player, TitleContext context, String... replacement) {
        String title = Placeholders.replaceInMessage(player, context.title(), replacement);
        String subtitle = Placeholders.replaceInMessage(player, context.subtitle(), replacement);
        for (var p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(title, subtitle, context.fadeIn(), context.fadeIn(), context.fadeOut());
        }
    }
}
