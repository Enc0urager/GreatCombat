package dev.enco.greatcombat.actions.impl;

import dev.enco.greatcombat.actions.Action;
import dev.enco.greatcombat.actions.context.MaterialContext;
import dev.enco.greatcombat.manager.ItemsManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RemoveItemAction implements Action<MaterialContext> {
    @Override
    public void execute(@NotNull Player player, @Nullable MaterialContext context, @Nullable String... replacement) {
        ItemsManager.removeItems(player, context.material());
    }
}
