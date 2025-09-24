package dev.enco.greatcombat.commands.impl;

import dev.enco.greatcombat.GreatCombat;
import dev.enco.greatcombat.commands.Subcommand;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Locale;
import dev.enco.greatcombat.manager.CombatManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.List;

public class CombatSubcommand implements Subcommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        final Locale locale = ConfigManager.getLocale();
        if (args.length < 1) {
            sender.sendMessage(locale.notSpecifiedPlayer());
            return true;
        }
        var player1 = Bukkit.getPlayer(args[0]);
        if (player1 == null) {
            sender.sendMessage(MessageFormat.format(locale.playerNotFound(), args[0]));
            return true;
        }
        final CombatManager combatManager = GreatCombat.getInstance().getCombatManager();
        if (args.length == 1) {
            combatManager.startSingle(player1);
            sender.sendMessage(locale.combatStarted());
            return true;
        }
        var player2 = Bukkit.getPlayer(args[1]);
        if (player2 == null) {
            sender.sendMessage(MessageFormat.format(locale.playerNotFound(), args[1]));
            return true;
        }
        combatManager.startCombat(player1, player2);
        sender.sendMessage(locale.combatStarted());
        return true;
    }

    @Override
    public @Nullable List<String> onTab() {
        return null;
    }
}
