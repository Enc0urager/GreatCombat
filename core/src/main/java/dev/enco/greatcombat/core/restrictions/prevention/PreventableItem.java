package dev.enco.greatcombat.core.restrictions.prevention;

import dev.enco.greatcombat.api.models.IPreventableItem;
import dev.enco.greatcombat.api.models.PreventionType;
import dev.enco.greatcombat.core.restrictions.CheckerHandle;
import dev.enco.greatcombat.core.restrictions.WrappedItem;

import java.util.EnumSet;
import java.util.Set;

public record PreventableItem(
        WrappedItem wrappedItem,
        String translation,
        EnumSet<PreventionType> types,
        Set<String> handlers,
        CheckerHandle[] checkedMetas,
        boolean setMaterialCooldown
) implements IPreventableItem {}
