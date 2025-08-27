package dev.enco.greatcombat.api;

import dev.enco.greatcombat.manager.User;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Event calls when two players who are already in combat with each other continue fighting.
 * This event refreshes the combat timer but doesn't change the combat state.
 *
 * @see CombatDamageEvent
 */
public class CombatContinueEvent extends CombatDamageEvent {
    private static final HandlerList handlers = new HandlerList();

    /**
     * Constructs a new CombatContinueEvent without specific damage cause.
     *
     * @param damager User who dealt damage
     * @param target User who received damage
     */
    public CombatContinueEvent(User damager, User target) {
        super(damager, target);
    }

    /**
     * Constructs a new CombatContinueEvent with specific damage cause.
     *
     * @param damager User who dealt damage
     * @param target User who received damage
     * @param cause Specific cause of damage
     */
    public CombatContinueEvent(User damager, User target, EntityDamageEvent.DamageCause cause) {
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
