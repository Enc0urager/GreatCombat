package dev.enco.greatcombat.restrictions.cooldowns;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.restrictions.CheckedMeta;
import dev.enco.greatcombat.restrictions.InteractionHandler;
import dev.enco.greatcombat.restrictions.meta.MetaManager;
import dev.enco.greatcombat.utils.EnumUtils;
import dev.enco.greatcombat.utils.ItemSerializer;
import dev.enco.greatcombat.utils.Logger;
import dev.enco.greatcombat.utils.colorizer.Colorizer;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class CooldownManager {
    private final Map<CooldownItem, Cache<UUID, Long>> itemsCooldowns = new HashMap<>();

    public CooldownItem getCooldownItem(ItemStack i) {
        return itemsCooldowns.keySet().stream()
                .filter(item -> MetaManager.isSimilar(item.itemStack(),i, item.checkedMetas()))
                .findFirst()
                .orElse(null);
    }

    public void setupCooldownItems(FileConfiguration config) {
        var section = config.getConfigurationSection("items-cooldowns");
        for (var key : section.getKeys(false)) {
            var itemSection = section.getConfigurationSection(key);

            var handlers = EnumUtils.toEnumSet(
                    itemSection.getStringList("handlers"),
                    InteractionHandler.class,
                    handler -> Logger.warn("Обработчик " + handler + " не существует")
            );

            var metas = EnumUtils.toEnumSet(
                    itemSection.getStringList("checked-meta"),
                    CheckedMeta.class,
                    meta -> Logger.warn("У предметов нельзя проверять " + meta)
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
        if (item.setMaterialCooldown())
            // Костыль, сделали фигню с кулдаунами на эндер-жемчуг
            Bukkit.getScheduler().runTaskLater(GreatCombat.getInstance(), () -> {
                player.setCooldown(item.itemStack().getType(), item.time() * 20);}, 1L);
    }

    public void clearPlayerCooldowns(Player player) {
        for (var item : itemsCooldowns.keySet()) player.setCooldown(item.itemStack().getType(), 0);
    }
}
