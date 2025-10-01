package dev.enco.greatcombat.config.settings;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import dev.enco.greatcombat.actions.ActionType;
import dev.enco.greatcombat.actions.context.Context;

public record Messages(
        ImmutableMap<ActionType, ImmutableList<Context>> onStartDamager,
        ImmutableMap<ActionType, ImmutableList<Context>> onStartTarget,
        ImmutableMap<ActionType, ImmutableList<Context>> onStop,
        ImmutableMap<ActionType, ImmutableList<Context>> onItemCooldown,
        ImmutableMap<ActionType, ImmutableList<Context>> onPvpLeave,
        ImmutableMap<ActionType, ImmutableList<Context>> onPvpCommand,
        ImmutableMap<ActionType, ImmutableList<Context>> onInteract,
        ImmutableMap<ActionType, ImmutableList<Context>> onTick,
        ImmutableMap<ActionType, ImmutableList<Context>> onPlayerCommand,
        ImmutableMap<ActionType, ImmutableList<Context>> onJoin,
        ImmutableMap<ActionType, ImmutableList<Context>> onMerge
) {}
