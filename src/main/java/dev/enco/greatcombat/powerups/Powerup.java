package dev.enco.greatcombat.powerups;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for check and disable specific powerups for players.
 */
public interface Powerup {
    /**
     * Checks if the specified player currently has the powerup active.
     * This method should verify the presence of the powerup effect without modifying
     *
     * @param player the player whose powerup should be checked
     * @return true if the player has active powerup, false otherwise
     */
    boolean hasPowerup(@NotNull Player player);
    /**
     * Disables a specific powerup for the given player.
     * This method should handle the logic to disable the powerup effect
     *
     * @param player the player whose powerup should be disabled
     */
    void disablePowerup(@NotNull Player player);
}
