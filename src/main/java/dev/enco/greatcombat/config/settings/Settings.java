package dev.enco.greatcombat.config.settings;

import com.google.common.collect.ImmutableSet;
import org.bukkit.entity.EntityType;

public record Settings(
        int combatTime,
        boolean allowTeleport,
        ImmutableSet<String> ignoredWorlds,
        boolean killOnLeave,
        boolean killOnKick,
        ImmutableSet<String> kickMessages,
        long tickInterval,
        long minTime,
        ImmutableSet<EntityType> ignoredProjectile
) {}
