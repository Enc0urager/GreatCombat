package dev.enco.greatcombat.manager;

import dev.enco.greatcombat.utils.ItemUtils;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@UtilityClass
public class ItemsManager {
    private final Map<UUID, EnumMap<Material, ItemStack[]>> stored = new HashMap<>();

    public void removeItems(Player player, Material material) {
        UUID uuid = player.getUniqueId();
        var matMap = stored.getOrDefault(uuid, new EnumMap<>(Material.class));
        matMap.put(material, ItemUtils.findAndRemove(player.getInventory(), material));
        stored.put(uuid, matMap);
    }

    public void backItems(Player player) {
        UUID uuid = player.getUniqueId();
        var matMap = stored.get(uuid);
        if (matMap == null) return;
        for (var i : matMap.values()) ItemUtils.giveOrDrop(player, i);
        stored.remove(uuid);
    }
}
