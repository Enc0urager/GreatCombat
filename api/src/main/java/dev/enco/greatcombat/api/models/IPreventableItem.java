package dev.enco.greatcombat.api.models;

import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;

/**
 * Represents an item whose usage must be prevented under certain conditions.
 */
public interface IPreventableItem {
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
     * Returns prevention types applied to this item.
     *
     * @return set of prevention types
     */
    @NotNull EnumSet<PreventionType> types();

    /**
     * Returns interaction handlers that trigger prevention.
     *
     * @return set of handlers
     */
    @NotNull Set<String> handlers();

    /**
     * Returns metadata checkers used for matching items.
     *
     * @return array of meta-checkers
     */
    @NotNull MetaChecker[] checkedMetas();

    /**
     * Indicates whether a visual material cooldown should be applied.
     *
     * @return true if material cooldown is enabled
     */
    boolean setMaterialCooldown();
}
