package dev.enco.greatcombat.config.settings;

import com.google.common.collect.ImmutableSet;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.EnumSet;

public record Settings(
        int combatTime,
        EnumSet<PlayerTeleportEvent.TeleportCause> allowedTpCause,
        ImmutableSet<String> ignoredWorlds,
        boolean killOnLeave,
        boolean killOnKick,
        ImmutableSet<String> kickMessages,
        long tickInterval,
        long minTime,
        ImmutableSet<EntityType> ignoredProjectile
) {}
