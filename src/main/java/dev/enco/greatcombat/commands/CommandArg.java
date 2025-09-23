package dev.enco.greatcombat.commands;

import dev.enco.greatcombat.commands.impl.*;
import lombok.Getter;

@Getter
public enum CommandArg {
    RELOAD(new ReloadSubcommand()),
    STOP(new StopSubcommand()),
    STOPALL(new StopAllSubcommand()),
    GIVE(new CombatSubcommand()),
    UPDATE(new UpdateSubcommand()),
    COPY(new CopySubcommand());

    private Subcommand subcommand;

    CommandArg(Subcommand subcommand) {
        this.subcommand = subcommand;
    }
}
