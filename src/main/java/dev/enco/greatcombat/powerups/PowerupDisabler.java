package dev.enco.greatcombat.powerups;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for disabling specific powerups for players.
 *
 * @deprecated This interface is deprecated and will be removed in version 1.8
 */
@Deprecated(since = "1.7.4")
public interface PowerupDisabler {
    /**
     * Disables a specific powerup for the given player.
     * This method should handle the logic to disable the powerup effect
     *
     * @param player the player whose powerup should be disabled
     */
    void disablePowerup(@NotNull Player player);
}
