package dev.enco.greatcombat.config.settings;

import com.google.common.collect.ImmutableSet;
import dev.enco.greatcombat.powerups.PowerupType;

public record Powerups(
        ImmutableSet<PowerupType> preventableDamagerPowerups,
        ImmutableSet<PowerupType> preventableTargetPowerups,
        ImmutableSet<PowerupType> disablingDamagerPowerups,
        ImmutableSet<PowerupType> disablingTargetPowerups
) {}
