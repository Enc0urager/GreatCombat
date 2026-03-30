package dev.enco.greatcombat.core.utils;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Utility class for serializing and deserializing ItemStacks to/from Base64 strings.
 * Used for storing ItemStack in configuration files.
 */
@UtilityClass
public class ItemUtils {
    private final Base64.Encoder encoder = Base64.getEncoder();
    private final Base64.Decoder decoder = Base64.getDecoder();

    /**
     * Encodes an ItemStack to a Base64 string.
     *
     * @param item The ItemStack to encode
     * @return Base64 encoded string representation of the ItemStack
     */
    public String encode(ItemStack item) {
        return encoder.encodeToString(item.serializeAsBytes());
    }

    /**
     * Decodes a Base64 string back to an ItemStack.
     *
     * @param s The Base64 string to decode
     * @return The decoded ItemStack, or null if decoding fails
     */
    public ItemStack decode(String s) {
        return ItemStack.deserializeBytes(decoder.decode(s));
    }

    /**
     * Finds all ItemStacks of the specified material in the inventory and removes them.
     *
     * @param inventory inventory to search in
     * @param material material to search for
     * @return Array of cloned ItemStacks that were removed
     */
    public ItemStack[] findAndRemove(Inventory inventory, Material material) {
        List<ItemStack> result = new ArrayList<>();

        int size = inventory.getSize();

        for (int i = 0; i < size; i++) {
            var stack = inventory.getItem(i);
            if (stack != null && stack.getType() == material) {
                result.add(stack.clone());
                inventory.setItem(i, null);
            }
        }

        return result.toArray(new ItemStack[0]);
    }

    /**
     * Gives multiple items to the player, dropping leftovers.
     *
     * @param player player to give items to
     * @param items Array of items to give
     */
    public void giveOrDrop(Player player, ItemStack[] items) {
        var over = player.getInventory().addItem(items);
        if (!over.isEmpty()) {
            World world = player.getWorld();
            Location location = player.getLocation();
            for (var stack : over.values())
                world.dropItemNaturally(location, stack);
        }
    }

    private final Reference2ObjectMap<UUID, EnumMap<Material, ItemStack[]>> stored = new Reference2ObjectOpenHashMap<>();

    /**
     * Removes items of a specific material from player's inventory and stores them in a cache
     *
     * @param player The player whose items will be removed.
     * @param material The material of the items to be removed
     */
    public void removeItems(Player player, Material material) {
        UUID uuid = player.getUniqueId();
        var matMap = stored.getOrDefault(uuid, new EnumMap<>(Material.class));
        matMap.put(material, findAndRemove(player.getInventory(), material));
        stored.put(uuid, matMap);
    }

    /**
     * Restores all previously stored items to the player's inventory.
     *
     * @param player The player to whom the items should be returned.
     */
    public void backItems(Player player) {
        UUID uuid = player.getUniqueId();
        var matMap = stored.get(uuid);
        if (matMap == null) return;
        for (var i : matMap.values()) giveOrDrop(player, i);
        stored.remove(uuid);
    }
}
