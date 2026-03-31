package dev.enco.greatcombat.core.restrictions.cooldowns;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.ICooldownManager;
import dev.enco.greatcombat.api.managers.IMetaManager;
import dev.enco.greatcombat.api.managers.ITaskManager;
import dev.enco.greatcombat.api.models.ICooldownItem;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.restrictions.DefaultCheckers;
import dev.enco.greatcombat.core.restrictions.MetaHandle;
import dev.enco.greatcombat.core.restrictions.WrappedItem;
import dev.enco.greatcombat.core.utils.EnumUtils;
import dev.enco.greatcombat.core.utils.ItemUtils;
import dev.enco.greatcombat.core.utils.LangUtils;
import dev.enco.greatcombat.core.utils.colorizer.Colorizer;
import dev.enco.greatcombat.core.utils.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Singleton
public class CooldownManager implements ICooldownManager {
    private ImmutableMap<ICooldownItem, Cache<UUID, Long>> itemsCooldowns;
    private final Map<String, ICooldownItem> idsRefs = new HashMap<>();
    private final IMetaManager metaManager;
    private final ITaskManager taskManager;

    @Inject
    public CooldownManager(IMetaManager metaManager, ITaskManager taskManager, ConfigManager configManager) {
        this.metaManager = metaManager;
        this.taskManager = taskManager;
        setupCooldownItems(configManager);
    }

    @Override
    public ICooldownItem getCooldownItem(ItemStack i) {
        var wrapped = WrappedItem.wrap(i);
        for (var item : itemsCooldowns.keySet())
            if (metaManager.isSimilar(item.wrappedItem(), wrapped, item.checkedMetas()))
                return item;
        return null;
    }

    public void setupCooldownItems(ConfigManager configManager) {
        var config = configManager.getMainConfig();
        final var locale = configManager.getLocale();
        var section = config.getConfigurationSection("items-cooldowns");
        Map<ICooldownItem, Cache<UUID, Long>> temp = new HashMap<>();
        for (var key : section.getKeys(false)) {
            var itemSection = section.getConfigurationSection(key);

            var handlers = new HashSet<>(itemSection.getStringList("handlers"));

            List<String> metaKeys = itemSection.getStringList("checked-meta");
            int size = metaKeys.size();
            MetaHandle[] handles = new MetaHandle[size];

            for (int i = 0; i < size; i++) {
                handles[i] = (MetaHandle) metaManager.getByID(metaKeys.get(i));
            }

            int time = itemSection.getInt("time");
            var itemStack = ItemUtils.decode(itemSection.getString("base64"));
            var translation = Colorizer.colorize(LangUtils.getTranslation(itemSection.getString("translation"), itemStack));

            CooldownItem item = new CooldownItem(
                    WrappedItem.withMeta(itemStack),
                    translation,
                    handles,
                    handlers,
                    time,
                    itemSection.getBoolean("set-material-cooldown"),
                    new HashSet<>(itemSection.getStringList("linked-items"))
            );
            idsRefs.put(key, item);
            temp.put(item, Caffeine.newBuilder()
                    .expireAfterWrite(time, TimeUnit.SECONDS)
                    .scheduler(Scheduler.systemScheduler())
                    .removalListener(((k, v, cause) -> {
                        if (cause == RemovalCause.EXPIRED) {
                            Player player = Bukkit.getPlayer((UUID) k);
                            if (player == null || !player.isOnline()) return;
                            configManager.getMessages().onCooldownExpired().execute(player, translation);
                        }
                    }))
                    .build());
        }
        itemsCooldowns = ImmutableMap.copyOf(temp);
    }

    @Override
    public boolean hasCooldown(UUID playerUUID, ICooldownItem item) {
        return itemsCooldowns.get(item).getIfPresent(playerUUID) != null;
    }

    @Override
    public int getCooldownTime(UUID playerUUID, ICooldownItem item) {
        long startTime = itemsCooldowns.get(item).getIfPresent(playerUUID);
        long leftTime = System.currentTimeMillis() - startTime;
        long remainingTime = item.time() * 1000L - leftTime;
        return (int) (remainingTime / 1000L);
    }

    @Override
    public void putCooldown(UUID playerUUID, Player player, ICooldownItem item) {
        setCooldown(playerUUID, player, item);
        for (var id : item.linkedItems()) setCooldown(playerUUID, player, idsRefs.get(id));
    }

    private void setCooldown(UUID playerUUID, Player player, ICooldownItem item) {
        itemsCooldowns.get(item).put(playerUUID, System.currentTimeMillis());
        if (item.setMaterialCooldown())
            taskManager.getGlobalScheduler().runLater(() ->
                    player.setCooldown(
                            item.wrappedItem().itemStack().getType(), item.time() * 20
                    ),1L
            );
    }

    @Override
    public void clearPlayerCooldowns(Player player) {
        for (var entry: itemsCooldowns.entrySet()) {
            player.setCooldown(entry.getKey().wrappedItem().itemStack().getType(), 0);
            entry.getValue().invalidate(player.getUniqueId());
        }
    }
}
