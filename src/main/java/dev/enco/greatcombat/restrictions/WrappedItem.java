package dev.enco.greatcombat.restrictions;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Record representing a wrapped item with its metadata for efficient comparison.
 *
 * @param itemStack The original ItemStack
 * @param itemMeta The ItemMeta of the item
 * @param hasMeta Whether the item has metadata
 */
public record WrappedItem(
        ItemStack itemStack,
        ItemMeta itemMeta,
        boolean hasMeta
) {
    /**
     * Creates a WrappedItem from an ItemStack with ItemMeta.
     *
     * @param itemStack The ItemStack to wrap
     * @return WrappedItem containing the item and its metadata
     */
    public static WrappedItem withMeta(ItemStack itemStack) {
        var meta = itemStack.getItemMeta();
        return new WrappedItem(
                itemStack,
                meta,
                meta != null
        );
    }

    /**
     * Creates a WrappedItem from an ItemStack without ItemMeta.
     *
     * @param itemStack The ItemStack to wrap
     * @return WrappedItem containing the item and its metadata
     */
    public static WrappedItem noMeta(ItemStack itemStack) {
        return new WrappedItem(
                itemStack,
                null,
                false
        );
    }
}
