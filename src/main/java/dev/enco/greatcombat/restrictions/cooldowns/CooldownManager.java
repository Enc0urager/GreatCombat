package dev.enco.greatcombat.restrictions.cooldowns;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.restrictions.CheckedMeta;
import dev.enco.greatcombat.restrictions.InteractionHandler;
import dev.enco.greatcombat.restrictions.meta.MetaManager;
import dev.enco.greatcombat.utils.EnumUtils;
import dev.enco.greatcombat.utils.ItemSerializer;
import dev.enco.greatcombat.utils.colorizer.Colorizer;
import dev.enco.greatcombat.utils.logger.Logger;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for managing item cooldowns during combat.
 * Provides functionality to set, check, and clear cooldowns for specific items.
 */
@UtilityClass
public class CooldownManager {
    private final Map<CooldownItem, Cache<UUID, Long>> itemsCooldowns = new HashMap<>();

    /**
     * Retrieves the CooldownItem associated with the given ItemStack.
     *
     * @param i The ItemStack to check for cooldown
     * @return CooldownItem if found, null otherwise
     */
    public CooldownItem getCooldownItem(ItemStack i) {
        return itemsCooldowns.keySet().stream()
                .filter(item -> MetaManager.isSimilar(item.itemStack(),i, item.checkedMetas()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Sets up cooldown items from the configuration file.
     *
     * @param config The configuration file containing cooldown item section
     */
    public void setupCooldownItems(FileConfiguration config) {
        final var locale = ConfigManager.getLocale();
        var section = config.getConfigurationSection("items-cooldowns");
        for (var key : section.getKeys(false)) {
            var itemSection = section.getConfigurationSection(key);

            var handlers = EnumUtils.toEnumSet(
                    itemSection.getStringList("handlers"),
                    InteractionHandler.class,
                    handler -> Logger.warn(MessageFormat.format(locale.handlerDoesNotExist(), handler))
            );

            var metas = EnumUtils.toEnumSet(
                    itemSection.getStringList("checked-meta"),
                    CheckedMeta.class,
                    meta -> Logger.warn(MessageFormat.format(locale.metaDoesNotExist(), meta))
            );

            int time = itemSection.getInt("time");

            var item = new CooldownItem(
                    ItemSerializer.decode(itemSection.getString("base64")),
                    Colorizer.colorize(itemSection.getString("translation")),
                    metas,
                    handlers,
                    time,
                    itemSection.getBoolean("set-material-cooldown")
            );
            itemsCooldowns.put(item, Caffeine.newBuilder()
                    .expireAfterWrite(time, TimeUnit.SECONDS)
                    .build());
        }
    }

    /**
     * Checks if a player has an active cooldown for the specified item.
     *
     * @param playerUUID The UUID of the player to check
     * @param item The CooldownItem to check for cooldown
     * @return true if player has active cooldown, false otherwise
     */
    public boolean hasCooldown(UUID playerUUID, CooldownItem item) {
        return itemsCooldowns.get(item).getIfPresent(playerUUID) != null;
    }

    /**
     * Gets the remaining cooldown time in seconds for a player and item.
     *
     * @param playerUUID The UUID of the player to check
     * @param item The CooldownItem to get cooldown time for
     * @return Remaining cooldown time in seconds
     */
    public int getCooldownTime(UUID playerUUID, CooldownItem item) {
        long startTime = itemsCooldowns.get(item).getIfPresent(playerUUID);
        long leftTime = System.currentTimeMillis() - startTime;
        long remainingTime = item.time() * 1000L - leftTime;
        return (int) (remainingTime / 1000L);
    }

    /**
     * Puts a player on cooldown for the specified item.
     *
     * @param playerUUID The UUID of the player to put on cooldown
     * @param player The Player for material cooldown if enabled
     * @param item The CooldownItem to set cooldown for
     */
    public void putCooldown(UUID playerUUID, Player player, CooldownItem item) {
        itemsCooldowns.get(item).put(playerUUID, System.currentTimeMillis());
        if (item.setMaterialCooldown())
            Bukkit.getScheduler().runTaskLater(GreatCombat.getInstance(), () -> {
                player.setCooldown(item.itemStack().getType(), item.time() * 20);}, 1L);
    }

    /**
     * Clears all item cooldowns for a player.
     *
     * @param player The player to clear cooldowns for
     */
    public void clearPlayerCooldowns(Player player) {
        for (var item : itemsCooldowns.keySet()) player.setCooldown(item.itemStack().getType(), 0);
    }
}
