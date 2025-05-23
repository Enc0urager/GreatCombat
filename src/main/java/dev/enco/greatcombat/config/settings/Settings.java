package dev.enco.greatcombat.config.settings;

import org.bukkit.entity.EntityType;

import java.util.List;

public record Settings(
        int combatTime,
        boolean allowTeleport,
        List<String> ignoredWorlds,
        boolean killOnLeave,
        boolean killOnKick,
        List<String> kickMessages,
        long tickInterval,
        long minTime,
        List<EntityType> ignoredProjectile
) {}
