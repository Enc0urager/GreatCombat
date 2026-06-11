package dev.enco.greatcombat.api.models;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Represents an item whose usage must be prevented for some time under certain conditions.
 */
public interface ICooldownItem {
    /**
     * Returns wrapped item representation.
     *
     * @return wrapped item
     */
    @NotNull IWrappedItem wrappedItem();

    /**
     * Returns localized translation key or display string.
     *
     * @return translation
     */
    @NotNull String translation();

    /**
     * Returns metadata checkers used for matching items.
     *
     * @return array of meta-checkers
     */
    @NotNull MetaChecker[] checkedMetas();

    /**
     * Returns interaction handlers that trigger cooldown.
     *
     * @return set of handlers
     */
    @NotNull Set<String> handlers();

    /**
     * Returns cooldown duration in seconds.
     *
     * @return cooldown time
     */
    int time();

    /**
     * Indicates whether a visual material cooldown should be applied.
     *
     * @return true if enabled
     */
    boolean setMaterialCooldown();

    /**
     * Returns identifiers of linked items to which cooldown also must be applied
     *
     * @return set of linked item IDs
     */
    @NotNull Set<String> linkedItems();
}
