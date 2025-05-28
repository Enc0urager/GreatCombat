package dev.enco.greatcombat.config.settings;

import com.google.common.collect.ImmutableMap;
import dev.enco.greatcombat.actions.ActionType;
import java.util.List;

public record Messages(
        ImmutableMap<ActionType, List<String>> onStartDamager,
        ImmutableMap<ActionType, List<String>> onStartTarget,
        ImmutableMap<ActionType, List<String>> onStop,
        ImmutableMap<ActionType, List<String>> onItemCooldown,
        ImmutableMap<ActionType, List<String>> onPvpLeave,
        ImmutableMap<ActionType, List<String>> onPvpCommand,
        ImmutableMap<ActionType, List<String>> onInteract,
        ImmutableMap<ActionType, List<String>> onTick
) {}
