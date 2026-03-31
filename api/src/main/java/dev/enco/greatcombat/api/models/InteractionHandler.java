package dev.enco.greatcombat.api.models;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a specific interaction rule mapped to a Bukkit Event.
 * <p>
 * This handler defines when an interaction is active and how to extract
 * necessary data (Player and ItemStack) from the event for cooldown
 * and prevention checks.
 *
 * @param <T> The specific Bukkit {@link Event} this handler processes.
 */
public interface InteractionHandler<T extends Event> {
    /**
     * Gets the unique identifier of this interaction handler.
     * <p>
     * This name is used in configuration files (e.g., "RIGHT_CLICK_AIR")
     * to bind items to specific actions.
     *
     * @return The unique string name of the interaction.
     */
    String name();

    /**
     * Gets the condition that must be met for this handler to trigger.
     * <p>
     * For example, in a {@link org.bukkit.event.player.PlayerInteractEvent},
     * the predicate might check if the action is a right-click.
     *
     * @return A predicate to test the event.
     */
    Predicate<T> predicate();

    /**
     * Gets the function used to extract the {@link Player} from the event.
     * <p>
     * This ensures the Core can identify who performed the action regardless
     * of the event type.
     *
     * @return A function that maps the event to a Player.
     */
    Function<T, Player> playerExtractor();

    /**
     * Gets the function used to extract the {@link ItemStack} from the event.
     * <p>
     * This allows the Core to determine which item is being checked for
     * cooldowns or restrictions.
     *
     * @return A function that maps the event to an ItemStack.
     */
    Function<T, ItemStack> itemExtractor();
}
