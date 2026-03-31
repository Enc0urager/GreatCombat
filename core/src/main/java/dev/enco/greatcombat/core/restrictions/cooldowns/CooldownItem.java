package dev.enco.greatcombat.core.restrictions.cooldowns;

import dev.enco.greatcombat.api.models.ICooldownItem;
import dev.enco.greatcombat.core.restrictions.MetaHandle;
import dev.enco.greatcombat.core.restrictions.WrappedItem;
import java.util.Set;

public record CooldownItem(
       WrappedItem wrappedItem,
       String translation,
       MetaHandle[] checkedMetas,
       Set<String> handlers,
       int time,
       boolean setMaterialCooldown,
       Set<String> linkedItems
) implements ICooldownItem {}
