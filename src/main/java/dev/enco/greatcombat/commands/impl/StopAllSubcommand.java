package dev.enco.greatcombat.commands.impl;

import dev.enco.greatcombat.commands.Subcommand;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.manager.CombatManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class StopAllSubcommand implements Subcommand {
    private final CombatManager combatManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        combatManager.stop();
        sender.sendMessage(ConfigManager.getLocale().stopAllSuccess());
        return true;
    }
}
