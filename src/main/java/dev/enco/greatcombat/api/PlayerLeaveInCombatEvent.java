package dev.enco.greatcombat.api;

import dev.enco.greatcombat.manager.User;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event calls when a player leaves the server while in combat.
 *
 * @see Event
 */
@Getter
public class PlayerLeaveInCombatEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    /**
     * User who left while in combat
     */
    private final User user;

    /**
     * Constructs a new PlayerLeaveInCombatEvent.
     *
     * @param user User who left
     */
    public PlayerLeaveInCombatEvent(User user) {
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
