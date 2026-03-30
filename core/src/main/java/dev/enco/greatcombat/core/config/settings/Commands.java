package dev.enco.greatcombat.core.config.settings;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import dev.enco.greatcombat.core.listeners.CommandsType;

import java.util.Set;

public record Commands(
        CommandsType changeType,
        boolean changeComplete,
        ImmutableMap<String, Set<String>> commands,
        ImmutableSet<String> playerCommands
) {}
