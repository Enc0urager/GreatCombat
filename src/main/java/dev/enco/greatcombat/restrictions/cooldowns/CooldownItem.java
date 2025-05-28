package dev.enco.greatcombat.restrictions.cooldowns;

import dev.enco.greatcombat.restrictions.CheckedMeta;
import dev.enco.greatcombat.restrictions.InteractionHandler;
import org.bukkit.inventory.ItemStack;
import java.util.EnumSet;


public record CooldownItem(
       ItemStack itemStack,
       String translation,
       EnumSet<CheckedMeta> checkedMetas,
       EnumSet<InteractionHandler> handlers,
       int time,
       boolean setMaterialCooldown
) {}
