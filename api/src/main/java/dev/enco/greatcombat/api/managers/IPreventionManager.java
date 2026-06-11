package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.models.IPreventableItem;
import dev.enco.greatcombat.api.models.IWrappedItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPreventionManager extends IManager {
    /**
     * Retrieves the PreventableItem associated with the given ItemStack.
     *
     * @param itemStack The ItemStack to check for prevention
     * @return PreventableItem if found, null otherwise
     */
    @Nullable IPreventableItem getPreventableItem(@NotNull ItemStack itemStack);

    /**
     * Retrieves the PreventableItem associated with the given IWrappedItem.
     *
     * @param i The WrappedItem to check for prevention
     * @return PreventableItem if found, null otherwise
     */
    @Nullable IPreventableItem getPreventableItem(@NotNull IWrappedItem i);
}
