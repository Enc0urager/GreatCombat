package dev.enco.greatcombat.commands.impl;

import dev.enco.greatcombat.commands.Subcommand;
import dev.enco.greatcombat.manager.CombatManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class StopSubcommand implements Subcommand {
    private final CombatManager combatManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Укажите ник игрока");
            return true;
        }
        var player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Игрок не найден!");
            return true;
        }
        var user = combatManager.getUser(player.getUniqueId());
        if (user == null) {
            sender.sendMessage(ChatColor.RED + "Игрок не в режиме боя");
            return true;
        }
        combatManager.stopCombat(user);
        sender.sendMessage(ChatColor.GREEN + "Игрок больше не в режиме боя!");
        return true;
    }
}
