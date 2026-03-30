package dev.enco.greatcombat.core.commands;

import dev.enco.greatcombat.core.commands.impl.*;
import lombok.Getter;

@Getter
public enum CommandArg {
    RELOAD(ReloadSubcommand.class),
    STOP(StopSubcommand.class),
    STOPALL(StopAllSubcommand.class),
    GIVE(CombatSubcommand.class),
    UPDATE(UpdateSubcommand.class),
    COPY(CopySubcommand.class);

    private final Class<? extends Subcommand> clazz;

    CommandArg(Class<? extends Subcommand> clazz) {
        this.clazz = clazz;
    }
}
