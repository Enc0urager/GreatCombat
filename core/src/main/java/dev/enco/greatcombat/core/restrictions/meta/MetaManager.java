package dev.enco.greatcombat.core.restrictions.meta;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.IMetaManager;
import dev.enco.greatcombat.api.models.IWrappedItem;
import dev.enco.greatcombat.api.models.MetaChecker;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.restrictions.DefaultCheckers;
import dev.enco.greatcombat.core.restrictions.MetaHandle;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class MetaManager implements IMetaManager {
    private final Map<String, MetaHandle> registry = new HashMap<>();

    @Inject
    public MetaManager(ConfigManager configManager) {
        registerDefaults(configManager);
    }

    private void registerDefaults(ConfigManager configManager) {
        for (var meta : DefaultCheckers.values())
            registerChecker(meta.name(), meta);

        for (var name : configManager.getCheckers())
            registerChecker(name, null);
    }

    @Override
    public void registerChecker(String name, MetaChecker checker) {
        getByID(name).bind(checker);
    }

    @Override
    public MetaHandle getByID(String name) {
        return registry.computeIfAbsent(name.toUpperCase(), MetaHandle::new);
    }

    @Override
    public boolean isSimilar(IWrappedItem f, IWrappedItem s, MetaChecker[] checkedMetas) {
        for (MetaChecker checker : checkedMetas) {
            if (checker.requiresMeta()) {
                if (f.hasMeta() != s.hasMeta()) return false;
                if (!f.hasMeta()) continue;
            }
            if (!checker.matches(f, s)) return false;
        }
        return true;
    }
}
