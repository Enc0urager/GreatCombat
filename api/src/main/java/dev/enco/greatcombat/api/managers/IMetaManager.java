package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.models.IWrappedItem;
import dev.enco.greatcombat.api.models.MetaChecker;

import java.util.Set;

public interface IMetaManager extends IManager {
    /**
     * Checks if two items are similar based on the specified metadata checks.
     *
     * @param f The first wrapped item to compare
     * @param s The second wrapped item to compare
     * @param checkedMetas The set of metadata types to check
     * @return true if items match all specified metadata checks, false otherwise
     */
    boolean isSimilar(IWrappedItem f, IWrappedItem s, Set<? extends MetaChecker> checkedMetas);
}
