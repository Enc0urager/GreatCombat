package dev.enco.greatcombat.api.models;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface IWrappedItem {
    ItemStack itemStack();
    ItemMeta itemMeta();
    boolean hasMeta();
}
