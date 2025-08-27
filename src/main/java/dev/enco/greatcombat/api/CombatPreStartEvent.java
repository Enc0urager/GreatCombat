package dev.enco.greatcombat.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event calls before CombatDamageEvent, allowing for pre-combat checks and cancellations.
 * This event is triggered for all combat scenarios except when players are already opponents (CombatContinueEvent).
 *
 * @see Event
 * @see Cancellable
 * @see CombatDamageEvent
 */
@RequiredArgsConstructor @Getter
public class CombatPreStartEvent extends Event implements Cancellable {
    /**
     * The player who initiated the damage
     */
    private final Player damager;
    /**
     * The player who received the damage
     */
    private final Player target;
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
