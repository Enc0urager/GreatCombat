package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.models.IWrappedItem;
import dev.enco.greatcombat.api.models.MetaChecker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IMetaManager extends IManager {
    /**
     * Checks if two items are similar based on the specified metadata checks.
     *
     * @param f The first wrapped item to compare
     * @param s The second wrapped item to compare
     * @param checkedMetas The array of metadata types to check
     * @return true if items match all specified metadata checks, false otherwise
     */
    boolean isSimilar(@NotNull IWrappedItem f,
                      @NotNull IWrappedItem s,
                      @NotNull MetaChecker[] checkedMetas);

    /**
     * Registers a meta checker under the specified id
     *
     * @param id unique checker id
     * @param checker checker implementation to register
     */
    void registerChecker(@NotNull String id,
                         @NotNull MetaChecker checker);

    /**
     * Retrieves a registered meta checker by its id
     *
     * @param id checker id
     * @return associated registered checker instance or null overwise
     */
    @Nullable MetaChecker getByID(@NotNull String id);
}
