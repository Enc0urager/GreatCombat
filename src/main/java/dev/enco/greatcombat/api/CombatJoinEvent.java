package dev.enco.greatcombat.api;

import dev.enco.greatcombat.manager.User;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Event called when one player joins another player's existing combat.
 * This occurs when a player not in combat attacks or is attacked by a player already in combat.
 *
 * @see CombatDamageEvent
 */
@Getter
public class CombatJoinEvent extends CombatDamageEvent {
    private static final HandlerList handlers = new HandlerList();
    /**
     * Indicates whether the damager was the player joining combat.
     */
    private final boolean damagerJoiner;

    /**
     * Constructs a new CombatJoinEvent without specific damage cause.
     *
     * @param damager User who dealt damage
     * @param target User who received damage
     * @param damagerJoiner True if damager was the joiner, false if target was the joiner
     */
    public CombatJoinEvent(User damager, User target, boolean damagerJoiner) {
        super(damager, target);
        this.damagerJoiner = damagerJoiner;
    }

    /**
     * Constructs a new CombatJoinEvent with specific damage cause.
     *
     * @param damager User who dealt damage
     * @param target User who received damage
     * @param damagerJoiner True if damager was the joiner, false if target was the joiner
     * @param cause Specific cause of damage
     */
    public CombatJoinEvent(User damager, User target, boolean damagerJoiner, EntityDamageEvent.DamageCause cause) {
        super(damager, target, cause);
        this.damagerJoiner = damagerJoiner;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}