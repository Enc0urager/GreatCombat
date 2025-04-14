package dev.enco.greatcombat.cooldowns;

import java.util.*;
import java.util.concurrent.TimeUnit;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.enco.greatcombat.utils.Colorizer;
import dev.enco.greatcombat.utils.Logger;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

@UtilityClass
public class CooldownManager {
    private final Map<CooldownItem, Cache<UUID, Long>> itemsCooldowns = new HashMap<>();

    public CooldownItem getCooldownItem(Material material) {
        return itemsCooldowns.keySet().stream()
                .filter(item -> item.material().equals(material))
                .findFirst()
                .orElse(null);
    }

    public void setupCooldownItems(FileConfiguration config) {
        var section = config.getConfigurationSection("items-cooldowns");
        for (var key : section.getKeys(false)) {
            Material material = null;
            try {
                material = Material.valueOf(key.toUpperCase());
            } catch (IllegalArgumentException e) {
                Logger.warn("Material " + key + " is not available, path " + section.getCurrentPath());
            }
            var parts = section.getString(key).split(";");
            if (parts.length < 3) {
                Logger.warn("Write items like MATERIAL: TRANSLATION;TIME;HANDLERS");
                continue;
            }
            int time = 0;
            try {
                time = Integer.valueOf(parts[1]);
            } catch (NumberFormatException e) {
                Logger.warn("Cooldown time will be a number");
            }
            List<InteractionHandler> handlers = new ArrayList<>();
            var handlersString = parts[2].split(",");
            Arrays.stream(handlersString).toList().forEach(handlerStr -> {
                InteractionHandler handler;
                try {
                    handler = InteractionHandler.valueOf(handlerStr);
                } catch (IllegalArgumentException e) {
                    handler = InteractionHandler.CONSUME;
                    Logger.warn("Handler " + handlerStr + " is not available, using CONSUME");
                }
                handlers.add(handler);
            });
            var item = new CooldownItem(
                    material,
                    Colorizer.colorize(parts[0]),
                    handlers,
                    time
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

    public void putCooldown(UUID playerUUID, CooldownItem item) {
        itemsCooldowns.get(item).put(playerUUID, System.currentTimeMillis());
    }
}
