package dev.enco.greatcombat.config.settings;

import dev.enco.greatcombat.actions.ActionMap;

public record Messages(
        ActionMap onStartDamager,
        ActionMap onStartTarget,
        ActionMap onStop,
        ActionMap onItemCooldown,
        ActionMap onPvpLeave,
        ActionMap onPvpCommand,
        ActionMap onInteract,
        ActionMap onTick,
        ActionMap onPlayerCommand,
        ActionMap onJoin,
        ActionMap onMerge,
        ActionMap onCooldownExpired,
        ActionMap onItemHeld
) {}
