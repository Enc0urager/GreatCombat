package dev.enco.greatcombat.config.settings;

import com.google.common.collect.ImmutableMap;
import dev.enco.greatcombat.actions.ActionType;
import dev.enco.greatcombat.actions.context.Context;

import java.util.List;

public record Messages(
        ImmutableMap<ActionType, List<Context>> onStartDamager,
        ImmutableMap<ActionType, List<Context>> onStartTarget,
        ImmutableMap<ActionType, List<Context>> onStop,
        ImmutableMap<ActionType, List<Context>> onItemCooldown,
        ImmutableMap<ActionType, List<Context>> onPvpLeave,
        ImmutableMap<ActionType, List<Context>> onPvpCommand,
        ImmutableMap<ActionType, List<Context>> onInteract,
        ImmutableMap<ActionType, List<Context>> onTick
) {}
