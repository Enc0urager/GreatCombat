package dev.enco.greatcombat.restrictions;

/**
 * Interface for checking if two wrapped items have matching metadata.
 */
public interface MetaChecker {
    /**
     * Checks if two wrapped items have matching metadata.
     *
     * @param first The first wrapped item
     * @param second The second wrapped item
     * @return true if metadata matches, false otherwise
     */
    boolean hasMeta(WrappedItem first, WrappedItem second);
}
