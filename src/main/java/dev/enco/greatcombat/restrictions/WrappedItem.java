package dev.enco.greatcombat.restrictions;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public record WrappedItem(
        ItemStack itemStack,
        ItemMeta itemMeta,
        boolean hasMeta
) {
    public static WrappedItem of(ItemStack itemStack) {
        var meta = itemStack.getItemMeta();
        return new WrappedItem(
                itemStack,
                meta,
                meta != null
        );
    }
}
