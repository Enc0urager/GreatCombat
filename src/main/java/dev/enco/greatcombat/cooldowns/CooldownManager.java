package dev.enco.greatcombat.cooldowns;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.enco.greatcombat.utils.ItemSerializer;
import dev.enco.greatcombat.utils.Logger;
import dev.enco.greatcombat.utils.colorizer.Colorizer;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class CooldownManager {
    private final Map<CooldownItem, Cache<UUID, Long>> itemsCooldowns = new HashMap<>();

    public CooldownItem getCooldownItem(ItemStack i) {
        return itemsCooldowns.keySet().stream()
                .filter(item -> item.itemStack().isSimilar(i))
                .findFirst()
                .orElse(null);
    }

    public void setupCooldownItems(FileConfiguration config) {
        var section = config.getConfigurationSection("items-cooldowns");
        for (var key : section.getKeys(false)) {
            var itemSection = section.getConfigurationSection(key);
            var handlers = new ArrayList<InteractionHandler>();

            for (var handler : itemSection.getStringList("handlers")) try {
                handlers.add(InteractionHandler.valueOf(handler));
            } catch (IllegalArgumentException e) {
                Logger.warn("Обработчик " + handler + " не существует");
            }

            int time = itemSection.getInt("time");

            var item = new CooldownItem(
                    ItemSerializer.decode(itemSection.getString("base64")),
                    Colorizer.colorize(itemSection.getString("translation")),
                    handlers,
                    time,
                    itemSection.getBoolean("set-material-cooldown")
            );
            itemsCooldowns.put(item, Caffeine.newBuilder()
                    .expireAfterWrite(time, TimeUnit.SECONDS)
                    .build());
        }
    }

    public boolean hasCooldown(UUID playerUUID, CooldownItem item) {
        return itemsCooldowns.get(item).getIfPresent(playerUUID) != null;
    }

    public int getCooldownTime(UUID playerUUID, CooldownItem item) {
        long startTime = itemsCooldowns.get(item).getIfPresent(playerUUID);
        long leftTime = System.currentTimeMillis() - startTime;
        long remainingTime = item.time() * 1000L - leftTime;
        return (int) (remainingTime / 1000L);
    }

    public void putCooldown(UUID playerUUID, Player player, CooldownItem item) {
        itemsCooldowns.get(item).put(playerUUID, System.currentTimeMillis());
        if (item.setMaterialCooldown()) player.setCooldown(item.itemStack().getType(), item.time() * 20);
    }
}
