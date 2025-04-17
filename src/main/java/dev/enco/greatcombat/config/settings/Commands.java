package dev.enco.greatcombat.config.settings;

import dev.enco.greatcombat.listeners.CommandsType;

import java.util.List;

public record Commands(
        CommandsType changeType,
        boolean changeComplete,
        List<String> commands
) {}
