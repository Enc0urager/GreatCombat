package dev.enco.greatcombat.restrictions.cooldowns;

import dev.enco.greatcombat.restrictions.CheckedMeta;
import dev.enco.greatcombat.restrictions.InteractionHandler;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.Set;

/**
 * Record representing an item that can have cooldowns during combat.
 *
 * @param itemStack The base ItemStack to match against
 * @param translation The display name translation key
 * @param checkedMetas The item metadata to check for matching
 * @param handlers The interaction handlers that trigger cooldown
 * @param time The cooldown time in seconds
 * @param setMaterialCooldown Whether to set material cooldown visually
 * @param linkedItems Items that will be blocked with current item
 */
public record CooldownItem(
       ItemStack itemStack,
       String translation,
       EnumSet<CheckedMeta> checkedMetas,
       EnumSet<InteractionHandler> handlers,
       int time,
       boolean setMaterialCooldown,
       Set<String> linkedItems
) {}
