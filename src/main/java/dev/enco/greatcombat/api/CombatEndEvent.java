package dev.enco.greatcombat.api;

import dev.enco.greatcombat.manager.User;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event called when a player's combat mode ends.
 * This occurs when the combat timer expires or combat is otherwise terminated.
 *
 * @see Event
 */
@Getter
public class CombatEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final User user;

    /**
     * Constructs a new CombatEndEvent.
     *
     * @param user User whose combat has ended
     */
    public CombatEndEvent(User user) {
        this.user = user;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
