package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.models.InteractionHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * This manager handles dynamic Bukkit event registration and maps them
 * to cooldown and prevention rules defined via {@link InteractionHandler}.
 */
public interface IInteractionManager extends IManager {
    /**
     * Registers a mapping between a Bukkit event class and an interaction handler.
     * <p>
     * If this is the first handler registered for the given event class,
     * the manager will automatically subscribe to this event in Bukkit.
     *
     * @param eventClass The Bukkit event class (e.g., PlayerInteractEvent.class).
     * @param handler    The handler instance containing filtering and data extraction logic.
     * @param <T>        The event type extending {@link Event}.
     */
    <T extends Event> void registerMapping(Class<T> eventClass, InteractionHandler<T> handler);

    /**
     * Creates a new instance of an interaction handler.
     *
     * @param name      Unique name of the handler (used in configs, e.g., "RIGHT_CLICK_AIR").
     * @param predicate Condition under which the handler is considered active for the event.
     * @param playerExt Function to extract the {@link Player} from the event object.
     * @param itemExt   Function to extract the {@link ItemStack} from the event object.
     * @param <T>       The event type.
     * @return          A new {@link InteractionHandler} instance.
     */
    <T extends Event> InteractionHandler<T> newHandler(String name, Predicate<T> predicate, Function<T, Player> playerExt, Function<T, ItemStack> itemExt);

    /**
     * Returns a stream of all registered handlers for a specific event type.
     *
     * @param event The event object.
     * @param <T>   The event type.
     * @return      A Stream of handlers mapped to the class of the provided event.
     */
    <T extends Event> Stream<InteractionHandler<T>> getHandlers(T event);

    /**
     * Returns a stream of handlers where the condition ({@link InteractionHandler#predicate()}) is met.
     * <p>
     * Used to determine which specific action occurred (e.g., Right Click vs Left Click)
     * within a single base Bukkit event.
     *
     * @param event The event object.
     * @param <T>   The event type.
     * @return      A Stream of active handlers for the current event state.
     */
    <T extends Event> Stream<InteractionHandler<T>> getPredicatedHandlers(T event);

    /**
     * Executes a specific action (consumer) for all active handlers of the event.
     *
     * @param event    The event object.
     * @param consumer The action to perform on the event.
     * @param <T>      The event type.
     */
    <T extends Event> void handle(T event, Consumer<T> consumer);

    /**
     * Registers the default interactions provided by the core.
     * <p>
     * This should be called during manager initialization to initialize base rules
     */
    void registerDefaults();
}
