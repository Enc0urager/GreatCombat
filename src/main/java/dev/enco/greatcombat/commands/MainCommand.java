package dev.enco.greatcombat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("greatcombat.admin")) return true;
        if (args.length < 1) {
            sendHelpMessage(sender);
            return true;
        }
        try {
            var arg = CommandArg.valueOf(args[0].toUpperCase());
            arg.getSubcommand().onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
        } catch (IllegalArgumentException e) {
            sendHelpMessage(sender);
        }
        return true;
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "GreatCombat помощь по командам");
        sender.sendMessage("/combat stop (Игрок) - Остановить бой игроку");
        sender.sendMessage("/combat stopall - Остановить все активные бои");
        sender.sendMessage("/combat give (Игрок1) (Игрок2) - Начать поединок между игроками");
        sender.sendMessage("/combat copy - Получить base64 предмета в руке");
    }
}
