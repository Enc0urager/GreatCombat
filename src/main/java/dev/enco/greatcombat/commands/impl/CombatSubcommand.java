package dev.enco.greatcombat.commands.impl;

import dev.enco.greatcombat.commands.Subcommand;
import dev.enco.greatcombat.config.ConfigManager;
import dev.enco.greatcombat.config.settings.Locale;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

@SuppressWarnings("removal")
public class CombatSubcommand implements Subcommand {
    private final PluginManager pm = Bukkit.getPluginManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        final Locale locale = ConfigManager.getLocale();
        if (args.length < 2) {
            sender.sendMessage(locale.specify2Players());
            return true;
        }
        var player1 = Bukkit.getPlayer(args[0]);
        if (player1 == null) {
            sender.sendMessage(MessageFormat.format(locale.playerNotFound(), args[0]));
            return true;
        }
        var player2 = Bukkit.getPlayer(args[1]);
        if (player2 == null) {
            sender.sendMessage(MessageFormat.format(locale.playerNotFound(), args[1]));
            return true;
        }
        pm.callEvent(new EntityDamageByEntityEvent(player1, player2, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 0.0D));
        sender.sendMessage(locale.combatStarted());
        return true;
    }
}
