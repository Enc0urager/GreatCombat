package dev.enco.greatcombat.config.settings;

import dev.enco.greatcombat.actions.ActionType;
import java.util.List;
import java.util.Map;

public record Messages(
        Map<ActionType, List<String>> onStartDamager,
        Map<ActionType, List<String>> onStartTarget,
        Map<ActionType, List<String>> onStop,
        Map<ActionType, List<String>> onItemCooldown,
        Map<ActionType, List<String>> onPvpLeave,
        Map<ActionType, List<String>> onPvpCommand,
        Map<ActionType, List<String>> onInteract
) {}
