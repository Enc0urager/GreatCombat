package dev.enco.greatcombat.restrictions.prevention;

import dev.enco.greatcombat.restrictions.CheckedMeta;
import dev.enco.greatcombat.restrictions.InteractionHandler;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;

/**
 * Record representing an item that can be prevented from interaction during combat.
 *
 * @param itemStack The base ItemStack to match against
 * @param translation The display name translation key
 * @param types The types of prevention to apply
 * @param handlers The interaction handlers to prevent
 * @param checkedMetas The item metadata to check for matching
 * @param setMaterialCooldown Whether to set material cooldown visually
 */
public record PreventableItem(
        ItemStack itemStack,
        String translation,
        EnumSet<PreventionType> types,
        EnumSet<InteractionHandler> handlers,
        EnumSet<CheckedMeta> checkedMetas,
        boolean setMaterialCooldown
) {}
