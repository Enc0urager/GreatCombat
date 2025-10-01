package dev.enco.greatcombat.config.settings;

import dev.enco.greatcombat.powerups.PowerupType;

import java.util.EnumSet;

public record Powerups(
        EnumSet<PowerupType> preventableDamagerPowerups,
        EnumSet<PowerupType> preventableTargetPowerups,
        EnumSet<PowerupType> disablingDamagerPowerups,
        EnumSet<PowerupType> disablingTargetPowerups
) {}
