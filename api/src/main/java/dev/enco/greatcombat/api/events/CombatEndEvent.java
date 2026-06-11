package dev.enco.greatcombat.api.events;

import dev.enco.greatcombat.api.models.IUser;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event called when a player's combat mode ends.
 * This occurs when the combat timer expires or combat is otherwise terminated.
 *
 * @see Event
 */
@Getter
public class CombatEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final @NotNull IUser user;

    /**
     * Constructs a new CombatEndEvent.
     *
     * @param user User whose combat has ended
     */
    public CombatEndEvent(@NotNull IUser user) {
        this.user = user;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }
}
