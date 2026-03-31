package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.models.IWrappedItem;
import dev.enco.greatcombat.api.models.MetaChecker;

public interface IMetaManager extends IManager {
    /**
     * Checks if two items are similar based on the specified metadata checks.
     *
     * @param f The first wrapped item to compare
     * @param s The second wrapped item to compare
     * @param checkedMetas The array of metadata types to check
     * @return true if items match all specified metadata checks, false otherwise
     */
    boolean isSimilar(IWrappedItem f, IWrappedItem s, MetaChecker[] checkedMetas);

    void registerChecker(String id, MetaChecker checker);

    MetaChecker getByID(String id);
}
