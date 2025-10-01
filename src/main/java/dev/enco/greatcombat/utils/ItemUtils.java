package dev.enco.greatcombat.utils;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.utils.logger.Logger;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
        try {
            return ItemStack.deserializeBytes(decoder.decode(s));
        } catch (NoSuchMethodError e) {
            for (var st : ConfigManager.getLocale().outdatedCore()) Logger.error(st);
            GreatCombat.getInstance().getServer().getPluginManager().disablePlugin(GreatCombat.getInstance());
        }
        return null;
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

        for (int i = 0; i < inventory.getSize(); i++) {
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
}
