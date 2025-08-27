package dev.enco.greatcombat.api;

import dev.enco.greatcombat.manager.User;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Event calls when two players who are already in separate combats engage each other.
 *
 * @see CombatDamageEvent
 */
public class CombatMergeEvent extends CombatDamageEvent {
    private static final HandlerList handlers = new HandlerList();

    /**
     * Constructs a new CombatMergeEvent without specific damage cause.
     *
     * @param damager User who dealt damage
     * @param target User who received damage
     */
    public CombatMergeEvent(User damager, User target) {
        super(damager, target);
    }

    /**
     * Constructs a new CombatMergeEvent with specific damage cause.
     *
     * @param damager User who dealt damage
     * @param target User who received damage
     * @param cause Specific cause of damage
     */
    public CombatMergeEvent(User damager, User target, EntityDamageEvent.DamageCause cause) {
        super(damager, target, cause);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
