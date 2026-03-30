package dev.enco.greatcombat.core.restrictions.prevention;

import dev.enco.greatcombat.api.models.IPreventableItem;
import dev.enco.greatcombat.api.models.InteractionHandler;
import dev.enco.greatcombat.api.models.PreventionType;
import dev.enco.greatcombat.core.restrictions.CheckedMeta;
import dev.enco.greatcombat.core.restrictions.WrappedItem;

import java.util.EnumSet;

public record PreventableItem(
        WrappedItem wrappedItem,
        String translation,
        EnumSet<PreventionType> types,
        EnumSet<InteractionHandler> handlers,
        EnumSet<CheckedMeta> checkedMetas,
        boolean setMaterialCooldown
) implements IPreventableItem {}
