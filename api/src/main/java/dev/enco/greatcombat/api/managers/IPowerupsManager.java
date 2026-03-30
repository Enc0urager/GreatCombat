package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.models.PowerupProvider;
import dev.enco.greatcombat.api.models.PowerupType;
import org.bukkit.entity.Player;

import java.util.EnumSet;

public interface IPowerupsManager extends IManager {
    /**
     * Sets the powerup provider implementation directly.
     *
     * @param provider The PowerupProvider implementation to use
     */
    void setPowerupProvider(PowerupProvider provider);

    /**
     * Checks if a player has any of the specified powerups active.
     *
     * @param player The player to check for powerups
     * @param checks Set of powerup types to check for
     * @return true if player has any of the specified powerups active, false otherwise
     */
    boolean hasPowerups(Player player, EnumSet<PowerupType> checks);

    /**
     * Disables the specified powerups for a player.
     *
     * @param player The player to disable powerups for
     * @param checks Set of powerup types to disable
     */
    void disablePowerups(Player player, EnumSet<PowerupType> checks);
}
