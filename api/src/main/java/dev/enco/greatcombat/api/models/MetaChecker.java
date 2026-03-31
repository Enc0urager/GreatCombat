package dev.enco.greatcombat.api.models;

/**
 * Strategy interface for comparing metadata between two {@link IWrappedItem}s.
 * <p>
 * Implementations define specific comparison rules (e.g. name, lore, enchantments).
 */
public interface MetaChecker {
    /**
     * Checks whether two wrapped items match, according to this checker.
     *
     * @param first the first item
     * @param second the second item
     * @return true if the items match, false otherwise
     */
    boolean hasMeta(IWrappedItem first, IWrappedItem second);
    /**
     * Indicates whether this checker requires both items to have item meta.
     * <p>
     * If true, comparison should be skipped or fail when one of the items
     * does not have a meta.
     *
     * @return true if metadata is required, false otherwise
     */
    boolean requiresMeta();
}
