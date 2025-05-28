package dev.enco.greatcombat.config.settings;

import com.google.common.collect.ImmutableSet;
import dev.enco.greatcombat.listeners.CommandsType;

public record Commands(
        CommandsType changeType,
        boolean changeComplete,
        ImmutableSet<String> commands
) {}
