package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.models.ICooldownItem;
import dev.enco.greatcombat.api.models.IWrappedItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface ICooldownManager extends IManager {
    /**
     * Retrieves the CooldownItem associated with the given ItemStack.
     *
     * @param i The ItemStack to check for cooldown
     * @return CooldownItem if found, null otherwise
     */
    @Nullable ICooldownItem getCooldownItem(@NotNull ItemStack i);

    /**
     * Retrieves the CooldownItem associated with the given IWrappedItem.
     *
     * @param i The IWrappedItem to check for cooldown
     * @return CooldownItem if found, null otherwise
     */
    @Nullable ICooldownItem getCooldownItem(@NotNull IWrappedItem i);

    /**
     * Checks if a player has an active cooldown for the specified item.
     *
     * @param playerUUID The UUID of the player to check
     * @param item       The CooldownItem to check for cooldown
     * @return true if player has active cooldown, false otherwise
     */
    boolean hasCooldown(@NotNull UUID playerUUID,
                        @NotNull ICooldownItem item);

    /**
     * Gets the remaining cooldown time in seconds for a player and item.
     *
     * @param playerUUID The UUID of the player to check
     * @param item       The CooldownItem to get cooldown time for
     * @return Remaining cooldown time in seconds
     */
    int getCooldownTime(@NotNull UUID playerUUID,
                        @NotNull ICooldownItem item);

    /**
     * Puts a player on cooldown for the specified item.
     *
     * @param playerUUID The UUID of the player to put on cooldown
     * @param player     The Player for material cooldown if enabled
     * @param item       The CooldownItem to set cooldown for
     */
    void putCooldown(@NotNull UUID playerUUID,
                     @NotNull Player player,
                     @NotNull ICooldownItem item);

    /**
     * Clears all item cooldowns for a player.
     *
     * @param player The player to clear cooldowns for
     */
    void clearPlayerCooldowns(@NotNull Player player);
}
