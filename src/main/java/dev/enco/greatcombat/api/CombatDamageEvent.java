package dev.enco.greatcombat.api;

import dev.enco.greatcombat.manager.User;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Abstract base class for all damage-relates events.
 * Provides common functionality for damage events between two players in combat.
 *
 * @see Cancellable
 * @see Event
 */
@Getter
public abstract class CombatDamageEvent extends Event implements Cancellable {
    protected final User damager;
    protected final User target;
    protected EntityDamageEvent.DamageCause cause;
    protected boolean cancelled;

    /**
     * Constructs a new CombatDamageEvent without specific damage cause.
     *
     * @param damager User who dealt damage
     * @param target User who received damage
     */
    public CombatDamageEvent(User damager, User target) {
        this.damager = damager;
        this.target = target;
    }

    /**
     * Constructs a new CombatDamageEvent with specific damage cause.
     *
     * @param damager User who dealt damage
     * @param target User who received damage
     * @param cause Specific cause of damage
     */
    public CombatDamageEvent(User damager, User target, EntityDamageEvent.DamageCause cause) {
        this.damager = damager;
        this.target = target;
        this.cause = cause;
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
