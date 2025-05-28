package dev.enco.greatcombat.restrictions.prevention;

import dev.enco.greatcombat.restrictions.CheckedMeta;
import dev.enco.greatcombat.restrictions.InteractionHandler;
import org.bukkit.inventory.ItemStack;
import java.util.EnumSet;

public record PreventableItem(
        ItemStack itemStack,
        String translation,
        EnumSet<PreventionType> types,
        EnumSet<InteractionHandler> handlers,
        EnumSet<CheckedMeta> checkedMetas,
        boolean setMaterialCooldown
) {}
