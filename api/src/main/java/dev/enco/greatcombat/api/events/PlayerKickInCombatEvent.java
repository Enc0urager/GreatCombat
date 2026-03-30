package dev.enco.greatcombat.api.events;

import dev.enco.greatcombat.api.models.IUser;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event calls when a player is kicked from the server while in combat.
 *
 * @see Event
 */
@Getter
public class PlayerKickInCombatEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    /**
     * User who was kicked
     */
    private final IUser user;

    /**
     * The reason for the kick
     */
    private final String reason;

    /**
     * Constructs a new PlayerKickInCombatEvent.
     *
     * @param user User who was kicked
     * @param reason The reason for the kick
     */
    public PlayerKickInCombatEvent(IUser user, String reason) {
        this.user = user;
        this.reason = reason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
