package dev.enco.greatcombat.commands;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.commands.impl.CombatSubcommand;
import dev.enco.greatcombat.commands.impl.CopySubcommand;
import dev.enco.greatcombat.commands.impl.StopAllSubcommand;
import dev.enco.greatcombat.commands.impl.StopSubcommand;
import lombok.Getter;

@Getter
public enum CommandArg {
    STOP(new StopSubcommand(GreatCombat.getInstance().getCombatManager())),
    STOPALL(new StopAllSubcommand(GreatCombat.getInstance().getCombatManager())),
    GIVE(new CombatSubcommand()),
    COPY(new CopySubcommand());

    private Subcommand subcommand;

    CommandArg(Subcommand subcommand) {
        this.subcommand = subcommand;
    }
}
