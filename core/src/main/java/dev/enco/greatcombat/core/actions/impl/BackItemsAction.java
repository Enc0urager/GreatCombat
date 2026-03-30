package dev.enco.greatcombat.core.actions.impl;

import dev.enco.greatcombat.core.actions.Action;
import dev.enco.greatcombat.core.actions.context.StringContext;
import dev.enco.greatcombat.core.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackItemsAction implements Action<StringContext> {
    @Override
    public void execute(@NotNull Player player, @Nullable StringContext context, @Nullable String... replacement) {
        ItemUtils.backItems(player);
    }
}
