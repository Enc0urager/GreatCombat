package dev.enco.greatcombat.api.models;

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
    IWrappedItem wrappedItem();

    /**
     * Returns localized translation key or display string.
     *
     * @return translation
     */
    String translation();

    /**
     * Returns prevention types applied to this item.
     *
     * @return set of prevention types
     */
    EnumSet<PreventionType> types();

    /**
     * Returns interaction handlers that trigger prevention.
     *
     * @return set of handlers
     */
    EnumSet<InteractionHandler> handlers();

    /**
     * Returns metadata checkers used for matching items.
     *
     * @return set of meta-checkers
     */
    Set<? extends MetaChecker> checkedMetas();

    /**
     * Indicates whether a visual material cooldown should be applied.
     *
     * @return true if material cooldown is enabled
     */
    boolean setMaterialCooldown();
}
