package dev.enco.greatcombat.api;

import dev.enco.greatcombat.manager.User;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event called periodically specific player in combat.
 * This event is used to update combat-related displays (bossbars, scoreboards) and check combat expiration.
 *
 * @see Event
 */
@Getter
public class CombatTickEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    /**
     * User whose combat timer is ticking
     */
    private final User user;

    /**
     * Constructs a new CombatTickEvent.
     *
     * @param user User whose combat timer is ticking
     */
    public CombatTickEvent(User user) {
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
