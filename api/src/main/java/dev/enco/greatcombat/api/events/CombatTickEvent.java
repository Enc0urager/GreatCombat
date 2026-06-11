package dev.enco.greatcombat.api.events;

import dev.enco.greatcombat.api.models.IUser;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event called periodically specific player in combat.
 * This event is used to update combat-related displays (bossbars, scoreboards) and check combat expiration.
 *
 * @see Event
 */
@Getter
public class CombatTickEvent extends Event {
    private static final @NotNull HandlerList handlers = new HandlerList();
    /**
     * User whose combat timer is ticking
     */
    private final @NotNull IUser user;

    /**
     * Constructs a new CombatTickEvent.
     *
     * @param user User whose combat timer is ticking
     */
    public CombatTickEvent(@NotNull IUser user) {
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
