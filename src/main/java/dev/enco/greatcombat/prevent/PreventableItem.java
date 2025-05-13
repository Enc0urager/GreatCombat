package dev.enco.greatcombat.prevent;

import dev.enco.greatcombat.cooldowns.InteractionHandler;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public record PreventableItem(
        ItemStack itemStack,
        String translation,
        List<PreventionType> types,
        List<InteractionHandler> handlers
) {}
