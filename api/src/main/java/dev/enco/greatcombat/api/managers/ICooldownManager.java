package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.models.ICooldownItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface ICooldownManager extends IManager {
    /**
     * Retrieves the CooldownItem associated with the given ItemStack.
     *
     * @param i The ItemStack to check for cooldown
     * @return CooldownItem if found, null otherwise
     */
    ICooldownItem getCooldownItem(ItemStack i);

    /**
     * Checks if a player has an active cooldown for the specified item.
     *
     * @param playerUUID The UUID of the player to check
     * @param item The CooldownItem to check for cooldown
     * @return true if player has active cooldown, false otherwise
     */
    boolean hasCooldown(UUID playerUUID, ICooldownItem item);

    /**
     * Gets the remaining cooldown time in seconds for a player and item.
     *
     * @param playerUUID The UUID of the player to check
     * @param item The CooldownItem to get cooldown time for
     * @return Remaining cooldown time in seconds
     */
    int getCooldownTime(UUID playerUUID, ICooldownItem item);

    /**
     * Puts a player on cooldown for the specified item.
     *
     * @param playerUUID The UUID of the player to put on cooldown
     * @param player The Player for material cooldown if enabled
     * @param item The CooldownItem to set cooldown for
     */
    void putCooldown(UUID playerUUID, Player player, ICooldownItem item);

    /**
     * Clears all item cooldowns for a player.
     *
     * @param player The player to clear cooldowns for
     */
    void clearPlayerCooldowns(Player player);
}
