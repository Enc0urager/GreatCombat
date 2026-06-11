package dev.enco.greatcombat.api.models;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @NotNull ItemStack itemStack();
    /**
     * Returns the associated {@link ItemMeta}.
     *
     * @return the item meta, or null if not present
     */
    @Nullable ItemMeta itemMeta();
    /**
     * Indicates whether this item has metadata.
     *
     * @return true if metadata is present, false otherwise
     */
    boolean hasMeta();
}
