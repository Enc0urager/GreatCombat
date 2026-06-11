package dev.enco.greatcombat.api.events;

import dev.enco.greatcombat.api.models.IUser;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event calls when a player leaves the server while in combat.
 *
 * @see Event
 */
@Getter
public class PlayerLeaveInCombatEvent extends Event {
    private static final @NotNull HandlerList handlers = new HandlerList();
    /**
     * User who left while in combat
     */
    private final @NotNull IUser user;

    /**
     * Constructs a new PlayerLeaveInCombatEvent.
     *
     * @param user User who left
     */
    public PlayerLeaveInCombatEvent(@NotNull IUser user) {
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
