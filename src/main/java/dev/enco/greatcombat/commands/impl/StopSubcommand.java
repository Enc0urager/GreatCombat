package dev.enco.greatcombat.commands.impl;

import dev.enco.greatcombat.commands.Subcommand;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Locale;
import dev.enco.greatcombat.manager.CombatManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

@RequiredArgsConstructor
public class StopSubcommand implements Subcommand {
    private final CombatManager combatManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        final Locale locale = ConfigManager.getLocale();
        if (args.length < 1) {
            sender.sendMessage(locale.notSpecifiedPlayer());
            return true;
        }
        var player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(MessageFormat.format(locale.playerNotFound(), args[0]));
            return true;
        }
        var user = combatManager.getUser(player.getUniqueId());
        if (user == null) {
            sender.sendMessage(locale.playerNotInCombat());
            return true;
        }
        combatManager.stopCombat(user);
        sender.sendMessage(locale.stopSuccess());
        return true;
    }
}
