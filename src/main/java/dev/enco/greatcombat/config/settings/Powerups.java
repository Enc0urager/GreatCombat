package dev.enco.greatcombat.config.settings;

import dev.enco.greatcombat.powerups.PowerupType;
import java.util.List;

public record Powerups(
        List<PowerupType> preventableDamagerPowerups,
        List<PowerupType> preventableTargetPowerups,
        List<PowerupType> disablingDamagerPowerups,
        List<PowerupType> disablingTargetPowerups
) {}
