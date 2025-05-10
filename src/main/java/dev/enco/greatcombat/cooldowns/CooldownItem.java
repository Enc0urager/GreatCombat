package dev.enco.greatcombat.cooldowns;

import org.bukkit.Material;

import java.util.List;

public record CooldownItem(
       Material material,
       String translation,
       List<InteractionHandler> handlers,
       int time
) {}
