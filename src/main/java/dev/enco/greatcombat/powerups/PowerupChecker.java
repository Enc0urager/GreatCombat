package dev.enco.greatcombat.powerups;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for check specific powerups for players.
 *
 * @deprecated This interface is deprecated and will be removed in version 1.8
 */
@Deprecated(since = "1.7.4")
public interface PowerupChecker {
    /**
     * Checks if the specified player currently has the powerup active.
     * This method should verify the presence of the powerup effect without modifying
     *
     * @param player the player whose powerup should be checked
     * @return true if the player has active powerup, false otherwise
     */
    boolean hasPowerup(@NotNull Player player);
}
