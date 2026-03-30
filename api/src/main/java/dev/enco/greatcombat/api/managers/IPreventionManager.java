package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.models.IPreventableItem;
import org.bukkit.inventory.ItemStack;

public interface IPreventionManager extends IManager {
    /**
     * Retrieves the PreventableItem associated with the given ItemStack.
     *
     * @param itemStack The ItemStack to check for prevention
     * @return PreventableItem if found, null otherwise
     */
    IPreventableItem getPreventableItem(ItemStack itemStack);
}
