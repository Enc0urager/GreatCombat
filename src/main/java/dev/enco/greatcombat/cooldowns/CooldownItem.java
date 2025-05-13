package dev.enco.greatcombat.cooldowns;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public record CooldownItem(
       ItemStack itemStack,
       String translation,
       List<InteractionHandler> handlers,
       int time,
       boolean setMaterialCooldown
) {}
