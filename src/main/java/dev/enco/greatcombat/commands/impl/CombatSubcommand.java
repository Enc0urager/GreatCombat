package dev.enco.greatcombat.commands.impl;

import dev.enco.greatcombat.commands.Subcommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class CombatSubcommand implements Subcommand {
    private final PluginManager pm = Bukkit.getPluginManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Укажите 2 никнейма игроков");
            return true;
        }
        var player1 = Bukkit.getPlayer(args[0]);
        if (player1 == null) {
            sender.sendMessage(ChatColor.RED + "Игрок №1 не найден!");
            return true;
        }
        var player2 = Bukkit.getPlayer(args[1]);
        if (player2 == null) {
            sender.sendMessage(ChatColor.RED + "Игрок №2 не найден!");
            return true;
        }
        pm.callEvent(new EntityDamageByEntityEvent(player1, player2, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 0.0D));
        sender.sendMessage(ChatColor.GREEN + "Режим боя начат если не был предотвращён!");
        return true;
    }
}
