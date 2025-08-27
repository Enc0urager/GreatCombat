package dev.enco.greatcombat.powerups;

import com.google.common.collect.ImmutableSet;
import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Locale;
import dev.enco.greatcombat.powerups.impl.CMI;
import dev.enco.greatcombat.powerups.impl.EssentialsX;
import dev.enco.greatcombat.powerups.impl.Vanilla;
import dev.enco.greatcombat.utils.logger.Logger;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for managing powerup-related operations in the combat
 * Provides methods for powerup transformation, server manager setup, and powerup checking/disabling.
 */
@UtilityClass @Getter
public class PowerupsManager {
    private ServerManager serverManager;

    /**
     * Transforms a list of string powerup names into PowerupType enum values.
     *
     * @param pw List of powerup type names as strings
     * @return List of validated PowerupType enum values
     */
    public List<PowerupType> transform(List<String> pw) {
        final Locale locale = ConfigManager.getLocale();
        List<PowerupType> pws = new ArrayList<>();
        for (var str : pw) {
            try {
                pws.add(PowerupType.valueOf(str));
            } catch (IllegalArgumentException e) {
                Logger.warn(MessageFormat.format(locale.powerupTypeDoesNotExist(), str));
            }
        }
        return pws;
    }

    /**
     * Sets the server manager implementation directly.
     *
     * @param manager The ServerManager implementation to use
     */
    public void setServerManager(ServerManager manager) {
        serverManager = manager;
    }

    /**
     * Sets up the server manager implementation based on the specified manager name.
     * Initializes all powerup types with the appropriate checker and disabler implementations.
     *
     * @param manager The name of the server manager to use
     */
    public void setServerManager(String manager) {
        var pm = Bukkit.getPluginManager();
        var combatManager = GreatCombat.getInstance().getCombatManager();
        switch (manager) {
            case "CMI": {
                var cmi = new CMI(combatManager);
                serverManager = cmi;
                pm.registerEvents(cmi, GreatCombat.getInstance());
                serverManager.setup();
                break;
            }
            case "Essentials": {
                var ess = new EssentialsX(combatManager);
                serverManager = ess;
                pm.registerEvents(ess, GreatCombat.getInstance());
                serverManager.setup();
                break;
            }
            default: {
                serverManager = new Vanilla();
                serverManager.setup();
                break;
            }
        }
        for (PowerupType type : PowerupType.values()) {
            type.initialize(serverManager);
        }
    }

    /**
     * Checks if a player has any of the specified powerups active.
     *
     * @param player The player to check for powerups
     * @param checks Set of powerup types to check for
     * @return true if player has any of the specified powerups active, false otherwise
     */
    public boolean hasPowerups(Player player, ImmutableSet<PowerupType> checks) {
        if (player.hasPermission("greatcombat.powerups.bypass")) return false;
        for (PowerupType check : checks) {
            if (check.getPowerupChecker().hasPowerup(player)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Disables the specified powerups for a player.
     *
     * @param player The player to disable powerups for
     * @param checks Set of powerup types to disable
     */
    public void disablePowerups(Player player, ImmutableSet<PowerupType> checks) {
        for (PowerupType check : checks) {
            check.getPowerupDisabler().disablePowerup(player);
        }
    }
}
