package dev.enco.greatcombat.actions.impl;

import dev.enco.greatcombat.actions.Action;
import dev.enco.greatcombat.actions.context.StringContext;
import dev.enco.greatcombat.manager.ItemsManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackItemsAction implements Action<StringContext> {
    @Override
    public void execute(@NotNull Player player, @Nullable StringContext context, @Nullable String... replacement) {
        ItemsManager.backItems(player);
    }
}
