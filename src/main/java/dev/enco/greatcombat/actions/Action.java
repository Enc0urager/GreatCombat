package dev.enco.greatcombat.actions;

import dev.enco.greatcombat.actions.context.Context;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Action<C extends Context> {
    void execute(@NotNull Player player, @Nullable C context, @Nullable String... replacement);
}
