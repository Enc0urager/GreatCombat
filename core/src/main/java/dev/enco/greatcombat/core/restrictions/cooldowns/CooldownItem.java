package dev.enco.greatcombat.core.restrictions.cooldowns;

import dev.enco.greatcombat.api.models.ICooldownItem;
import dev.enco.greatcombat.api.models.InteractionHandler;
import dev.enco.greatcombat.core.restrictions.CheckedMeta;
import dev.enco.greatcombat.core.restrictions.WrappedItem;

import java.util.EnumSet;
import java.util.Set;

public record CooldownItem(
       WrappedItem wrappedItem,
       String translation,
       EnumSet<CheckedMeta> checkedMetas,
       EnumSet<InteractionHandler> handlers,
       int time,
       boolean setMaterialCooldown,
       Set<String> linkedItems
) implements ICooldownItem {}
