package dev.enco.greatcombat.api.models;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Abstraction representing a wrapped {@link ItemStack} with cached item meta.
 * <p>
 * This interface is used to avoid repeated meta-extraction and to provide
 * a unified structure for item comparison operations.
 */
public interface IWrappedItem {
    /**
     * Returns the underlying {@link ItemStack}.
     *
     * @return the original item stack
     */
    ItemStack itemStack();
    /**
     * Returns the associated {@link ItemMeta}.
     *
     * @return the item meta, or null if not present
     */
    ItemMeta itemMeta();
    /**
     * Indicates whether this item has metadata.
     *
     * @return true if metadata is present, false otherwise
     */
    boolean hasMeta();
}
