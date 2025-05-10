package dev.enco.greatcombat.prevent;

import dev.enco.greatcombat.cooldowns.InteractionHandler;
import org.bukkit.Material;

import java.util.List;

public record PreventableItem(
        Material material,
        String translation,
        List<PreventionType> types,
        List<InteractionHandler> handlers
) {}
