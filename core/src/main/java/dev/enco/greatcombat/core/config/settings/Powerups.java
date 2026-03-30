package dev.enco.greatcombat.core.config.settings;

import dev.enco.greatcombat.api.models.PowerupType;

import java.util.EnumSet;

public record Powerups(
        EnumSet<PowerupType> preventableDamagerPowerups,
        EnumSet<PowerupType> preventableTargetPowerups,
        EnumSet<PowerupType> disablingDamagerPowerups,
        EnumSet<PowerupType> disablingTargetPowerups
) {}
