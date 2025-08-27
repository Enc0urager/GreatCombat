package dev.enco.greatcombat.actions;

import dev.enco.greatcombat.actions.context.Context;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Generic interface for executing actions with specific contexts.
 *
 * @param <C> The type of Context required by this action
 */
public interface Action<C extends Context> {
    /**
     * Executes the action for the specified player with the given context.
     *
     * @param player The player to execute the action on
     * @param context The context containing action parameters
     * @param replacement Optional replacement values for message formatting
     */
    void execute(@NotNull Player player, @Nullable C context, @Nullable String... replacement);
}
