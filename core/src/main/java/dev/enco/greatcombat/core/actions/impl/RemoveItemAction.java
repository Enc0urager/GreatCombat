package dev.enco.greatcombat.core.actions.impl;

import dev.enco.greatcombat.core.actions.Action;
import dev.enco.greatcombat.core.actions.context.MaterialContext;
import dev.enco.greatcombat.core.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RemoveItemAction implements Action<MaterialContext> {
    @Override
    public void execute(@NotNull Player player, @Nullable MaterialContext context, @Nullable String... replacement) {
        ItemUtils.removeItems(player, context.material());
    }
}
