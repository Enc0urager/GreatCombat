package dev.enco.greatcombat.core.restrictions;

import dev.enco.greatcombat.api.models.IWrappedItem;
import dev.enco.greatcombat.api.models.MetaChecker;
import dev.enco.greatcombat.core.utils.logger.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MetaHandle implements MetaChecker {
    private final String name;
    private MetaChecker delegate;
    private boolean requiresMeta;

    public void bind(MetaChecker checker) {
        this.delegate = checker;
    }

    @Override
    public boolean matches(IWrappedItem first, IWrappedItem second) {
        if (delegate == null) {
            Logger.warn("Delegating MetaChecker for " + name + " is not initialized yet! Make sure, that you initialized it using IMetaManager#registerChecker");
            return false;
        }
        return delegate.matches(first, second);
    }

    @Override
    public boolean requiresMeta() {
        return requiresMeta;
    }
}
