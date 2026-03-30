package dev.enco.greatcombat.core.restrictions.meta;

import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.IMetaManager;
import dev.enco.greatcombat.api.models.IWrappedItem;
import dev.enco.greatcombat.api.models.MetaChecker;

import java.util.Set;

@Singleton
public class MetaManager implements IMetaManager {
    @Override
    public boolean isSimilar(IWrappedItem f, IWrappedItem s, Set<? extends MetaChecker> checkedMetas) {
        for (var checker : checkedMetas) {
            if (checker.requiresMeta()) {
                if (f.hasMeta() != s.hasMeta()) return false;
                if (!f.hasMeta()) continue;
            }
            if (!checker.hasMeta(f, s)) return false;
        }
        return true;
    }
}
