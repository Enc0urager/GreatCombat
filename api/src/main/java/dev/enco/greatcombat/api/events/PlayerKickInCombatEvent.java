package dev.enco.greatcombat.api.events;

import dev.enco.greatcombat.api.models.IUser;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event calls when a player is kicked from the server while in combat.
 *
 * @see Event
 */
@Getter
public class PlayerKickInCombatEvent extends Event {
    private static final @NotNull HandlerList handlers = new HandlerList();

    /**
     * User who was kicked
     */
    private final @NotNull IUser user;

    /**
     * The reason for the kick
     */
    private final @NotNull String reason;

    /**
     * Constructs a new PlayerKickInCombatEvent.
     *
     * @param user User who was kicked
     * @param reason The reason for the kick
     */
    public PlayerKickInCombatEvent(@NotNull IUser user,
                                   @NotNull String reason
    ) {
        this.user = user;
        this.reason = reason;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }
}
